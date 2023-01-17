package com.quickblox.quickblox_sdk.webrtc;

import android.text.TextUtils;

import org.webrtc.PeerConnection;

import java.util.HashMap;
import java.util.Map;

public class IceServerMapper {
    private IceServerMapper() {
        //private
    }

    public static Map<String, String> iceServerToMap(PeerConnection.IceServer iceServer) {
        HashMap<String, String> map = new HashMap<>();

        if (iceServer != null && !TextUtils.isEmpty(iceServer.uri)) {
            map.put("url", iceServer.uri);
            map.put("userName", TextUtils.isEmpty(iceServer.username) ? "" : iceServer.username);
            map.put("password", TextUtils.isEmpty(iceServer.password) ? "" : iceServer.password);
        }

        return map;
    }

    public static Map<String, String> valuesToIceServerMap(String uri, String userName, String password) {
        HashMap<String, String> map = new HashMap<>();

        if (!TextUtils.isEmpty(uri)) {
            map.put("url", uri);
            map.put("userName", TextUtils.isEmpty(userName) ? "" : userName);
            map.put("password", TextUtils.isEmpty(password) ? "" : password);
        }

        return map;
    }
}