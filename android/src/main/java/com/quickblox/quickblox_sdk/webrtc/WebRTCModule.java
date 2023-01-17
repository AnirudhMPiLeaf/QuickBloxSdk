package com.quickblox.quickblox_sdk.webrtc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;

import com.quickblox.chat.QBChatService;
import com.quickblox.quickblox_sdk.base.BaseModule;
import com.quickblox.quickblox_sdk.event.EventHandler;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import org.webrtc.CameraVideoCapturer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
public class WebRTCModule implements BaseModule {
    static final String CHANNEL_NAME = "FlutterQBWebRTCChannel";

    private static final String INIT_METHOD = "init";
    private static final String RELEASE_METHOD = "release";
    private static final String GET_SESSION_METHOD = "getSession";
    private static final String CALL_METHOD = "call";
    private static final String ACCEPT_METHOD = "accept";
    private static final String REJECT_METHOD = "reject";
    private static final String HANGUP_METHOD = "hangUp";
    private static final String ENABLE_VIDEO_METHOD = "enableVideo";
    private static final String ENABLE_AUDIO_METHOD = "enableAudio";
    private static final String SWITCH_CAMERA_METHOD = "switchCamera";
    private static final String SWITCH_AUDIO_OUTPUT = "switchAudioOutput";

    private BinaryMessenger binaryMessenger;
    private Context context;

    private WebRTCCallService webRTCCallService;
    private ServiceConnection callServiceConnection;

    public WebRTCModule(BinaryMessenger binaryMessenger, Context context) {
        this.binaryMessenger = binaryMessenger;
        this.context = context;

        initEventHandler();
    }

