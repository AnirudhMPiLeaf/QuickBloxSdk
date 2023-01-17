package com.quickblox.quickblox_sdk.webrtc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;

import com.quickblox.videochat.webrtc.view.QBRTCSurfaceView;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;

import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

/**
 * Created by Injoit on 2020-01-11.
 * Copyright © 2019 Quickblox. All rights reserved.
 */
public class WebRTCVideoView implements PlatformView, MethodChannel.MethodCallHandler {
    private static final String MIRROR_METHOD = "mirror";
    private static final String SCALE_TYPE_METHOD = "scaleType";
    private static final String PLAY_METHOD = "play";
    private static final String RELEASE_METHOD = "release";

    private final QBRTCSurfaceView videoView;
    private final MethodChannel methodChannel;
    private WebRTCCallService callService;
    private ServiceConnection callServiceConnection;
    private final Context context;

    WebRTCVideoView(Context context, BinaryMessenger messenger, int id) {
        videoView = new QBRTCSurfaceView(context);
        methodChannel = new MethodChannel(messenger, "QBWebRTCFlutterVideoViewChannel/" + id);
        methodChannel.setMethodCallHandler(this);
        this.context = context;
    }

    @Override
    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
        switch (methodCall.method) {
            case MIRROR_METHOD:
                mirror(methodCall, result);
                break;
            case SCALE_TYPE_METHOD:
                scaleType(methodCall, result);
                break;
            case PLAY_METHOD:
                play(methodCall, result);
                break;
            case RELEASE_METHOD:
                release(methodCall, result);
                break;
            default:
                result.notImplemented();
        }
    }

    @Override
    public View getView() {
        return videoView;
    }

    @Override
    public void dispose() {
        unbindCallService();
    }


    private void mirror(MethodCall methodCall, MethodChannel.Result result) {
        // TODO: need implement
    }

    private void scaleType(MethodCall methodCall, MethodChannel.Result result) {
        // TODO: need implement
    }

    private void play(MethodCall methodCall, MethodChannel.Result result) {
        if (callServiceConnection == null) {
            bindCallService(methodCall, result);
        } else {
            playVideo(methodCall, result);
        }
    }

    private void playVideo(MethodCall methodCall, MethodChannel.Result result) {
        Map<String, Object> data = methodCall.arguments();

        String sessionId = data != null && data.containsKey("sessionId") ? (String) data.get("sessionId") : null;
        Integer userId = data != null && data.containsKey("userId") ? (Integer) data.get("userId") : null;

        callService.getVideoTrack(sessionId, userId, new WebRTCCallService.ServiceCallback<QBRTCVideoTrack>() {
            @Override
            public void onSuccess(QBRTCVideoTrack videoTrack) {
                if (videoTrack != null) {
                    videoTrack.addRenderer(videoView);
                    videoView.requestLayout();
                } else {
                    result.error("The video track is null", null, null);
                }
            }

            @Override
            public void onError(String errorMessage) {
                result.error(errorMessage, null, null);
            }
        });
    }

    private void release(MethodCall methodCall, MethodChannel.Result result) {
        // TODO: 3/31/21 need to add release render, need to check to available this logic
        unbindCallService();
        result.success(null);
    }

    private void bindCallService(MethodCall methodCall, MethodChannel.Result result) {
        if (callServiceConnection == null) {
            callServiceConnection = new CallServiceConnection(methodCall, result);
        }

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
        private final MethodCall methodCall;
        private final MethodChannel.Result result;

        CallServiceConnection(MethodCall methodCall, MethodChannel.Result result) {
            this.methodCall = methodCall;
            this.result = result;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            callService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WebRTCCallService.ServiceBinder binder = (WebRTCCallService.ServiceBinder) service;
            callService = binder.getService();
            playVideo(methodCall, result);
        }
    }
}
