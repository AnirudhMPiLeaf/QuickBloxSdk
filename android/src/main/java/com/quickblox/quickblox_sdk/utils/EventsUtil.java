package com.quickblox.quickblox_sdk.utils;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
public class EventsUtil {
    private static final String PAYLOAD = "payload";
    private static final String TYPE = "type";

    private EventsUtil() {
        //empty
    }

    public static <T> Map<String, Object> buildPayload(String eventName, T data) {
        Map<String, Object> payload = new HashMap<>();

        if (!TextUtils.isEmpty(eventName)) {
            payload.put(TYPE, eventName);
        }

        if (data != null) {
            payload.put(PAYLOAD, data);
        }

        return payload;
    }
}