package com.quickblox.quickblox_sdk.conference;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;

import com.quickblox.conference.ConferenceConfig;
import com.quickblox.quickblox_sdk.base.BaseModule;
import com.quickblox.quickblox_sdk.event.EventHandler;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import org.webrtc.CameraVideoCapturer;

import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * Created by Injoit on 1/28/21.
 * Copyright © 2020 Quickblox. All rights reserved.
 */
public class ConferenceModule implements BaseModule {
    static final String CHANNEL_NAME = "FlutterQBConferenceChannel";

    private static final String INIT_METHOD = "init";
    private static final String RELEASE_METHOD = "release";
    private static final String CREATE_METHOD = "create";
    private static final String JOIN_AS_PUBLISHER_METHOD = "joinAsPublisher";
    private static final String GET_ONLINE_PARTICIPANTS_METHOD = "getOnlineParticipants";
    private static final String SUBSCRIBE_TO_PARTICIPANT_METHOD = "subscribeToParticipant";
    private static final String UNSUBSCRIBE_FROM_PARTICIPANT_METHOD = "unsubscribeFromParticipant";
    private static final String LEAVE_METHOD = "leave";
    private static final String ENABLE_VIDEO_METHOD = "enableVideo";
    private static final String ENABLE_AUDIO_METHOD = "enableAudio";
    private static final String SWITCH_CAMERA_METHOD = "switchCamera";
    private static final String SWITCH_AUDIO_OUTPUT = "switchAudioOutput";

    private BinaryMessenger binaryMessenger;
    private Context context;

    private ConferenceCallService callService;
    private ServiceConnection serviceConnection;

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public ConferenceModule(BinaryMessenger binaryMessenger, Context reactContext) {
        this.binaryMessenger = binaryMessenger;
        this.context = reactContext;

        initEventHandler();
    }

    @Override
    public void initEventHandler() {
        EventHandler.init(ConferenceConstants.getAllEvents(), binaryMessenger);
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
        switch (methodCall.method) {
            case INIT_METHOD:
                init(methodCall.arguments(), result);
                break;
            case CREATE_METHOD:
                create((String) (((List) methodCall.arguments()).get(0)),
                        (Integer) ((List) methodCall.arguments()).get(1),
                        result);
                break;
            case JOIN_AS_PUBLISHER_METHOD:
                joinAsPublisher(methodCall.arguments(), result);
                break;
            case GET_ONLINE_PARTICIPANTS_METHOD:
                getOnlineParticipants(methodCall.arguments(), result);
                break;
            case SUBSCRIBE_TO_PARTICIPANT_METHOD:
                subscribeToParticipant((Integer) (((List) methodCall.arguments()).get(0)),
                        (String) ((List) methodCall.arguments()).get(1),
                        result);
                break;
            case UNSUBSCRIBE_FROM_PARTICIPANT_METHOD:
                unsubscribeFromParticipant((Integer) (((List) methodCall.arguments()).get(0)),
                        (String) ((List) methodCall.arguments()).get(1),
                        result);
                break;
            case LEAVE_METHOD:
                leave(methodCall.arguments(), result);
                break;
            case RELEASE_METHOD:
                release(result);
                break;
            case ENABLE_VIDEO_METHOD:
                enableVideo((Map) methodCall.arguments, result);
                break;
            case SWITCH_CAMERA_METHOD:
                switchCamera((Map) methodCall.arguments, result);
                break;
            case ENABLE_AUDIO_METHOD:
                enableAudio((Map) methodCall.arguments, result);
                break;
            case SWITCH_AUDIO_OUTPUT:
                switchAudioOutput((Map) methodCall.arguments, result);
                break;
        }
    }

    private void init(final String endpoint, final MethodChannel.Result result) {
        if (TextUtils.isEmpty(endpoint)) {
            result.error("The endpoint has a wrong value", null, null);
            return;
        }
        ConferenceConfig.setUrl(endpoint);
        unbindService();
        ConferenceCallService.start(context);
        bindService(CallServiceConnection.CONNECTED_ACTION, result);
    }

