package com.quickblox.quickblox_sdk.webrtc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IceServerUtils {
    public IceServerUtils() {
        //private
    }

    public static List<Map<String, String>> buildCorrectIceServers() {
        List<Map<String, String>> iceServers = new ArrayList<>();

        iceServers.add(buildIceServerMap("stun:stun.l.google.com:19302", "", ""));
        iceServers.add(buildIceServerMap("stun:turn.quickblox.com", "quickblox", "baccb97ba2d92d71e26eb9886da5f1e0"));
        iceServers.add(buildIceServerMap("turn:turn.quickblox.com:3478?transport=udp", "quickblox", "baccb97ba2d92d71e26eb9886da5f1e0"));
        iceServers.add(buildIceServerMap("turn:turn.quickblox.com:3478?transport=tcp", "quickblox", "baccb97ba2d92d71e26eb9886da5f1e0"));

        return iceServers;
    }

    public static List<Map<String, String>> buildWrongIceServers() {
        List<Map<String, String>> iceServers = new ArrayList<>();

        iceServers.add(buildIceServerMap("", "", ""));
        iceServers.add(buildIceServerMap("", "quickblox", "baccb97ba2d92d71e26eb9886da5f1e0"));
        iceServers.add(buildIceServerMap("", "quickblox", "baccb97ba2d92d71e26eb9886da5f1e0"));
        iceServers.add(buildIceServerMap("", "quickblox", "baccb97ba2d92d71e26eb9886da5f1e0"));

        return iceServers;
    }

    public static List<Map<String, String>> build_3_correct_1_Wrong_IceServers() {
        List<Map<String, String>> iceServers = new ArrayList<>();

        iceServers.add(buildIceServerMap("stun:stun.l.google.com:19302", "", ""));
        iceServers.add(buildIceServerMap("stun:turn.quickblox.com", "quickblox", "baccb97ba2d92d71e26eb9886da5f1e0"));
        iceServers.add(buildIceServerMap("", "quickblox", "baccb97ba2d92d71e26eb9886da5f1e0"));
        iceServers.add(buildIceServerMap("turn:turn.quickblox.com:3478?transport=udp", "quickblox", "baccb97ba2d92d71e26eb9886da5f1e0"));

        return iceServers;
    }

    private static Map<String, String> buildIceServerMap(String url, String userName, String password) {
        Map<String, String> iceServer = new HashMap<>();

        iceServer.put("url", url);
        iceServer.put("userName", userName);
        iceServer.put("password", password);

        return iceServer;
    }
}
