package com.quickblox.quickblox_sdk.chat;

import android.text.TextUtils;

import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogCustomData;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.core.request.QueryRule;
import com.quickblox.quickblox_sdk.utils.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
public class ChatMapper {

    private ChatMapper() {
        //private
    }

    public static QBAttachment mapToQbAttachment(Map<String, Object> attachmentMap) {
        String attachmentType = attachmentMap.containsKey("type") ? (String) attachmentMap.get("type") : null;
        String attachmentId = attachmentMap.containsKey("id") ? (String) attachmentMap.get("id") : null;
        String attachmentUrl = attachmentMap.containsKey("url") ? (String) attachmentMap.get("url") : null;
        String attachmentName = attachmentMap.containsKey("name") ? (String) attachmentMap.get("name") : null;
        String contentType = attachmentMap.containsKey("contentType") ? (String) attachmentMap.get("contentType") : null;
        String attachmentData = attachmentMap.containsKey("data") ? (String) attachmentMap.get("data") : null;
        Integer attachmentSize = attachmentMap.containsKey("size") ? (Integer) attachmentMap.get("size") : null;
        Integer attachmentHeight = attachmentMap.containsKey("height") ? (Integer) attachmentMap.get("height") : null;
        Integer attachmentWidth = attachmentMap.containsKey("width") ? (Integer) attachmentMap.get("width") : null;
        Integer attachmentDuration = attachmentMap.containsKey("duration") ? (Integer) attachmentMap.get("duration") : null;

        if (TextUtils.isEmpty(attachmentType)) {
            return null;
        }

        QBAttachment attachment = new QBAttachment(attachmentType);

        if (!TextUtils.isEmpty(attachmentId)) {
            attachment.setId(attachmentId);
        }
        if (!TextUtils.isEmpty(attachmentUrl)) {
            attachment.setUrl(attachmentUrl);
        }
        if (!TextUtils.isEmpty(attachmentName)) {
            attachment.setName(attachmentName);
        }
        if (!TextUtils.isEmpty(contentType)) {
            attachment.setContentType(contentType);
        }
        if (!TextUtils.isEmpty(attachmentData)) {
            attachment.setData(attachmentData);
        }
        if (attachmentSize != null) {
            attachment.setSize(attachmentSize);
        }
        if (attachmentHeight != null) {
            attachment.setHeight(attachmentHeight);
        }
        if (attachmentWidth != null) {
            attachment.setWidth(attachmentWidth);
        }
        if (attachmentDuration != null) {
            attachment.setDuration(attachmentDuration);
        }

        return attachment;
    }

    public static Map<String, Object> qbAttachmentToMap(QBAttachment attachment) {
        Map<String, Object> map = new HashMap<>();

        if (!TextUtils.isEmpty(attachment.getType())) {
            map.put("type", attachment.getType());
        }
        if (!TextUtils.isEmpty(attachment.getId())) {
            map.put("id", attachment.getId());
        }
        if (!TextUtils.isEmpty(attachment.getUrl())) {
            map.put("url", attachment.getUrl());
        }
        if (!TextUtils.isEmpty(attachment.getName())) {
            map.put("name", attachment.getName());
        }
        if (!TextUtils.isEmpty(attachment.getContentType())) {
            map.put("contentType", attachment.getContentType());
        }
        if (!TextUtils.isEmpty(attachment.getData())) {
            map.put("data", attachment.getData());
        }
        if (attachment.getSize() > 0) {
            map.put("size", attachment.getSize());
        }
        if (attachment.getHeight() != 0) {
            map.put("height", attachment.getHeight());
        }
        if (attachment.getWidth() != 0) {
            map.put("width", attachment.getWidth());
        }
        if (attachment.getDuration() != 0) {
            map.put("duration", attachment.getDuration());
        }

        return map;
    }

    public static QBChatMessage mapToQBChatMessage(Map<String, Object> map) {
        QBChatMessage message = new QBChatMessage();

        String id = "";
        if (map.containsKey("id") && map.get("id") instanceof String) {
            id = (String) map.get("id");
        }

        Integer senderId = null;
        if (map.containsKey("senderId") && map.get("senderId") instanceof Integer) {
            senderId = (Integer) map.get("senderId");
        }

        String dialogId = "";
        if (map.containsKey("dialogId") && map.get("dialogId") instanceof String) {
            dialogId = (String) map.get("dialogId");
        }

        message.setId(id);
        message.setSenderId(senderId);
        message.setDialogId(dialogId);

        return message;
    }