    @Override
    public void initEventHandler() {
        EventHandler.init(WebRTCConstants.getAllEvents(), binaryMessenger);
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
            case INIT_METHOD:
                init(data, result);
                break;
            case RELEASE_METHOD:
                release(data, result);
                break;
            case GET_SESSION_METHOD:
                getSession(data, result);
                break;
            case CALL_METHOD:
                call(data, result);
                break;
            case ACCEPT_METHOD:
                accept(data, result);
                break;
            case REJECT_METHOD:
                reject(data, result);
                break;
            case HANGUP_METHOD:
                hangUp(data, result);
                break;
            case ENABLE_VIDEO_METHOD:
                enableVideo(data, result);
                break;
            case ENABLE_AUDIO_METHOD:
                enableAudio(data, result);
                break;
            case SWITCH_CAMERA_METHOD:
                switchCamera(data, result);
                break;
            case SWITCH_AUDIO_OUTPUT:
                switchAudioOutput(data, result);
                break;
        }
    }

    private void init(Map<String, Object> data, final MethodChannel.Result result) {
        if (QBChatService.getInstance().isLoggedIn()) {
            unbindCallService();
            WebRTCCallService.start(context);
            bindCallService(CallServiceConnection.CONNECTED_ACTION, result);
        } else {
            result.error("The chat is not connected", null, null);
        }
    }

    private void release(Map<String, Object> data, final MethodChannel.Result result) {
        unbindCallService();
        WebRTCCallService.stop(context);
        result.success(null);
    }

    private void getSession(Map data, final MethodChannel.Result result) {
        String sessionId = data != null && data.containsKey("sessionId") ? (String) data.get("sessionId") : null;

        if (TextUtils.isEmpty(sessionId)) {
            result.error("The sessionId is required parameter", null, null);
            return;
        }

        if (webRTCCallService == null) {
            result.error("The call service is not connected", null, null);
            return;
        }

        webRTCCallService.getSession(sessionId, new WebRTCCallService.ServiceCallback<QBRTCSession>() {
            @Override
            public void onSuccess(QBRTCSession qbrtcSession) {
                Map sessionMap = WebRTCMapper.qBRTCSessionToMap(qbrtcSession);
                result.success(sessionMap);
            }

            @Override
            public void onError(String errorMessage) {
                result.error(errorMessage, null, null);
            }
        });
    }

    private void call(Map data, final MethodChannel.Result result) {
        List<Integer> opponentsIds = data != null && data.containsKey("opponentsIds") ? (List<Integer>) data.get("opponentsIds") : null;
        Integer sessionType = data != null && data.containsKey("type") ? (int) data.get("type") : null;
        Map<String, Object> userInfo = data != null && data.containsKey("userInfo") ? (Map) data.get("userInfo") : null;

        if (opponentsIds == null || opponentsIds.size() <= 0 || sessionType == null || sessionType <= 0) {
            result.error("The opponentsIds, type are required parameters", null, null);
            return;
        }

        if (webRTCCallService == null) {
            result.error("The call service is not connected", null, null);
            return;
        }

        QBRTCTypes.QBConferenceType conferenceType = WebRTCConstants.getSessionType(sessionType);

        Map<String, String> userInfoMap = new HashMap<>();
        if (userInfo != null && userInfo.size() > 0) {
            for (Map.Entry<String, Object> entry : userInfo.entrySet()) {
                String key = entry.getKey();
                String value = (String) entry.getValue();
                userInfoMap.put(key, value);
            }
        }

        webRTCCallService.startCall(opponentsIds, conferenceType, userInfoMap, new WebRTCCallService.ServiceCallback<QBRTCSession>() {
            @Override
            public void onSuccess(QBRTCSession qbrtcSession) {
                Map sessionMap = WebRTCMapper.qBRTCSessionToMap(qbrtcSession);
                result.success(sessionMap);
            }

            @Override
            public void onError(String errorMessage) {
                result.error(errorMessage, null, null);
            }
        });
    }

    private void accept(Map data, final MethodChannel.Result result) {
        String sessionId = data != null && data.containsKey("sessionId") ? (String) data.get("sessionId") : null;
        Map<String, Object> userInfo = data != null && data.containsKey("userInfo") ? (Map) data.get("userInfo") : null;

        if (TextUtils.isEmpty(sessionId)) {
            result.error("The id is required parameter", null, null);
            return;
        }

        if (webRTCCallService == null) {
            result.error("The call service is not connected", null, null);
            return;
        }

        Map<String, String> userInfoMap = new HashMap<>();
        if (userInfo != null && userInfo.size() > 0) {
            for (Map.Entry<String, Object> entry : userInfo.entrySet()) {
                String key = entry.getKey();
                String value = (String) entry.getValue();
                userInfoMap.put(key, value);
            }
        }

        webRTCCallService.acceptCall(sessionId, userInfoMap, new WebRTCCallService.ServiceCallback<QBRTCSession>() {
            @Override
            public void onSuccess(QBRTCSession qbrtcSession) {
                Map<String, Object> sessionMap = WebRTCMapper.qBRTCSessionToMap(qbrtcSession);

                // TODO: 2019-08-12 need to find more clean solution
                if ((int) sessionMap.get("state") == 1) {
                    sessionMap.put("state", 3);
                }

                result.success(sessionMap);
            }

            @Override
            public void onError(String errorMessage) {
                result.error(errorMessage, null, null);
            }
        });
    }

    private void reject(Map data, final MethodChannel.Result result) {
        String sessionId = data != null && data.containsKey("sessionId") ? (String) data.get("sessionId") : null;
        Map<String, Object> userInfo = data != null && data.containsKey("userInfo") ? (Map) data.get("userInfo") : null;

        if (TextUtils.isEmpty(sessionId)) {
            result.error("The id is required parameter", null, null);
            return;
        }

        if (webRTCCallService == null) {
            result.error("The call service is not connected", null, null);
            return;
        }

        Map<String, String> userInfoMap = new HashMap<>();
        if (userInfo != null && userInfo.size() > 0) {
            for (Map.Entry<String, Object> entry : userInfo.entrySet()) {
                String key = entry.getKey();
                String value = (String) entry.getValue();
                userInfoMap.put(key, value);
            }
        }

        webRTCCallService.rejectCall(sessionId, userInfoMap, new WebRTCCallService.ServiceCallback<QBRTCSession>() {
            @Override
            public void onSuccess(QBRTCSession qbrtcSession) {
                Map sessionMap = WebRTCMapper.qBRTCSessionToMap(qbrtcSession);
                result.success(sessionMap);
            }

            @Override
            public void onError(String errorMessage) {
                result.error(errorMessage, null, null);
            }
        });
    }

    private void hangUp(Map data, final MethodChannel.Result result) {
        String sessionId = data != null && data.containsKey("sessionId") ? (String) data.get("sessionId") : null;
        Map<String, Object> userInfo = data != null && data.containsKey("userInfo") ? (Map) data.get("userInfo") : null;

        if (TextUtils.isEmpty(sessionId)) {
            result.error("The id is required parameter", null, null);
            return;
        }

        if (webRTCCallService == null) {
            result.error("The call service is not connected", null, null);
            return;
        }

        Map<String, String> userInfoMap = new HashMap<>();
        if (userInfo != null && userInfo.size() > 0) {
            for (Map.Entry<String, Object> entry : userInfo.entrySet()) {
                String key = entry.getKey();
                String value = (String) entry.getValue();
                userInfoMap.put(key, value);
            }
        }

        webRTCCallService.hangUpCall(sessionId, userInfoMap, new WebRTCCallService.ServiceCallback<QBRTCSession>() {
            @Override
            public void onSuccess(QBRTCSession qbrtcSession) {
                Map sessionMap = WebRTCMapper.qBRTCSessionToMap(qbrtcSession);
                result.success(sessionMap);
            }

            @Override
            public void onError(String errorMessage) {
                result.error(errorMessage, null, null);
            }
        });
    }

    private void enableVideo(Map data, final MethodChannel.Result result) {
        String sessionId = data != null && data.containsKey("sessionId") ? (String) data.get("sessionId") : null;
        boolean enabled = (data != null && data.containsKey("enable")) && (boolean) data.get("enable");
        Double userId = data != null && data.containsKey("userId") ? (double) data.get("userId") : null;

        if (TextUtils.isEmpty(sessionId)) {
            result.error("The id is required parameter", null, null);
            return;
        }

        if (webRTCCallService == null) {
            result.error("The call service is not connected", null, null);
            return;
        }

        webRTCCallService.setVideoEnabled(enabled, userId != null && userId > 0 ? userId.intValue() : -1,
                sessionId, new WebRTCCallService.ServiceCallback<Void>() {
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

    private void enableAudio(Map data, final MethodChannel.Result result) {
        String sessionId = data != null && data.containsKey("sessionId") ? (String) data.get("sessionId") : null;
        boolean enabled = (data != null && data.containsKey("enable")) && (boolean) data.get("enable");
        Double userId = data != null && data.containsKey("userId") ? (double) data.get("userId") : null;

        if (TextUtils.isEmpty(sessionId)) {
            result.error("The id is required parameter", null, null);
            return;
        }

        if (webRTCCallService == null) {
            result.error("The call service is not connected", null, null);
            return;
        }

        webRTCCallService.setAudioEnabled(enabled, userId != null && userId > 0 ? userId.intValue() : -1,
                sessionId, new WebRTCCallService.ServiceCallback<Void>() {
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

        if (webRTCCallService == null) {
            result.error("The call service is not connected", null, null);
            return;
        }

        webRTCCallService.switchCamera(sessionId, new CameraVideoCapturer.CameraSwitchHandler() {
            @Override
            public void onCameraSwitchDone(boolean frontCamera) {
                result.success(null);
            }

            @Override
            public void onCameraSwitchError(String errorMessage) {
                result.error(errorMessage, null, null);
            }
        }, new WebRTCCallService.ServiceCallback<Void>() {
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

    private void bindCallService(int action, MethodChannel.Result result) {
        if (callServiceConnection == null) {
            callServiceConnection = new CallServiceConnection();
        }

        ((CallServiceConnection) callServiceConnection).setAction(action);
        ((CallServiceConnection) callServiceConnection).setPromise(result);

        Intent intent = new Intent(context, WebRTCCallService.class);
        context.bindService(intent, callServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindCallService() {
        if (callServiceConnection != null) {
            context.unbindService(callServiceConnection);
            callServiceConnection = null;
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
            webRTCCallService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WebRTCCallService.ServiceBinder binder = (WebRTCCallService.ServiceBinder) service;

            webRTCCallService = binder.getService();
            webRTCCallService.setContext(context);

            if (action == CONNECTED_ACTION && result != null) {
                result.success(null);
            }
        }
    }

    void switchAudioOutput(Map data, final MethodChannel.Result result) {
        Integer audioDevice = data != null && data.containsKey("output") ? (Integer) data.get("output") : null;

        if (audioDevice == null) {
            result.error("The output is required parameter", null, null);
            return;
        }

        if (webRTCCallService == null) {
            result.error("The call service is not connected", null, null);
        }

        webRTCCallService.switchAudioOutput(audioDevice, result);
    }
}