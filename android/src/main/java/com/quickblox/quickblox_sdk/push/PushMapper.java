package com.quickblox.quickblox_sdk.push;

import android.text.TextUtils;

import com.quickblox.messages.model.QBNotificationChannel;
import com.quickblox.messages.model.QBSubscription;

import java.util.HashMap;
import java.util.Map;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class PushMapper {

    private PushMapper() {
        //empty
    }

    static Map qbSubscriptionToMap(QBSubscription subscription) {
        Map<String, Object> map = new HashMap<>();

        if (subscription.getId() != null && subscription.getId() > 0) {
            map.put("id", subscription.getId());
        }
        if (!TextUtils.isEmpty(subscription.getDeviceUdid())) {
            map.put("deviceUdid", subscription.getDeviceUdid());
        }
        if (!TextUtils.isEmpty(subscription.getRegistrationID())) {
            map.put("deviceToken", subscription.getRegistrationID());
        }
        if (!TextUtils.isEmpty(subscription.getDevice().getPlatform().getCaption())) {
            String devicePlatform = subscription.getDevice().getPlatform().getCaption();
            map.put("devicePlatform", devicePlatform);
        }
        if (!TextUtils.isEmpty(subscription.getNotificationChannel().getCaption())) {
            String pushChannel = subscription.getNotificationChannel().getCaption();
            map.put("pushChannel", pushChannel);
        }

        return map;
    }

    static QBNotificationChannel getQBNotificationChannelByValue(String value) {
        QBNotificationChannel channel = null;
        if (value.equals(QBNotificationChannel.GCM.toString())) {
            channel = QBNotificationChannel.GCM;
        } else if (value.equals(QBNotificationChannel.APNS.toString())) {
            channel = QBNotificationChannel.APNS;
        } else if (value.equals(QBNotificationChannel.APNS_VOIP.toString())) {
            channel = QBNotificationChannel.APNS_VOIP;
        } else if (value.equals(QBNotificationChannel.EMAIL.toString())) {
            channel = QBNotificationChannel.EMAIL;
        } else if (value.equals(QBNotificationChannel.PULL.toString())) {
            channel = QBNotificationChannel.PULL;
        }
        return channel;
    }
}