    public static Map<String, Object> qbChatMessageToMap(QBChatMessage message) {
        Map<String, Object> map = new HashMap<>();

        if (!TextUtils.isEmpty(message.getId())) {
            map.put("id", message.getId());
        }
        if (message.getAttachments() != null && message.getAttachments().size() > 0) {
            List<Map<String, Object>> attachmentsArray = new ArrayList<>();
            for (QBAttachment qbAttachment : message.getAttachments()) {
                Map<String, Object> attachment = qbAttachmentToMap(qbAttachment);
                attachmentsArray.add(attachment);
            }
            map.put("attachments", attachmentsArray);
        }
        if (message.getProperties() != null && message.getProperties().size() > 0) {
            Map<String, String> writableMap = new HashMap<>();
            for (Map.Entry<String, String> entry : message.getProperties().entrySet()) {
                String propertyName = entry.getKey();
                String propertyValue = entry.getValue();
                writableMap.put(propertyName, propertyValue);
            }
            map.put("properties", writableMap);
        }
        if (message.getDateSent() != 0) {
            map.put("dateSent", message.getDateSent() * 1000);
        }
        if (message.getSenderId() != null && message.getSenderId() > 0) {
            map.put("senderId", message.getSenderId());
        }
        if (message.getRecipientId() != null && message.getRecipientId() > 0) {
            map.put("recipientId", message.getRecipientId());
        }
        if (message.getReadIds() != null && message.getReadIds().size() > 0) {
            map.put("readIds", message.getReadIds());
        }
        if (message.getDeliveredIds() != null && message.getDeliveredIds().size() > 0) {
            map.put("deliveredIds", message.getDeliveredIds());
        }
        if (!TextUtils.isEmpty(message.getDialogId())) {
            map.put("dialogId", message.getDialogId());
        }
        map.put("markable", message.isMarkable());
        map.put("delayed", message.isDelayed());
        if (!TextUtils.isEmpty(message.getBody())) {
            map.put("body", message.getBody());
        }

        return map;
    }

    public static Map<String, Object> qbChatDialogToMap(QBChatDialog dialog) {
        Map<String, Object> map = new HashMap<>();

        map.put("isJoined", dialog.isJoined() || dialog.getType().equals(QBDialogType.PRIVATE));

        if (dialog.getCreatedAt() != null) {
            Date date = dialog.getCreatedAt();
            String createdAt = DateUtil.convertDateToISO(date);
            map.put("createdAt", createdAt);
        }
        if (!TextUtils.isEmpty(dialog.getLastMessage())) {
            map.put("lastMessage", dialog.getLastMessage());
        }
        if (dialog.getLastMessageDateSent() > 0) {
            map.put("lastMessageDateSent", dialog.getLastMessageDateSent() * 1000);
        }
        if (dialog.getLastMessageUserId() != null && dialog.getLastMessageUserId() > 0) {
            map.put("lastMessageUserId", dialog.getLastMessageUserId());
        }
        if (!TextUtils.isEmpty(dialog.getName())) {
            map.put("name", dialog.getName());
        }
        if (!TextUtils.isEmpty(dialog.getPhoto())) {
            map.put("photo", dialog.getPhoto());
        }
        if (dialog.getType().getCode() > 0) {
            map.put("type", dialog.getType().getCode());
        }
        if (dialog.getUnreadMessageCount() != null) {
            map.put("unreadMessagesCount", dialog.getUnreadMessageCount());
        }
        if (dialog.getUpdatedAt() != null) {
            Date date = dialog.getUpdatedAt();
            String updatedAt = DateUtil.convertDateToISO(date);
            map.put("updatedAt", updatedAt);
        }
        if (dialog.getUserId() != null && dialog.getUserId() > 0) {
            map.put("userId", dialog.getUserId());
        }
        if (!TextUtils.isEmpty(dialog.getRoomJid())) {
            map.put("roomJid", dialog.getRoomJid());
        }
        if (!TextUtils.isEmpty(dialog.getDialogId())) {
            map.put("id", dialog.getDialogId());
        }
        if (dialog.getOccupants() != null) {
            map.put("occupantsIds", dialog.getOccupants());
        }

        if (dialog.getCustomData() != null && !TextUtils.isEmpty(dialog.getCustomData().getClassName())) {
            Map<String, Object> customDataMap = qbDialogCustomDataToMap(dialog.getCustomData());
            map.put("customData", customDataMap);
        }

        return map;
    }

    public static Map<String, Object> qbDialogCustomDataToMap(QBDialogCustomData customData) {
        Map<String, Object> map = new HashMap<>();

        if (!TextUtils.isEmpty(customData.getClassName())) {
            map.put("class_name", customData.getClassName());
        }

        HashMap<String, Object> objectHashMap = customData.getFields();
        for (Map.Entry<String, Object> entry : objectHashMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (!TextUtils.isEmpty(key) && value != null) {
                map.put(key, value);
            }
        }

        return map;
    }

