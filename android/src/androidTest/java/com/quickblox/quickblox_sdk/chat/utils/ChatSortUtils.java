package com.quickblox.quickblox_sdk.chat.utils;

import java.util.HashMap;
import java.util.Map;

public class ChatSortUtils {
    private ChatSortUtils() {
        //private
    }

    public static Map<String, Object> buildSortMessageByDateSend() {
        Map<String, Object> map = new HashMap<>();

        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("ascending", true);
        filterMap.put("field", "date_sent");

        map.put("filter", filterMap);

        return map;
    }
}