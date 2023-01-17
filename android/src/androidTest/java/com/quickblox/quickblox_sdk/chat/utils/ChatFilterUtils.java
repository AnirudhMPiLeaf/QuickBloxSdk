package com.quickblox.quickblox_sdk.chat.utils;

import com.quickblox.chat.model.QBDialogType;
import com.quickblox.quickblox_sdk.BaseTest;

import java.util.HashMap;
import java.util.Map;

public class ChatFilterUtils {
    private ChatFilterUtils() {
        //private
    }

    public static Map<String, Object> buildFilterDialogId(String dialogId) {
        Map<String, Object> map = new HashMap<>();

        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("operator", "all");
        filterMap.put("field", "_id");
        filterMap.put("value", dialogId);


        map.put("filter", filterMap);

        return map;
    }

    public static Map<String, Object> buildFilterDialogType(QBDialogType type) {
        Map<String, Object> map = new HashMap<>();

        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("field", "type");
        filterMap.put("operator", "in");
        filterMap.put("value", String.valueOf(type.getCode()));


        map.put("filter", filterMap);

        return map;
    }

    public static Map<String, Object> buildFilterMessageSenderId() {
        Map<String, Object> filter = new HashMap<>();

        filter.put("operator", "in");
        filter.put("field", "sender_id");
        filter.put("value", String.valueOf(BaseTest.USER_ID));

        return filter;
    }
}