    private void create(final String roomId, final Integer sessionType, final MethodChannel.Result result) {
        if (TextUtils.isEmpty(roomId)) {
            result.error("The roomId has a wrong value", null, null);
            return;
        }
        if (sessionType == null) {
            result.error("The session type has a wrong value", null, null);
            return;
        }
        if (callService == null) {
            result.error("The service has not inited", null, null);
            return;
        }

        QBRTCTypes.QBConferenceType conferenceType = ConferenceConstants.getSessionType(sessionType);

        callService.startConference(roomId, conferenceType, new ConferenceCallService.ServiceCallback<SessionWrapper>() {
            @Override
            public void onSuccess(SessionWrapper session) {
                mainHandler.post(() -> {
                    Map sessionMap = ConferenceMapper.qbConferenceSessionToMap(session);
                    result.success(sessionMap);
                });
            }

            @Override
            public void onError(String errorMessage) {
                mainHandler.post(() -> result.error(errorMessage, null, null));
            }
        });
    }

    private void joinAsPublisher(final String id, final MethodChannel.Result result) {
        if (TextUtils.isEmpty(id)) {
            result.error("The session type has a wrong value", null, null);
            return;
        }
        if (callService == null) {
            result.error("The service has not inited", null, null);
            return;
        }

        callService.joinAsPublisher(id, new ConferenceCallService.ServiceCallback<List<Integer>>() {
            @Override
            public void onSuccess(List<Integer> publishers) {
                mainHandler.post(() -> result.success(publishers));
            }

            @Override
            public void onError(String errorMessage) {
                mainHandler.post(() -> result.error(errorMessage, null, null));
            }
        });
    }

    private void getOnlineParticipants(final String id, final MethodChannel.Result result) {
        if (TextUtils.isEmpty(id)) {
            result.error("The session type has a wrong value", null, null);
            return;
        }
        if (callService == null) {
            result.error("The service has not inited", null, null);
            return;
        }

        callService.getOnlineParticipants(id, new ConferenceCallService.ServiceCallback<List<Integer>>() {
            @Override
            public void onSuccess(List<Integer> participants) {
                mainHandler.post(() -> result.success(participants));
            }

            @Override
            public void onError(String errorMessage) {
                mainHandler.post(() -> result.error(errorMessage, null, null));
            }
        });
    }

    private void subscribeToParticipant(final Integer userId, final String sessionId, final MethodChannel.Result result) {
        if (TextUtils.isEmpty(sessionId)) {
            result.error("The session type has a wrong value", null, null);
            return;
        }
        if (userId == null || userId <= 0) {
            result.error("The user id type has a wrong value", null, null);
            return;
        }
        if (callService == null) {
            result.error("The service has not inited", null, null);
            return;
        }

        callService.subscribeToParticipant(sessionId, userId, new ConferenceCallService.ServiceCallback<Void>() {
            @Override
            public void onSuccess(Void value) {
                result.success(null);
            }

            @Override
            public void onError(String errorMessage) {
                result.error(errorMessage, null, null);
            }
        });
    }

    private void unsubscribeFromParticipant(final Integer userId, final String sessionId, final MethodChannel.Result result) {
        if (TextUtils.isEmpty(sessionId)) {
            result.error("The session type has a wrong value", null, null);
            return;
        }
        if (userId == null || userId <= 0) {
            result.error("The user id type has a wrong value", null, null);
            return;
        }
        if (callService == null) {
            result.error("The service has not inited", null, null);
            return;
        }

        callService.unsubscribeToParticipant(sessionId, userId, new ConferenceCallService.ServiceCallback<Void>() {
            @Override
            public void onSuccess(Void value) {
                result.success(null);
            }

            @Override
            public void onError(String errorMessage) {
                result.error(errorMessage, null, null);
            }
        });
    }

    private void leave(final String id, final MethodChannel.Result result) {
        if (TextUtils.isEmpty(id)) {
            result.error("The session type has a wrong value", null, null);
            return;
        }
        if (callService == null) {
            result.error("The service has not inited", null, null);
            return;
        }

        callService.leave(id, new ConferenceCallService.ServiceCallback<Void>() {
            @Override
            public void onSuccess(Void value) {
                mainHandler.post(() -> result.success(null));
            }

            @Override
            public void onError(String errorMessage) {
                mainHandler.post(() -> result.error(errorMessage, null, null));
            }
        });
    }

