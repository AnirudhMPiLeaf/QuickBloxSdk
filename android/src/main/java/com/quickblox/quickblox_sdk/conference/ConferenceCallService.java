package com.quickblox.quickblox_sdk.conference;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.conference.ConferenceClient;
import com.quickblox.conference.ConferenceSession;
import com.quickblox.conference.QBConferenceRole;
import com.quickblox.conference.WsException;
import com.quickblox.conference.callbacks.ConferenceEntityCallback;
import com.quickblox.quickblox_sdk.R;
import com.quickblox.quickblox_sdk.audio.QBAudioManager;
import com.quickblox.quickblox_sdk.event.EventHandler;
import com.quickblox.quickblox_sdk.utils.EventsUtil;
import com.quickblox.videochat.webrtc.QBRTCCameraVideoCapturer;
import com.quickblox.videochat.webrtc.QBRTCConfig;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;

import org.webrtc.CameraVideoCapturer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

import io.flutter.plugin.common.MethodChannel;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
public class ConferenceCallService extends Service {
    private static final String TAG = ConferenceCallService.class.getSimpleName();

    private static final int SERVICE_ID = 757;
    private static final String CHANNEL_ID = "Quickblox channel";
    private static final String CHANNEL_NAME = "Quickblox foreground service";

    private final ServiceBinder serviceBinder = new ServiceBinder();
    private QBAudioManager audioManager;

    private Context context;

    private final Set<SessionWrapper> sessionCache = new CopyOnWriteArraySet<>();

    private ConferenceClient conferenceClient;

    private SessionListenerImpl sessionListener;
    private SessionStateListenerImpl sessionStateListener;
    private VideoTrackListenerImpl videoTrackListener;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private CallTimerTask callTimerTask = new CallTimerTask();
    private Timer callTimer = new Timer();

    public static void start(Context context) {
        Intent intent = new Intent(context, ConferenceCallService.class);
        context.startService(intent);
    }

    public static void stop(Context context) {
        Intent intent = new Intent(context, ConferenceCallService.class);
        context.stopService(intent);
    }

