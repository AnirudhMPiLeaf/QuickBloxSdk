
package com.quickblox.quickblox_sdk.file;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBProgressCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.quickblox_sdk.base.BaseModule;
import com.quickblox.quickblox_sdk.event.EventHandler;
import com.quickblox.quickblox_sdk.utils.EventsUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
public class FileModule implements BaseModule {
    static final String CHANNEL_NAME = "FlutterQBFileChannel";

    private static final String SUBSCRIBE_UPLOAD_PROGRESS_METHOD = "subscribeUploadProgress";
    private static final String UNSUBSCRIBE_UPLOAD_PROGRESS_METHOD = "unsubscribeUploadProgress";
    private static final String UPLOAD_METHOD = "upload";
    private static final String GET_INFO_METHOD = "getInfo";
    private static final String GET_PUBLIC_URL_METHOD = "getPublicURL";
    private static final String GET_PRIVATE_URL_METHOD = "getPrivateURL";

    private Set<UploadProgressItem> uploadProgressItemSet = new CopyOnWriteArraySet<>();
    private Context context;
    private BinaryMessenger binaryMessenger;

    public FileModule(Context context, BinaryMessenger binaryMessenger) {
        this.context = context;
        this.binaryMessenger = binaryMessenger;
        initEventHandler();
    }

    @Override
    public void initEventHandler() {
        EventHandler.init(FileConstants.UploadProgress.FILE_UPLOAD_PROGRESS, binaryMessenger);
    }

    @Override
    public String getChannelName() {
        return CHANNEL_NAME;
    }

    @Override
    public MethodChannel.MethodCallHandler getMethodHandler() {
        return this::handleMethod;
    }

    @Override
    public void handleMethod(MethodCall methodCall, MethodChannel.Result result) {
        Map<String, Object> data = methodCall.arguments();
        switch (methodCall.method) {
            case SUBSCRIBE_UPLOAD_PROGRESS_METHOD:
                subscribeUploadProgress(data, result);
                break;
            case UNSUBSCRIBE_UPLOAD_PROGRESS_METHOD:
                unsubscribeUploadProgress(data, result);
                break;
            case UPLOAD_METHOD:
                upload(data, result);
                break;
            case GET_INFO_METHOD:
                getInfo(data, result);
                break;
            case GET_PUBLIC_URL_METHOD:
                getPublicURL(data, result);
                break;
            case GET_PRIVATE_URL_METHOD:
                getPrivateURL(data, result);
                break;
        }
    }

    private UploadProgressItem getUploadProgressItem(String url) {
        UploadProgressItem uploadProgressItem = null;
        if (uploadProgressItemSet.contains(new UploadProgressItem(url))) {
            for (UploadProgressItem item : uploadProgressItemSet) {
                if (item.equals(new UploadProgressItem(url))) {
                    uploadProgressItem = item;
                    break;
                }
            }
        }
        return uploadProgressItem;
    }

    private void subscribeUploadProgress(Map<String, Object> data, MethodChannel.Result result) {
        String url = data != null && data.containsKey("url") ? (String) data.get("url") : null;

        if (TextUtils.isEmpty(url)) {
            result.error("The url is required parameter", null, null);
            return;
        }

        UploadProgressItem uploadProgressItem = getUploadProgressItem(url);
        if (uploadProgressItem == null) {
            uploadProgressItem = new UploadProgressItem(url);
        }
        uploadProgressItem.subscribe(true);
        uploadProgressItemSet.add(uploadProgressItem);
        result.success(null);
    }

    private void unsubscribeUploadProgress(Map<String, Object> data, MethodChannel.Result result) {
        String url = data != null && data.containsKey("url") ? (String) data.get("url") : null;

        if (TextUtils.isEmpty(url)) {
            result.error("The url is required parameter", null, null);
            return;
        }

        UploadProgressItem uploadProgressItem = getUploadProgressItem(url);
        if (uploadProgressItem != null) {
            uploadProgressItem.subscribe(false);
            uploadProgressItemSet.remove(uploadProgressItem);
        }
        result.success(null);
    }