    private void release(final MethodChannel.Result result) {
        unbindService();
        ConferenceCallService.stop(context);
        result.success(null);
    }

    private void enableVideo(Map data, final MethodChannel.Result result) {
        String sessionId = data != null && data.containsKey("sessionId") ? (String) data.get("sessionId") : null;
        boolean enabled = (data != null && data.containsKey("enable")) && (boolean) data.get("enable");
        Double userId = data != null && data.containsKey("userId") ? (double) data.get("userId") : null;

        if (TextUtils.isEmpty(sessionId)) {
            result.error("The id is required parameter", null, null);
            return;
        }

        if (callService == null) {
            result.error("The call service is not connected", null, null);
            return;
        }

        callService.setVideoEnabled(enabled, userId != null && userId > 0 ? userId.intValue() : -1,
                sessionId, new ConferenceCallService.ServiceCallback<Void>() {
                    @Override
                    public void onSuccess(Void value) {
                        result.success(null);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        result.error(errorMessage, null, null);
                    }
                });
    }

    private void switchCamera(Map data, final MethodChannel.Result result) {
        String sessionId = data != null && data.containsKey("sessionId") ? (String) data.get("sessionId") : null;

        if (TextUtils.isEmpty(sessionId)) {
            result.error("The id is required parameter", null, null);
            return;
        }

        if (callService == null) {
            result.error("The call service is not connected", null, null);
            return;
        }

        callService.switchCamera(sessionId, new CameraVideoCapturer.CameraSwitchHandler() {
            @Override
            public void onCameraSwitchDone(boolean frontCamera) {
                result.success(null);
            }

            @Override
            public void onCameraSwitchError(String errorMessage) {
                result.error(errorMessage, null, null);
            }
        }, new ConferenceCallService.ServiceCallback<Void>() {
            @Override
            public void onSuccess(Void value) {
                //ignore
            }

            @Override
            public void onError(String errorMessage) {
                result.error(errorMessage, null, null);
            }
        });
    }

    private void enableAudio(Map data, final MethodChannel.Result result) {
        String sessionId = data != null && data.containsKey("sessionId") ? (String) data.get("sessionId") : null;
        boolean enabled = (data != null && data.containsKey("enable")) && (boolean) data.get("enable");
        Double userId = data != null && data.containsKey("userId") ? (double) data.get("userId") : null;

        if (TextUtils.isEmpty(sessionId)) {
            result.error("The id is required parameter", null, null);
            return;
        }
        if (callService == null) {
            result.error("The call service is not connected", null, null);
            return;
        }

        callService.setAudioEnabled(enabled, userId != null && userId > 0 ? userId.intValue() : -1,
                sessionId, new ConferenceCallService.ServiceCallback<Void>() {
                    @Override
                    public void onSuccess(Void value) {
                        result.success(null);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        result.error(errorMessage, null, null);
                    }
                });
    }

    void switchAudioOutput(Map data, final MethodChannel.Result result) {
        Integer audioDevice = data != null && data.containsKey("output") ? (Integer) data.get("output") : null;

        if (audioDevice == null) {
            result.error("The output is required parameter", null, null);
            return;
        }

        if (callService == null) {
            result.error("The call service is not connected", null, null);
        }

        callService.switchAudioOutput(audioDevice, result);
    }

    private void bindService(int action, MethodChannel.Result result) {
        if (serviceConnection == null) {
            serviceConnection = new CallServiceConnection();
        }

        ((CallServiceConnection) serviceConnection).setAction(action);
        ((CallServiceConnection) serviceConnection).setPromise(result);

        Intent intent = new Intent(context, ConferenceCallService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService() {
        if (serviceConnection != null) {
            context.unbindService(serviceConnection);
            serviceConnection = null;
        }
    }

    private class CallServiceConnection implements ServiceConnection {
        private static final int CONNECTED_ACTION = 1;

        private int action = 0;
        private MethodChannel.Result result;

        void setAction(int action) {
            this.action = action;
        }

        void setPromise(MethodChannel.Result result) {
            this.result = result;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            callService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ConferenceCallService.ServiceBinder binder = (ConferenceCallService.ServiceBinder) service;

            callService = binder.getService();

            if (action == CONNECTED_ACTION && result != null) {
                result.success(null);
            }
        }
    }
}