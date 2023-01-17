package com.quickblox.quickblox_sdk.notification;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import com.quickblox.messages.model.QBEventType;
import com.quickblox.messages.model.QBNotificationType;
import com.quickblox.messages.model.QBPushType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class NotificationConstants {

    private NotificationConstants() {
        //empty
    }

    ///////////////////////////////////////////////////////////////////////////
    // EVENT TYPES
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            EventTypes.FIXED_DATE,
            EventTypes.PERIOD_DATE,
            EventTypes.ONE_SHOT
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface EventTypes {
        String FIXED_DATE = "fixed_date";
        String PERIOD_DATE = "period_date";
        String ONE_SHOT = "one_shot";
    }

    static QBEventType getQBEventType(String type) {
        QBEventType qbEventType = null;
        switch (type) {
            case EventTypes.FIXED_DATE:
                qbEventType = QBEventType.FIXED_DATE;
                break;
            case EventTypes.PERIOD_DATE:
                qbEventType = QBEventType.PERIOD_DATE;
                break;
            case EventTypes.ONE_SHOT:
                qbEventType = QBEventType.ONE_SHOT;
                break;
        }
        return qbEventType;
    }

    ///////////////////////////////////////////////////////////////////////////
    // TYPES
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            Types.PUSH,
            Types.EMAIL
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface Types {
        String PUSH = "push";
        String EMAIL = "email";
    }

    static QBNotificationType getQBNotificationType(String notificationType) {
        QBNotificationType qbNotificationType = null;
        switch (notificationType) {
            case Types.PUSH:
                qbNotificationType = QBNotificationType.PUSH;
                break;
            case Types.EMAIL:
                qbNotificationType = QBNotificationType.EMAIL;
                break;
        }
        return qbNotificationType;
    }

    ///////////////////////////////////////////////////////////////////////////
    // PUSH TYPES
    ///////////////////////////////////////////////////////////////////////////
    @IntDef({
            PushTypes.APNS,
            PushTypes.APNS_VOIP,
            PushTypes.GCM,
            PushTypes.MPNS
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface PushTypes {
        int APNS = 1;
        int APNS_VOIP = 2;
        int GCM = 3;
        int MPNS = 4;
    }

    static int getPushType(String eventNotificationChannel) {
        int pushType = 0;
        switch (eventNotificationChannel) {
            case "APNS":
                pushType = PushTypes.APNS;
                break;
            case "APNS_VOIP":
                pushType = PushTypes.APNS_VOIP;
                break;
            case "GCM":
                pushType = PushTypes.GCM;
                break;
            case "MPNS":
                pushType = PushTypes.MPNS;
                break;
        }
        return pushType;
    }

    static QBPushType getQBPushType(int type) {
        QBPushType qbPushType = null;
        switch (type) {
            case PushTypes.APNS:
                qbPushType = QBPushType.APNS;
                break;
            case PushTypes.APNS_VOIP:
                qbPushType = QBPushType.APNS_VOIP;
                break;
            case PushTypes.GCM:
                qbPushType = QBPushType.GCM;
                break;
            case PushTypes.MPNS:
                qbPushType = QBPushType.MPNS;
                break;
        }
        return qbPushType;
    }

    ///////////////////////////////////////////////////////////////////////////
    // PERIODS
    ///////////////////////////////////////////////////////////////////////////
    @IntDef({
            Periods.DAY,
            Periods.WEEK,
            Periods.MONTH,
            Periods.YEAR
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface Periods {
        int DAY = 86400;
        int WEEK = 604800;
        int MONTH = 2592000;
        int YEAR = 31557600;
    }
}