    public static boolean isRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        boolean running = false;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ConferenceCallService.class.getName().equals(service.service.getClassName())) {
                running = true;
                break;
            }
        }
        return running;
    }

    @Override
    public void onCreate() {
        context = getApplicationContext();

        initConferenceClient();
        addSessionListeners();
        addSessionStateListeners();
        addVideoTrackListeners();
        initAudioManager();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String notificationTitle = getString(R.string.conference_call_service_notification_title);
        String notificationText = getString(R.string.conference_call_service_notification_text);

        Notification notification = buildNotification(notificationTitle, notificationText);
        startForeground(SERVICE_ID, notification);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeSessionListeners();
        removeSessionStateListeners();
        removeVideoTrackListeners();
        releaseAudioManager();
        stopCallTimer();
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    private Notification buildNotification(String notificationTitle, String notificationText) {
        Class moduleClass = getActivityClass();
        Intent notifyIntent = new Intent(this, moduleClass);
        notifyIntent.setAction(Intent.ACTION_MAIN);
        notifyIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        int intentFlag = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intentFlag = PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0,
                notifyIntent, intentFlag);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(notificationTitle);
        bigTextStyle.bigText(notificationText);

        String channelID = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                createNotificationChannel(CHANNEL_ID, CHANNEL_NAME)
                : getString(R.string.conference);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID);
        builder.setStyle(bigTextStyle);
        builder.setContentTitle(notificationTitle);
        builder.setContentText(notificationText);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_call_service_small);

        Bitmap bitmapIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_call_service_large);
        builder.setLargeIcon(bitmapIcon);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setPriority(NotificationManager.IMPORTANCE_LOW);
        } else {
            builder.setPriority(Notification.PRIORITY_LOW);
        }
        builder.setContentIntent(notifyPendingIntent);

        return builder.build();
    }

    private void updateNotification(String notificationTitle, String notificationText) {
        Notification notification = buildNotification(notificationTitle, notificationText);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(SERVICE_ID, notification);
    }

    private Class getActivityClass() {
        String packageName = context.getPackageName();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        String className = launchIntent.getComponent().getClassName();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelID, String channelName) {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_LOW);
        channel.setLightColor(getColor(R.color.primary));
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        return channelID;
    }

    private void initConferenceClient() {
        conferenceClient = ConferenceClient.getInstance(this);
        conferenceClient.setCameraErrorHandler(new CameraEventsListener());
        QBRTCConfig.setDebugEnabled(true);
    }

    // add/remove listeners
    private void addSessionStateListeners() {
        sessionStateListener = new SessionStateListenerImpl();
        for (SessionWrapper session : sessionCache) {
            session.addSessionStateListener(sessionStateListener);
        }
    }

    private void removeSessionStateListeners() {
        sessionStateListener = null;
        for (SessionWrapper session : sessionCache) {
            session.removeSessionStateListener();
        }
    }

    private void addVideoTrackListeners() {
        videoTrackListener = new VideoTrackListenerImpl();
        for (SessionWrapper session : sessionCache) {
            session.addVideoTrackListener(videoTrackListener);
        }
    }

    private void removeVideoTrackListeners() {
        videoTrackListener = null;
        for (SessionWrapper session : sessionCache) {
            session.removeVideoTrackListener();
        }
    }

    private void addSessionListeners() {
        sessionListener = new SessionListenerImpl();
        for (SessionWrapper session : sessionCache) {
            session.addSessionListener(sessionListener);
        }
    }

    private void removeSessionListeners() {
        sessionListener = null;
        for (SessionWrapper session : sessionCache) {
            session.removeSessionListener();
        }
    }

    private void initAudioManager() {
        Log.e(TAG + "_AUDIO_MANAGER", "initAudioManager: try to init...");
        if (audioManager != null) {
            Log.e(TAG + "_AUDIO_MANAGER", "initAudioManager: the appRTCAudioManger != null, try to release");
            releaseAudioManager();
        }

        audioManager = QBAudioManager.create(this);
        audioManager.setManageSpeakerPhoneByProximity(false);

        audioManager.setOnWiredHeadsetStateListener(new QBAudioManager.OnWiredHeadsetStateListener() {
            @Override
            public void onWiredHeadsetStateChanged(boolean plugged, boolean hasMicrophone) {
            }
        });

        audioManager.setBluetoothAudioDeviceStateListener(new QBAudioManager.BluetoothAudioDeviceStateListener() {
            @Override
            public void onStateChanged(boolean connected) {
            }
        });

        audioManager.start(new QBAudioManager.AudioManagerEvents() {
            @Override
            public void onAudioDeviceChanged(QBAudioManager.AudioDevice audioDevice, Set<QBAudioManager.AudioDevice> set) {
            }
        });
        Log.e(TAG + "_AUDIO_MANAGER", "initAudioManager: success init");
    }

    public void setAudioEnabled(boolean enabled, int userId, String sessionId, ServiceCallback<Void> serviceCallback) {
        SessionWrapper sessionWrapper = getSessionFromCache(sessionId);

        if (sessionWrapper == null) {
            serviceCallback.onError("The session with id: " + sessionId + " has not found");
            return;
        }

        if (userId > 0 && sessionWrapper.getConferenceSession().getMediaStreamManager().getAudioTrack(userId) != null) {
            //set enabled track by user id
            sessionWrapper.getConferenceSession().getMediaStreamManager().getAudioTrack(userId).setEnabled(enabled);
        } else {
            //if userId = -1, then set enabled local track
            sessionWrapper.getConferenceSession().getMediaStreamManager().getLocalAudioTrack().setEnabled(enabled);
        }
        serviceCallback.onSuccess(null);
    }

    public void setVideoEnabled(boolean enabled, int userId, String sessionId, ServiceCallback<Void> serviceCallback) {
        SessionWrapper sessionWrapper = getSessionFromCache(sessionId);

        if (sessionWrapper == null) {
            serviceCallback.onError("The session with id: " + sessionId + " has not found");
            return;
        }

        if (userId > 0 && sessionWrapper.getConferenceSession().getMediaStreamManager().getVideoTrack(userId) != null) {
            //set enabled track by user id
            sessionWrapper.getConferenceSession().getMediaStreamManager().getVideoTrack(userId).setEnabled(enabled);
        } else {
            //if userId = -1, then set enabled local track
            sessionWrapper.getConferenceSession().getMediaStreamManager().getLocalVideoTrack().setEnabled(enabled);
        }
        serviceCallback.onSuccess(null);
    }

    public void switchAudioOutput(final int value, final MethodChannel.Result result) {
        Handler mainHandler = new Handler(context.getMainLooper());
        Runnable mainThreadRunnable = () -> {
            if (audioManager == null) {
                Log.e(TAG + "_AUDIO_MANAGER", "switchAudioOutput: error switch webRTCAudioManager is null");
                return;
            }

            QBAudioManager.AudioDevice audioDevice = audioManager.appRTCAudioDeviceFromValue(value);

            if (audioManager.getAudioDevices().contains(audioDevice)) {
                audioManager.selectAudioDevice(audioDevice);
                if (result != null) {
                    result.success(null);
                }
                Log.e(TAG + "_AUDIO_MANAGER", "switchAudioOutput: success switched to " + audioDevice);
            } else {
                if (result != null) {
                    String deviceName = audioManager.getDeviceNameFromValue(value);
                    if (TextUtils.isEmpty(deviceName)) {
                        deviceName = String.valueOf(value);
                    }
                    result.error("Switch Device of type " + deviceName + " is not found", null, null);
                    Log.e(TAG + "_AUDIO_MANAGER", "switchAudioOutput: error, the device didn't fount " + audioDevice);
                }
            }
        };

        //Some devices like Huawei requires the delay before change audio output
        mainHandler.postDelayed(mainThreadRunnable, 500);
    }

    public void releaseAudioManager() {
        Log.e(TAG + "_AUDIO_MANAGER", "releaseAudioManager: try to release...");
        if (audioManager != null) {
            audioManager.stop();
            audioManager = null;
            Log.e(TAG + "_AUDIO_MANAGER", "releaseAudioManager: the appRTCAudioManger is success released");
        } else {
            Log.e(TAG + "_AUDIO_MANAGER", "releaseAudioManager: the appRTCAudioManger == null (not initialized), not need to release");
        }
    }

    public void switchCamera(String sessionId, CameraVideoCapturer.CameraSwitchHandler cameraSwitchHandler, ServiceCallback<Void> serviceCallback) {
        SessionWrapper session = getSessionFromCache(sessionId);
        if (session != null) {
            QBRTCCameraVideoCapturer videoCapturer = (QBRTCCameraVideoCapturer) session.getConferenceSession().getMediaStreamManager().getVideoCapturer();
            videoCapturer.switchCamera(cameraSwitchHandler);
            serviceCallback.onSuccess(null);
        } else {
            serviceCallback.onError("The session with id: " + sessionId + " has not found");
        }
    }

    private SessionWrapper getSessionFromCache(String sessionId) {
        SessionWrapper sessionWrapper = null;
        if (sessionCache.contains(new SessionWrapper(sessionId, null))) {
            for (SessionWrapper item : sessionCache) {
                if (item.equals(new SessionWrapper(sessionId, null))) {
                    sessionWrapper = item;
                    break;
                }
            }
        }
        return sessionWrapper;
    }

    public void startCallTimer() {
        if (callTimerTask != null) {
            callTimerTask.cancel();
        }
        if (callTimer != null) {
            callTimer = new Timer();
        }
        callTimerTask = new CallTimerTask();
        callTimer.scheduleAtFixedRate(callTimerTask, 0, 1000L);
    }

    private void stopCallTimer() {
        if (callTimerTask != null) {
            callTimerTask.cancel();
        }
        if (callTimer != null) {
            callTimer.cancel();
            callTimer.purge();
        }
    }

    public void startConference(final String roomId, final QBRTCTypes.QBConferenceType conferenceType, final ServiceCallback<SessionWrapper> callback) {
        int userId = QBSessionManager.getInstance().getActiveSession().getUserId();

        conferenceClient.createSession(userId, conferenceType, new ConferenceEntityCallback<ConferenceSession>() {
            @Override
            public void onSuccess(ConferenceSession conferenceSession) {
                conferenceSession.setDialogId(roomId);

                String id = UUID.randomUUID().toString().toLowerCase();

                SessionWrapper session = new SessionWrapper(id, conferenceSession);

                session.addSessionListener(sessionListener);
                session.addSessionStateListener(sessionStateListener);
                session.addVideoTrackListener(videoTrackListener);

                sessionCache.add(session);

                callback.onSuccess(session);
            }

            @Override
            public void onError(WsException e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void joinAsPublisher(final String sessionId, final ServiceCallback<List<Integer>> callback) {
        SessionWrapper session = getSessionFromCache(sessionId);
        if (session == null) {
            callback.onError("The session with id " + sessionId + " has not found");
            return;
        }

        String roomId = session.getConferenceSession().getDialogID();
        QBConferenceRole role = QBConferenceRole.PUBLISHER;

        session.getConferenceSession().joinDialog(roomId, role, new ConferenceEntityCallback<ArrayList<Integer>>() {
            @Override
            public void onSuccess(ArrayList<Integer> publishers) {
                callback.onSuccess(publishers);
            }

            @Override
            public void onError(WsException e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void getOnlineParticipants(final String sessionId, final ServiceCallback<List<Integer>> callback) {
        SessionWrapper session = getSessionFromCache(sessionId);
        if (session == null) {
            callback.onError("The session with id " + sessionId + " has not found");
            return;
        }

        session.getConferenceSession().getOnlineParticipants(new ConferenceEntityCallback<Map<Integer, Boolean>>() {
            @Override
            public void onSuccess(Map<Integer, Boolean> participantsMap) {
                List<Integer> participants = new ArrayList<>(participantsMap.keySet());
                callback.onSuccess(participants);
            }

            @Override
            public void onError(WsException e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void leave(String sessionId, final ServiceCallback<Void> callback) {
        SessionWrapper session = getSessionFromCache(sessionId);
        if (session == null) {
            callback.onError("The session with id " + sessionId + " has not found");
            return;
        }

        session.getConferenceSession().leave();

        releaseSession(session);

        callback.onSuccess(null);
    }

    public void getVideoTrack(String sessionId, Integer userId, ServiceCallback<QBRTCVideoTrack> serviceCallback) {
        SessionWrapper session = getSessionFromCache(sessionId);

        if (session == null || session.getConferenceSession() == null || session.getConferenceSession().getMediaStreamManager() == null) {
            serviceCallback.onError("The session with id: " + sessionId + " has not found");
            return;
        }

        int currentUserId = QBSessionManager.getInstance().getActiveSession().getUserId();
        QBRTCVideoTrack videoTrack;
        if (userId == currentUserId) {
            videoTrack = session.getConferenceSession().getMediaStreamManager().getLocalVideoTrack();
        } else {
            videoTrack = session.getConferenceSession().getMediaStreamManager().getVideoTrack(userId);
        }

        if (videoTrack != null) {
            serviceCallback.onSuccess(videoTrack);
        } else {
            serviceCallback.onError("The video track for user" + userId + " has not found");
        }
    }

    private void releaseSession(SessionWrapper session) {
        session.removeSessionListener();
        session.removeVideoTrackListener();
        session.removeSessionStateListener();

        sessionCache.remove(session);
    }

    public void subscribeToParticipant(String sessionId, Integer userId, ServiceCallback<Void> serviceCallback) {
        SessionWrapper session = getSessionFromCache(sessionId);
        if (session == null) {
            serviceCallback.onError("The session with id " + sessionId + " has not found");
            return;
        }

        session.getConferenceSession().subscribeToPublisher(userId);
        serviceCallback.onSuccess(null);
    }

    public void unsubscribeToParticipant(String sessionId, Integer userId, ServiceCallback<Void> serviceCallback) {
        SessionWrapper session = getSessionFromCache(sessionId);
        if (session == null) {
            serviceCallback.onError("The session with id " + sessionId + " has not found");
            return;
        }

        session.getConferenceSession().unsubscribeFromPublisher(userId);
        serviceCallback.onSuccess(null);
    }

    private class CallTimerTask extends TimerTask {
        private Long callTime;

        CallTimerTask() {
            callTime = 1000L;
        }

        @Override
        public void run() {
            callTime = callTime + 1000L;

            String callTime = getCallTime();

            String notificationTitle = getString(R.string.notification_title_call);
            String notificationText = getString(R.string.notification_text_call, callTime);

            updateNotification(notificationTitle, notificationText);
        }

        private String getCallTime() {
            String time = "";
            if (callTime != null) {
                String format = String.format(context.getString(R.string.call_time_format), 2);
                long elapsedTime = callTime / 1000;
                String seconds = String.format(format, elapsedTime % 60);
                String minutes = String.format(format, elapsedTime % 3600 / 60);
                String hours = String.format(format, elapsedTime / 3600);

                time = minutes + ":" + seconds;
                if (!TextUtils.isEmpty(hours) && hours != "00") {
                    time = hours + ":" + minutes + ":" + seconds;
                }
            }
            return time;
        }
    }

    class ServiceBinder extends Binder {
        ConferenceCallService getService() {
            return ConferenceCallService.this;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // CAMERA EVENTS LISTENER
    ///////////////////////////////////////////////////////////////////////////
    private static class CameraEventsListener implements CameraVideoCapturer.CameraEventsHandler {
        @Override
        public void onCameraError(String message) {
            //void
        }

        @Override
        public void onCameraDisconnected() {
            //void
        }

        @Override
        public void onCameraFreezed(String message) {
            //void
        }

        @Override
        public void onCameraOpening(String message) {
            //void
        }

        @Override
        public void onFirstFrameAvailable() {
            //void
        }

        @Override
        public void onCameraClosed() {
            //void
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // SESSION STATE LISTENER
    ///////////////////////////////////////////////////////////////////////////
    private class SessionStateListenerImpl implements SessionWrapper.SessionStateListener {
        @Override
        public void onChanged(String sessionId, int state) {
            String eventName = ConferenceConstants.Events.CONFERENCE_STATE_CHANGED;

            Map<String, Object> payload = new HashMap<>();

            payload.put("sessionId", sessionId);
            payload.put("state", state);

            Map<String, Object> eventData = EventsUtil.buildPayload(eventName, payload);

            mainHandler.post(() -> EventHandler.sendEvent(eventName, eventData));
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // SESSION LISTENER
    ///////////////////////////////////////////////////////////////////////////
    private class SessionListenerImpl implements SessionWrapper.SessionListener {
        @Override
        public void onPublisherReceived(String sessionId, Integer userId) {
            String eventName = ConferenceConstants.Events.CONFERENCE_PARTICIPANT_RECEIVED;

            Map<String, Object> payload = new HashMap<>();

            payload.put("sessionId", sessionId);
            payload.put("userId", userId);

            Map<String, Object> eventData = EventsUtil.buildPayload(eventName, payload);

            mainHandler.post(() -> EventHandler.sendEvent(eventName, eventData));
        }

        @Override
        public void onPublisherLeft(String sessionId, Integer userId) {
            String eventName = ConferenceConstants.Events.CONFERENCE_PARTICIPANT_LEFT;

            Map<String, Object> payload = new HashMap<>();

            payload.put("sessionId", sessionId);
            payload.put("userId", userId);

            Map<String, Object> eventData = EventsUtil.buildPayload(eventName, payload);

            mainHandler.post(() -> EventHandler.sendEvent(eventName, eventData));
        }

        @Override
        public void onError(String sessionId, String errorMessage) {
            String eventName = ConferenceConstants.Events.CONFERENCE_ERROR_RECEIVED;

            Map<String, Object> payload = new HashMap<>();

            payload.put("sessionId", sessionId);
            payload.put("errorMessage", errorMessage);

            Map<String, Object> eventData = EventsUtil.buildPayload(eventName, payload);

            mainHandler.post(() -> EventHandler.sendEvent(eventName, eventData));
        }

        @Override
        public void onClosed(String sessionId) {
            SessionWrapper session = getSessionFromCache(sessionId);
            if (session != null) {
                releaseSession(session);
            }

            String eventName = ConferenceConstants.Events.CONFERENCE_CLOSED;

            Map<String, Object> payload = new HashMap<>();

            payload.put("sessionId", sessionId);

            Map<String, Object> eventData = EventsUtil.buildPayload(eventName, payload);

            mainHandler.post(() -> EventHandler.sendEvent(eventName, eventData));
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // VIDEO TRACK LISTENER
    ///////////////////////////////////////////////////////////////////////////
    private class VideoTrackListenerImpl implements SessionWrapper.VideoTrackListener {
        @Override
        public void onReceive(String sessionId, Integer userId, boolean enabled) {
            String eventName = ConferenceConstants.Events.CONFERENCE_VIDEO_TRACK_RECEIVED;

            Map<String, Object> payload = new HashMap<>();

            payload.put("userId", userId);
            payload.put("sessionId", sessionId);
            payload.put("enabled", enabled);

            Map<String, Object> eventData = EventsUtil.buildPayload(eventName, payload);

            mainHandler.post(() -> EventHandler.sendEvent(eventName, eventData));
        }
    }

    interface ServiceCallback<T> {
        void onSuccess(T value);

        void onError(String errorMessage);
    }
}