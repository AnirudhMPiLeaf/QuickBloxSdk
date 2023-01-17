package com.quickblox.quickblox_sdk.chat.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtils {
    private MessageUtils() {
        //private
    }

    public static Map<String, Object> buildMessage(String dialogId) {
        Map<String, Object> message = new HashMap<>();

        message.put("dialogId", dialogId);
        message.put("body", "Flutter message body: " + System.currentTimeMillis());
        message.put("attachments", buildAttachments());
        message.put("properties", buildProperties());
        message.put("markable", true);
        message.put("dateSent", System.currentTimeMillis());
        message.put("saveToHistory", false);

        return message;
    }

    public static List<Map<String, Object>> buildAttachments() {
        List<Map<String, Object>> attachments = new ArrayList<>();

        attachments.add(buildAttachment());
        attachments.add(buildAttachment());
        attachments.add(buildAttachment());
        attachments.add(buildAttachment());
        attachments.add(buildAttachment());

        return attachments;
    }

    public static Map<String, Object> buildAttachment() {
        Map<String, Object> attachment = new HashMap<>();

        attachment.put("type", "photo");
        attachment.put("id", "102470n210c9120c09248u" + System.currentTimeMillis());
        attachment.put("url", "https:/quickblox.com");
        attachment.put("name", "test attachment name " + System.currentTimeMillis());
        attachment.put("contentType", "jpg");
        attachment.put("data", "347h9ydfHHHd89qynqp9328ry");
        attachment.put("size", 2048);
        attachment.put("height", 100);
        attachment.put("width", 100);
        attachment.put("duration", 1000);

        return attachment;
    }

    public static Map<String, Object> buildProperties() {
        Map<String, Object> property = new HashMap<>();

        property.put("propertyTestKey " + System.currentTimeMillis(), "propertyTestValue" + System.currentTimeMillis());
        property.put("propertyTestKey " + System.currentTimeMillis(), "propertyTestValue" + System.currentTimeMillis());
        property.put("propertyTestKey " + System.currentTimeMillis(), "propertyTestValue" + System.currentTimeMillis());
        property.put("propertyTestKey " + System.currentTimeMillis(), "propertyTestValue" + System.currentTimeMillis());
        property.put("propertyTestKey " + System.currentTimeMillis(), "propertyTestValue" + System.currentTimeMillis());

        return property;
    }

    public static Map<String, Object> buildMarkMessageEmpty() {
        return buildMarkMessage("", null, "");
    }

    public static Map<String, Object> buildMarkMessageWrong() {
        return buildMarkMessage("wrongId", -100000, "wrongDialogId");
    }

    public static Map<String, Object> buildMarkMessage(String id, Integer senderId, String dialogId) {
        Map<String, Object> message = new HashMap<>();

        message.put("message", new HashMap<String, Object>() {{
            put("id", id);
            put("senderId", senderId);
            put("dialogId", dialogId);
        }});

        return message;
    }
}