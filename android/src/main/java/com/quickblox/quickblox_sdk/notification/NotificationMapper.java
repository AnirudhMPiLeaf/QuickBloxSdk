package com.quickblox.quickblox_sdk.notification;

import android.text.TextUtils;

import com.quickblox.messages.model.QBEvent;

import java.util.HashMap;
import java.util.Map;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class NotificationMapper {

    private NotificationMapper() {
        //empty
    }

    static Map qbEventToMap(QBEvent event) {
        Map<String, Object> map = new HashMap<>();

        if (event.getId() != null && event.getId() > 0) {
            map.put("id", event.getId());
        }
        if (!TextUtils.isEmpty(event.getName())) {
            map.put("name", event.getName());
        }
        if (event.isActive() != null) {
            map.put("active", event.isActive());
        }
        if (event.getNotificationType() != null && !TextUtils.isEmpty(event.getNotificationType().getCaption())) {
            String notificationType = event.getNotificationType().getCaption();
            map.put("notificationType", notificationType);
        }
        if (!TextUtils.isEmpty(event.getNotificationChannel().getCaption())) {
            String eventNotificationChannel = event.getNotificationChannel().getCaption();
            Integer pushType = NotificationConstants.getPushType(eventNotificationChannel);
            map.put("pushType", pushType);
        }
        if (event.getDate() != null && event.getDate() > 0) {
            double date = event.getDate().doubleValue();
            map.put("date", date);
        }
        if (event.getEndDate() != null && event.getEndDate() > 0) {
            double endDate = event.getEndDate().doubleValue();
            map.put("endDate", endDate);
        }
        if (event.getPeriod() != null && event.getPeriod() > 0) {
            Integer period = event.getPeriod();
            map.put("period", period);
        }
        if (event.getOccuredCount() != null && event.getOccuredCount() > 0) {
            map.put("occuredCount", event.getOccuredCount());
        }
        if (event.getUserId() != null && event.getUserId() > 0) {
            map.put("senderId", event.getUserId());
        }
        if (event.getUserIds() != null && event.getUserIds().size() > 0) {
            map.put("recipientsIds", event.getUserIds());
        }
        if (event.getUserTagsAny() != null && event.getUserTagsAny().size() > 0) {
            map.put("recipientsTagsAny", event.getUserTagsAny());
        }
        if (event.getUserTagsAll() != null && event.getUserTagsAll().size() > 0) {
            map.put("recipientsTagsAll", event.getUserTagsAll());
        }
        if (event.getUserTagsExclude() != null && event.getUserTagsExclude().size() > 0) {
            map.put("recipientsTagsExclude", event.getUserTagsAny());
        }
        if (!TextUtils.isEmpty(event.getMessage())) {
            map.put("payload", event.getMessage());
        }

        return map;
    }
}