    public static QBRequestGetBuilder addDialogSortToRequestBuilder(QBRequestGetBuilder requestGetBuilder, Map<String, Object> sortMap) {
        String sortField = sortMap != null && sortMap.containsKey("field") ? (String) sortMap.get("field") : null;

        if (sortField == null) {
            return requestGetBuilder;
        }

        boolean ascendingValue = sortMap.containsKey("ascending") && (boolean) sortMap.get("ascending");
        if (ascendingValue) {
            requestGetBuilder.sortAsc(sortField);
        } else {
            requestGetBuilder.sortDesc(sortField);
        }

        return requestGetBuilder;
    }

    public static QBRequestGetBuilder addDialogFilterToRequestBuilder(QBRequestGetBuilder requestGetBuilder, Map<String, Object> filterMap) {
        String filterField = filterMap != null && filterMap.containsKey("field") ? (String) filterMap.get("field") : null;
        String filterOperator = filterMap != null && filterMap.containsKey("operator") ? (String) filterMap.get("operator") : null;
        String filterValue = filterMap != null && filterMap.containsKey("value") ? (String) filterMap.get("value") : null;

        if (filterOperator == null) {
            return requestGetBuilder;
        }

        switch (filterOperator) {
            case ChatConstants.DialogFilterOperators.LT:
                requestGetBuilder.lt(filterField, filterValue);
                break;
            case ChatConstants.DialogFilterOperators.LTE:
                requestGetBuilder.lte(filterField, filterValue);
                break;
            case ChatConstants.DialogFilterOperators.GT:
                requestGetBuilder.gt(filterField, filterValue);
                break;
            case ChatConstants.DialogFilterOperators.GTE:
                requestGetBuilder.gte(filterField, filterValue);
                break;
            case ChatConstants.DialogFilterOperators.NE:
                requestGetBuilder.ne(filterField, filterValue);
                break;
            case ChatConstants.DialogFilterOperators.IN:
                requestGetBuilder.in(filterField, filterValue);
                break;
            case ChatConstants.DialogFilterOperators.NIN:
                requestGetBuilder.nin(filterField, filterValue);
                break;
            case ChatConstants.DialogFilterOperators.ALL:
                requestGetBuilder.all(filterField, filterValue);
                break;
            case ChatConstants.DialogFilterOperators.CTN:
                requestGetBuilder.ctn(filterField, filterValue);
                break;
            default:
                requestGetBuilder.addRule(filterField, QueryRule.EQ, filterValue);
                break;
        }

        return requestGetBuilder;
    }

    public static QBRequestGetBuilder addMessageSortToRequestBuilder(QBRequestGetBuilder requestGetBuilder, Map<String, Object> sortMap) {
        if (sortMap == null) {
            return requestGetBuilder;
        }

        String sortField = sortMap.containsKey("field") ? (String) sortMap.get("field") : null;
        boolean ascendingValue = sortMap.containsKey("ascending") && (boolean) sortMap.get("ascending");

        if (ascendingValue) {
            requestGetBuilder.sortAsc(sortField);
        } else {
            requestGetBuilder.sortDesc(sortField);
        }

        return requestGetBuilder;
    }

    public static QBRequestGetBuilder addMessageFilterToRequestBuilder(QBRequestGetBuilder requestGetBuilder, Map<String, Object> filterMap) {
        String filterField = filterMap != null && filterMap.containsKey("field") ? (String) filterMap.get("field") : null;
        String filterOperator = filterMap != null && filterMap.containsKey("operator") ? (String) filterMap.get("operator") : null;
        String filterValue = filterMap != null && filterMap.containsKey("value") ? (String) filterMap.get("value") : null;

        if (filterOperator == null) {
            return requestGetBuilder;
        }

        switch (filterOperator) {
            case ChatConstants.MessageFilterOperators.LT:
                requestGetBuilder.lt(filterField, filterValue);
                break;
            case ChatConstants.MessageFilterOperators.LTE:
                requestGetBuilder.lte(filterField, filterValue);
                break;
            case ChatConstants.MessageFilterOperators.GT:
                requestGetBuilder.gt(filterField, filterValue);
                break;
            case ChatConstants.MessageFilterOperators.GTE:
                requestGetBuilder.gte(filterField, filterValue);
                break;
            case ChatConstants.MessageFilterOperators.NE:
                requestGetBuilder.ne(filterField, filterValue);
                break;
            case ChatConstants.MessageFilterOperators.IN:
                requestGetBuilder.in(filterField, filterValue);
                break;
            case ChatConstants.MessageFilterOperators.NIN:
                requestGetBuilder.nin(filterField, filterValue);
                break;
            case ChatConstants.MessageFilterOperators.OR:
                requestGetBuilder.all(filterField, filterValue);
                break;
            case ChatConstants.MessageFilterOperators.CTN:
                requestGetBuilder.ctn(filterField, filterValue);
                break;
            default:
                requestGetBuilder.addRule(filterField, QueryRule.EQ, filterValue);
                break;
        }

        return requestGetBuilder;
    }
}