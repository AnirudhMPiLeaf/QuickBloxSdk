
package com.quickblox.quickblox_sdk.users;

import android.os.Bundle;
import android.text.TextUtils;

import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.request.GenericQueryRule;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.quickblox_sdk.base.BaseModule;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
public class UsersModule implements BaseModule {
    private static final String CHANNEL_NAME = "FlutterQBUsersChannel";

    private static final String CREATE_METHOD = "create";
    private static final String GET_USERS_METHOD = "getUsers";
    private static final String GET_USERS_BY_TAG = "getUsersByTag";
    private static final String UPDATE_METHOD = "update";

    @Override
    public void initEventHandler() {
        //empty
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
            case CREATE_METHOD:
                create(data, result);
                break;
            case GET_USERS_METHOD:
                getUsers(data, result);
                break;
            case GET_USERS_BY_TAG:
                getUsersByTag(data, result);
                break;
            case UPDATE_METHOD:
                update(data, result);
                break;
        }
    }

    private void create(Map<String, Object> data, final MethodChannel.Result result) {
        String login = data != null && data.containsKey("login") ? (String) data.get("login") : null;
        String password = data != null && data.containsKey("password") ? (String) data.get("password") : null;
        String email = data != null && data.containsKey("email") ? (String) data.get("email") : null;
        Integer blobId = data != null && data.containsKey("blobId") ? (int) data.get("blobId") : null;
        Integer externalUserId = data != null && data.containsKey("externalUserId") ? (int) data.get("externalUserId") : null;
        Integer facebookId = data != null && data.containsKey("facebookId") ? (int) data.get("facebookId") : null;
        Integer twitterId = data != null && data.containsKey("twitterId") ? (int) data.get("twitterId") : null;
        String fullName = data != null && data.containsKey("fullName") ? (String) data.get("fullName") : null;
        String phone = data != null && data.containsKey("phone") ? (String) data.get("phone") : null;
        String webSite = data != null && data.containsKey("website") ? (String) data.get("website") : null;
        String customData = data != null && data.containsKey("customData") ? (String) data.get("customData") : null;
        String tagList = data != null && data.containsKey("tagList") ? (String) data.get("tagList") : null;

        if (TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
            result.error("Login and password are required parameters", null, null);
            return;
        }

        QBUser qbUser = new QBUser();
        qbUser.setLogin(login);
        qbUser.setPassword(password);

        if (!TextUtils.isEmpty(email)) {
            qbUser.setEmail(email);
        }
        if (blobId != null && blobId != 0) {
            qbUser.setFileId(blobId);
        }
        if (externalUserId != null && externalUserId != 0) {
            qbUser.setExternalId(String.valueOf(externalUserId));
        }
        if (facebookId != null && facebookId != 0) {
            qbUser.setFacebookId(String.valueOf(facebookId));
        }
        if (twitterId != null && twitterId != 0) {
            qbUser.setTwitterId(String.valueOf(twitterId));
        }
        if (!TextUtils.isEmpty(fullName)) {
            qbUser.setFullName(fullName);
        }
        if (!TextUtils.isEmpty(phone)) {
            qbUser.setPhone(phone);
        }
        if (!TextUtils.isEmpty(webSite)) {
            qbUser.setWebsite(webSite);
        }
        if (!TextUtils.isEmpty(customData)) {
            qbUser.setCustomData(customData);
        }
        if (!TextUtils.isEmpty(tagList)) {
            StringifyArrayList<String> list = new StringifyArrayList<>();
            String[] parts = tagList.split(",");
            list.addAll(Arrays.asList(parts));
            qbUser.setTags(list);
        }

        QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Map user = UsersMapper.qbUserToMap(qbUser);
                result.success(user);
            }

            @Override
            public void onError(QBResponseException e) {
                result.error(e.getMessage(), null, null);
            }
        });
    }

    private void getUsers(Map<String, Object> data, final MethodChannel.Result result) {
        Map sortMap = data != null && data.containsKey("sort") ? (Map) data.get("sort") : null;
        Map filterMap = data != null && data.containsKey("filter") ? (Map) data.get("filter") : null;
        int page = data != null && data.containsKey("page") ? (int) data.get("page") : 1;
        int perPage = data != null && data.containsKey("perPage") ? (int) data.get("perPage") : 100;

        String filter = UsersMapper.filterMapToFilter(filterMap);
        ArrayList filterValue = UsersMapper.getFilterValueFromMap(filterMap);
        String sort = UsersMapper.sortMapToSort(sortMap);

        ArrayList<GenericQueryRule> queryRules = new ArrayList<>();

        if (!TextUtils.isEmpty(sort)) {
            queryRules.add(new GenericQueryRule("order", sort));
        }

        QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
        requestBuilder.setRules(queryRules);
        requestBuilder.setPage(page);
        requestBuilder.setPerPage(perPage);

        if (!TextUtils.isEmpty(filter) && filterValue != null && filterValue.size() > 0) {
            QBUsers.getUsersByFilter(filterValue, filter, requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
                @Override
                public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("perPage", perPage);
                    payload.put("total", bundle.containsKey("total_entries") ? bundle.getInt("total_entries") : -1);
                    payload.put("page", page);

                    List<Map> usersList = new ArrayList<>();
                    for (QBUser qbUser : qbUsers) {
                        Map user = UsersMapper.qbUserToMap(qbUser);
                        usersList.add(user);
                    }
                    payload.put("users", usersList);

                    result.success(payload);
                }

                @Override
                public void onError(QBResponseException e) {
                    result.error(e.getMessage(), null, null);
                }
            });
        } else {
            QBUsers.getUsers(requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
                @Override
                public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("perPage", perPage);
                    payload.put("total", bundle.containsKey("total_entries") ? bundle.getInt("total_entries") : -1);
                    payload.put("page", page);

                    List<Map> usersList = new ArrayList<>();
                    for (QBUser qbUser : qbUsers) {
                        Map user = UsersMapper.qbUserToMap(qbUser);
                        usersList.add(user);
                    }
                    payload.put("users", usersList);

                    result.success(payload);
                }

                @Override
                public void onError(QBResponseException e) {
                    result.error(e.getMessage(), null, null);
                }
            });
        }
    }

    private void getUsersByTag(Map<String, Object> data, final MethodChannel.Result result) {
        List<String> tags = data != null && data.containsKey("tags") ? (List<String>) data.get("tags") : null;

        int page = data != null && data.containsKey("page") ? (int) data.get("page") : 1;
        int perPage = data != null && data.containsKey("perPage") ? (int) data.get("perPage") : 100;

        if (tags == null || tags.isEmpty()) {
            result.error("The tags won't be empty", null, null);
            return;
        }

        QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
        requestBuilder.setPage(page);
        requestBuilder.setPerPage(perPage);

        QBUsers.getUsersByTags(tags, requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                Map<String, Object> payload = new HashMap<>();
                payload.put("perPage", perPage);
                payload.put("total", bundle.containsKey("total_entries") ? bundle.getInt("total_entries") : -1);
                payload.put("page", page);

                List<Map> usersList = new ArrayList<>();
                for (QBUser qbUser : qbUsers) {
                    Map user = UsersMapper.qbUserToMap(qbUser);
                    usersList.add(user);
                }
                payload.put("users", usersList);

                result.success(payload);
            }

            @Override
            public void onError(QBResponseException e) {
                result.error(e.getMessage(), null, null);
            }
        });
    }

    private void update(Map<String, Object> data, final MethodChannel.Result result) {
        String login = data != null && data.containsKey("login") ? (String) data.get("login") : null;
        String newPassword = data != null && data.containsKey("newPassword") ? (String) data.get("newPassword") : null;
        String password = data != null && data.containsKey("password") ? (String) data.get("password") : null;
        String email = data != null && data.containsKey("email") ? (String) data.get("email") : null;
        Integer blobId = data != null && data.containsKey("blobId") ? (int) data.get("blobId") : null;
        Integer externalUserId = data != null && data.containsKey("externalUserId") ? (int) data.get("externalUserId") : null;
        Integer facebookId = data != null && data.containsKey("facebookId") ? (int) data.get("facebookId") : null;
        Integer twitterId = data != null && data.containsKey("twitterId") ? (int) data.get("twitterId") : null;
        String fullName = data != null && data.containsKey("fullName") ? (String) data.get("fullName") : null;
        String phone = data != null && data.containsKey("phone") ? (String) data.get("phone") : null;
        String webSite = data != null && data.containsKey("website") ? (String) data.get("website") : null;
        String customData = data != null && data.containsKey("customData") ? (String) data.get("customData") : null;
        String tagList = data != null && data.containsKey("tagList") ? (String) data.get("tagList") : null;

        QBUser qbUser = new QBUser();

        if (TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(password)) {
            result.error("If field newPassword did set, the password should set as well.", null, null);
        }
        if (TextUtils.isEmpty(password) && !TextUtils.isEmpty(newPassword)) {
            result.error("If field password did set, the newPassword should set as well.", null, null);
        }
        if (!TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(password)) {
            qbUser.setPassword(newPassword);
            qbUser.setOldPassword(password);
        }

        if (!TextUtils.isEmpty(login)) {
            qbUser.setLogin(login);
        }
        if (!TextUtils.isEmpty(email)) {
            qbUser.setEmail(email);
        }
        if (blobId != null && blobId != 0) {
            qbUser.setFileId(blobId);
        }
        if (externalUserId != null && externalUserId != 0) {
            qbUser.setExternalId(String.valueOf(externalUserId));
        }
        if (facebookId != null && facebookId != 0) {
            qbUser.setFacebookId(String.valueOf(facebookId));
        }
        if (twitterId != null && twitterId != 0) {
            qbUser.setTwitterId(String.valueOf(twitterId));
        }
        if (!TextUtils.isEmpty(fullName)) {
            qbUser.setFullName(fullName);
        }
        if (!TextUtils.isEmpty(phone)) {
            qbUser.setPhone(phone);
        }
        if (!TextUtils.isEmpty(webSite)) {
            qbUser.setWebsite(webSite);
        }
        if (!TextUtils.isEmpty(customData)) {
            qbUser.setCustomData(customData);
        }
        if (!TextUtils.isEmpty(tagList)) {
            StringifyArrayList<String> list = new StringifyArrayList<>();
            String[] parts = tagList.split(",");
            list.addAll(Arrays.asList(parts));
            qbUser.setTags(list);
        }

        if (QBSessionManager.getInstance().getActiveSession() == null) {
            result.error("The session doesn't exist", null, null);
            return;
        }

        int userId = QBSessionManager.getInstance().getActiveSession().getUserId();
        qbUser.setId(userId);

        QBUsers.updateUser(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Map user = UsersMapper.qbUserToMap(qbUser);
                result.success(user);
            }

            @Override
            public void onError(QBResponseException e) {
                result.error(e.getMessage(), null, null);
            }
        });
    }
}