    private void upload(Map<String, Object> data, final MethodChannel.Result result) {
        String url = data != null && data.containsKey("url") ? (String) data.get("url") : null;
        boolean isPublic = data != null && data.containsKey("public") && (boolean) data.get("public");

        if (TextUtils.isEmpty(url)) {
            result.error("Parameter uriString is required", null, null);
            return;
        }

        new LoadFileFromUrlTask(url, isPublic, result).execute();
    }

    private class LoadFileFromUrlTask extends AsyncTask<Void, Void, File> {
        private static final String SCHEME_CONTENT_GOOGLE = "content://com.google.android";
        private static final String FILE_PREFIX = "file://";

        private static final int BUFFER_SIZE_2_MB = 2048;

        private MethodChannel.Result result;
        private String uriString;
        private boolean isPublic;
        private String error = "";

        LoadFileFromUrlTask(String uriString, boolean isPublic, MethodChannel.Result result) {
            this.result = result;
            this.uriString = uriString;
            this.isPublic = isPublic;
        }

        @Override
        protected File doInBackground(Void... voids) {
            Uri fileUri = Uri.parse(uriString);
            File file = null;

            boolean isFromGoogleApp = fileUri.toString().startsWith(SCHEME_CONTENT_GOOGLE);
            boolean isKitKatAndUpper = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(fileUri.getScheme()) && !isFromGoogleApp && !isKitKatAndUpper) {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(fileUri, filePathColumn, null, null, null);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        file = new File(filePath);
                    }
                    cursor.close();
                }
            } else {
                try {
                    if (!uriString.contains(FILE_PREFIX)) {
                        fileUri = Uri.parse(FILE_PREFIX + uriString);
                    }
                    file = buildFile(fileUri);
                } catch (Exception e) {
                    e.printStackTrace();
                    error = e.getMessage();
                }
            }

            return file;
        }

        private File buildFile(Uri uri) throws Exception {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

            InputStream inputStream = new FileInputStream(fileDescriptor);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            File parentDir = context.getCacheDir();

            String fileExtension;
            try {
                fileExtension = getFileExtension(uri);
            } catch (Exception e) {
                throw new Exception("Didn't get file extension");
            }

            String fileName = uriString.substring(uriString.lastIndexOf("/") + 1);
            fileName = fileName.replace(" ", "_");

            fileName = System.currentTimeMillis() + fileName;

            if (!uriContainsExtension(uri)) {
                fileName = fileName + "." + fileExtension;
            }

            fileName = System.currentTimeMillis() + "_" + fileName;

            File resultFile = new File(parentDir, fileName);

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(resultFile));

            byte[] buffer = new byte[BUFFER_SIZE_2_MB];
            int length;

            try {
                while ((length = bufferedInputStream.read(buffer)) > 0) {
                    bufferedOutputStream.write(buffer, 0, length);
                }
            } catch (Exception e) {
                throw new IOException("Didn't write a file", e);
            } finally {
                parcelFileDescriptor.close();
                bufferedInputStream.close();
                bufferedOutputStream.close();
            }

            return resultFile;
        }

        private boolean uriContainsExtension(Uri uri) {
            String path = uri.getPath();
            boolean contains = false;
            try {
                contains = path.substring(path.lastIndexOf(".") + 1).length() >= 3;
            } catch (Exception e) {
                //ignore
            }
            return contains;
        }

        private String getFileExtension(Uri uri) throws Exception {
            String fileExtension;
            String path = uri.getPath();

            if (uriContainsExtension(uri)) {
                fileExtension = path.substring(path.lastIndexOf(".") + 1);
            } else if (uri.getScheme() != null && uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                ContentResolver contentResolver = context.getContentResolver();
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                fileExtension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
            } else if (uri.getScheme() != null && uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
                String sourceFileType = URLConnection.guessContentTypeFromStream(
                        new BufferedInputStream(new FileInputStream(new File(path))));
                fileExtension = sourceFileType.substring(sourceFileType.lastIndexOf("/") + 1);
            } else {
                fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            }
            return fileExtension;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file != null && file.length() > 0) {
                uploadFile(file, isPublic, result, uriString);
            } else {
                result.error("Can't load file from uriString: " + uriString
                        + (TextUtils.isEmpty(error) ? "" : "\n Error: " + error), null, null);
            }
        }
    }

    private void uploadFile(final File file, boolean isPublic, final MethodChannel.Result result, final String url) {
        final UploadProgressItem uploadProgressItem = getUploadProgressItem(url);
        QBContent.uploadFileTask(file, isPublic, "", new QBProgressCallback() {
            @Override
            public void onProgressUpdate(int progressValue) {
                if (uploadProgressItem != null && uploadProgressItem.subscribed()) {
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("url", url);
                    payload.put("progress", progressValue);

                    String eventName = FileConstants.UploadProgress.FILE_UPLOAD_PROGRESS;
                    Map data = EventsUtil.buildPayload(eventName, payload);
                    EventHandler.sendEvent(eventName, data);
                }
                if (uploadProgressItem != null && progressValue >= 100) {
                    uploadProgressItemSet.remove(uploadProgressItem);
                }
            }
        }).performAsync(new QBEntityCallback<QBFile>() {
            @Override
            public void onSuccess(QBFile qbFile, Bundle params) {
                Map file = FileMapper.qbFileToMap(qbFile);
                result.success(file);
            }

            @Override
            public void onError(QBResponseException responseException) {
                result.error(responseException.getMessage(), null, null);
            }
        });
    }

    private void getInfo(Map<String, Object> data, final MethodChannel.Result result) {
        Integer id = data != null && data.containsKey("id") ? (int) data.get("id") : null;

        if (id == null || id <= 0) {
            result.error("The id is required parameter", null, null);
            return;
        }

        QBContent.getFile(id).performAsync(new QBEntityCallback<QBFile>() {
            @Override
            public void onSuccess(QBFile qbFile, Bundle params) {
                Map file = FileMapper.qbFileToMap(qbFile);
                result.success(file);
            }

            @Override
            public void onError(QBResponseException responseException) {
                result.error(responseException.getMessage(), null, null);
            }
        });
    }

    private void getPublicURL(Map<String, Object> data, final MethodChannel.Result result) {
        String uid = data != null && data.containsKey("uid") ? (String) data.get("uid") : null;

        if (TextUtils.isEmpty(uid)) {
            result.error("The id is required parameter", null, null);
            return;
        }

        String publicUrl = QBFile.getPublicUrlForUID(uid);
        result.success(publicUrl);
    }

    private void getPrivateURL(Map<String, Object> data, final MethodChannel.Result result) {
        String uid = data != null && data.containsKey("uid") ? (String) data.get("uid") : null;

        if (TextUtils.isEmpty(uid)) {
            result.error("The id is required parameter", null, null);
            return;
        }

        String privateUrl = QBFile.getPrivateUrlForUID(uid);
        result.success(privateUrl);
    }

    private class UploadProgressItem {
        private String url;
        private boolean subscribe = false;

        UploadProgressItem(String url) {
            this.url = url;
        }

        void subscribe(boolean subscribe) {
            this.subscribe = subscribe;
        }

        boolean subscribed() {
            return subscribe;
        }

        @Override
        public boolean equals(Object obj) {
            boolean equals;
            if (obj instanceof UploadProgressItem) {
                equals = this.url.equals(((UploadProgressItem) obj).url);
            } else {
                equals = super.equals(obj);
            }
            return equals;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + url.hashCode();
            return hash;
        }
    }
}