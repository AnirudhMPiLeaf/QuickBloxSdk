package com.quickblox.quickblox_sdk.webrtc;

import android.content.Context;
import android.text.TextUtils;

import com.quickblox.quickblox_sdk.base.BaseModule;
import com.quickblox.quickblox_sdk.event.EventHandler;
import com.quickblox.videochat.webrtc.QBRTCConfig;

import org.webrtc.PeerConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * Created by Injoit on 2021-01-06.
 * Copyright © 2020 Quickblox. All rights reserved.
 */
public class QBRTCConfigModule implements BaseModule {
    static final String CHANNEL_NAME = "FlutterQBRTCConfigChannel";

    private static final String SET_RECONNECTION_TIME_INTERVAL_METHOD = "setReconnectionTimeInterval";
    private static final String GET_RECONNECTION__TIME_INTERVAL_METHOD = "getReconnectionTimeInterval";
    private static final String SET_ANSWER_TIME_INTERVAL_METHOD = "setAnswerTimeInterval";
    private static final String GET_ANSWER_TIME_INTERVAL_METHOD = "getAnswerTimeInterval";
    private static final String SET_DIALING_TIME_INTERVAL_METHOD = "setDialingTimeInterval";
    private static final String GET_DIALING_TIME_INTERVAL_METHOD = "getDialingTimeInterval";
    public static final String SET_ICE_SERVERS_METHOD = "setICEServers";
    public static final String GET_ICE_SERVERS_METHOD = "getICEServers";

    private static final int MIN_ANSWER_TIME_INTERVAL = 10;
    private static final int MIN_DIALING_TIME_INTERVAL = 3;
    private static final int MIN_RECONNECTION_TIME_INTERVAL = 10;

    private BinaryMessenger binaryMessenger;

    public QBRTCConfigModule() {
        //for tests
    }

    public QBRTCConfigModule(BinaryMessenger binaryMessenger, Context context) {
        this.binaryMessenger = binaryMessenger;
        initEventHandler();
    }

    @Override
    public void initEventHandler() {
        EventHandler.init(QBRTCConfigConstants.getAllEvents(), binaryMessenger);
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
            case SET_ANSWER_TIME_INTERVAL_METHOD:
                setAnswerTimeInterval(methodCall.arguments(), result);
                break;
            case GET_ANSWER_TIME_INTERVAL_METHOD:
                getAnswerTimeInterval(result);
                break;
            case SET_DIALING_TIME_INTERVAL_METHOD:
                setDialingTimeInterval(methodCall.arguments(), result);
                break;
            case GET_DIALING_TIME_INTERVAL_METHOD:
                getDialingTimeInterval(result);
                break;
            case SET_ICE_SERVERS_METHOD:
                setIceServers(methodCall.arguments(), result);
                break;
            case GET_ICE_SERVERS_METHOD:
                getIceServers(result);
                break;
            case SET_RECONNECTION_TIME_INTERVAL_METHOD:
                setReconnectionTimeInterval(methodCall.arguments(), result);
                break;
            case GET_RECONNECTION__TIME_INTERVAL_METHOD:
                getReconnectionTimeInterval(result);
                break;
        }
    }

    private void setAnswerTimeInterval(int interval, final MethodChannel.Result result) {
        if (interval < MIN_ANSWER_TIME_INTERVAL) {
            result.error("Value should be equal to or greater than " + MIN_ANSWER_TIME_INTERVAL,
                    null, null);
            return;
        }
        QBRTCConfig.setAnswerTimeInterval(interval);
        result.success(null);
    }

    private void getAnswerTimeInterval(final MethodChannel.Result result) {
        long interval = QBRTCConfig.getAnswerTimeInterval();
        result.success(interval);
    }

    private void setDialingTimeInterval(int interval, final MethodChannel.Result result) {
        if (interval < MIN_DIALING_TIME_INTERVAL) {
            result.error("Value should be equal to or greater than " + MIN_DIALING_TIME_INTERVAL,
                    null, null);
            return;
        }
        QBRTCConfig.setDialingTimeInterval(interval);
        result.success(null);
    }

    private void getDialingTimeInterval(final MethodChannel.Result result) {
        long interval = QBRTCConfig.getDialingTimeInterval();
        result.success(interval);
    }

    private void setIceServers(List<Object> servers, final MethodChannel.Result result) {
        if (servers == null || servers.size() <= 0) {
            result.error("servers shouldn't be empty", null, null);
            return;
        }

        List<PeerConnection.IceServer> iceServers = new ArrayList<>();

        for (Object item : servers) {
            HashMap<String, String> map = (HashMap<String, String>) item;
            String url = map.containsKey("url") ? map.get("url") : null;
            String userName = map.containsKey("userName") ? map.get("userName") : "";
            String password = map.containsKey("password") ? map.get("password") : "";

            if (TextUtils.isEmpty(url)) {
                result.error("server url shouldn't be empty", null, null);
                return;
            } else {
                PeerConnection.IceServer iceServer = new PeerConnection.IceServer(url, userName, password);
                iceServers.add(iceServer);
            }
        }

        QBRTCConfig.setIceServerList(iceServers);

        result.success(null);
    }

    private void getIceServers(final MethodChannel.Result result) {
        List<Map<String, String>> resultIceServers = new ArrayList<>();

        List<PeerConnection.IceServer> servers = QBRTCConfig.getIceServerList();

        if (servers != null && servers.size() > 0) {
            for (PeerConnection.IceServer item : servers) {
                if (!TextUtils.isEmpty(item.uri) && item.username != null && item.password != null) {
                    Map<String, String> iceServerMap = IceServerMapper.iceServerToMap(item);
                    resultIceServers.add(iceServerMap);
                }
            }
        } else {
            resultIceServers.add(IceServerMapper.valuesToIceServerMap("stun:stun.l.google.com:19302", "", ""));
            resultIceServers.add(IceServerMapper.valuesToIceServerMap("stun:turn.quickblox.com", "quickblox", "baccb97ba2d92d71e26eb9886da5f1e0"));
            resultIceServers.add(IceServerMapper.valuesToIceServerMap("turn:turn.quickblox.com:3478?transport=udp", "quickblox", "baccb97ba2d92d71e26eb9886da5f1e0"));
            resultIceServers.add(IceServerMapper.valuesToIceServerMap("turn:turn.quickblox.com:3478?transport=tcp", "quickblox", "baccb97ba2d92d71e26eb9886da5f1e0"));
        }

        result.success(resultIceServers);
    }

    private void setReconnectionTimeInterval(int interval, final MethodChannel.Result result) {
        if (interval < MIN_RECONNECTION_TIME_INTERVAL) {
            result.error("Value should be equal to or greater than " + MIN_RECONNECTION_TIME_INTERVAL, null, null);
            return;
        }
        QBRTCConfig.setDisconnectTimeInterval(interval);
        result.success(null);
    }

    private void getReconnectionTimeInterval(final MethodChannel.Result result) {
        long interval = QBRTCConfig.getDisconnectTimeInterval();
        result.success(interval);
    }
}