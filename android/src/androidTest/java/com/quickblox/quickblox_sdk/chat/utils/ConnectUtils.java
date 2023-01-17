package com.quickblox.quickblox_sdk.chat.utils;

import com.quickblox.quickblox_sdk.BaseTest;

import java.util.HashMap;
import java.util.Map;

public class ConnectUtils {
    private ConnectUtils() {
        //private
    }

    public static Map<String, Object> buildCorrectCredentials() {
        Map<String, Object> credentialsData = new HashMap<>();

        credentialsData.put("userId", BaseTest.USER_ID);
        credentialsData.put("password", BaseTest.USER_PASSWORD);

        return credentialsData;
    }
}
