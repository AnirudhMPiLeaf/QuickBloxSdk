package com.quickblox.quickblox_sdk.notification;

import android.os.Bundle;
import android.text.TextUtils;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBEventType;
import com.quickblox.messages.model.QBNotificationType;
import com.quickblox.messages.model.QBPushType;
import com.quickblox.quickblox_sdk.BuildConfig;
import com.quickblox.quickblox_sdk.base.BaseModule;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
public class NotificationModule implements BaseModule {
    private static final String CHANNEL_NAME = "FlutterQBNotificationEventsChannel";

    private static final String CREATE_METHOD = "create";
    private static final String UPDATE_METHOD = "update";
    private static final String REMOVE_METHOD = "remove";
    private static final String GET_BY_ID_METHOD = "getById";
    private static final String GET_METHOD = "get";

    @Override
    public void initEventHandler() {
        //empty
    }

    @Override
    public String getChannelName() {
        return CHANNEL_NAME;
    }

    @Override
    public MethodChannel.MethodCallHandler getMethodHandler() {
        return this::handleMethod;
    }

    @Override
    public void handleMethod(MethodCall methodCall, MethodChannel.Result result) {
        Map<String, Object> data = methodCall.arguments();
        switch (methodCall.method) {
            case CREATE_METHOD:
                create(data, result);
                break;
            case UPDATE_METHOD:
                update(data, result);
                break;
            case REMOVE_METHOD:
                remove(data, result);
                break;
            case GET_BY_ID_METHOD:
                getById(data, result);
                break;
            case GET_METHOD:
                get(data, result);
                break;
        }
    }

    private void create(Map data, final MethodChannel.Result result) {
        Integer id = data != null && data.containsKey("id") ? (int) data.get("id") : null;
        Boolean active = data != null && data.containsKey("active") ? (boolean) data.get("active") : null;
        String name = data != null && data.containsKey("name") ? (String) data.get("name") : null;
        String type = data != null && data.containsKey("type") ? (String) data.get("type") : null;
        String notificationType = data != null && data.containsKey("notificationType") ? (String) data.get("notificationType") : null;
        Integer pushType = data != null && data.containsKey("pushType") ? (int) data.get("pushType") : null;
        Double date = data != null && data.containsKey("date") ? (double) data.get("date") : null;
        Integer endDate = data != null && data.containsKey("endDate") ? (int) data.get("endDate") : null;
        String period = data != null && data.containsKey("period") ? (String) data.get("period") : null;
        Integer occuredCount = data != null && data.containsKey("occuredCount") ? (int) data.get("occuredCount") : null;
        Integer senderId = data != null && data.containsKey("senderId") ? (int) data.get("senderId") : null;
        List<Integer> recipientsIds = data != null && data.containsKey("recipientsIds") ? (List<Integer>) data.get("recipientsIds") : null;
        List<String> recipientsTagsAny = data != null && data.containsKey("recipientsTagsAny") ? (List<String>) data.get("recipientsTagsAny") : null;
        List<String> recipientsTagsAll = data != null && data.containsKey("recipientsTagsAll") ? (List<String>) data.get("recipientsTagsAll") : null;
        List<String> recipientsTagsExclude = data != null && data.containsKey("recipientsTagsExclude") ? (List<String>) data.get("recipientsTagsExclude") : null;
        Map<String, Object> payload = data != null && data.containsKey("payload") ? (Map) data.get("payload") : null;

        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(notificationType) || senderId == null || senderId <= 0) {
            result.error("The type, notificationType, senderId are required parameters", null, null);
            return;
        }

        QBEvent qbEvent = new QBEvent();
        qbEvent.setNotificationType(QBNotificationType.PUSH);

        qbEvent.setUserId(senderId);

        if (id != null && id > 0) {
            qbEvent.setId(id);
        }

        if (active != null) {
            qbEvent.setActive(active);
        }

        qbEvent.setEnvironment(QBEnvironment.PRODUCTION);
        if (BuildConfig.DEBUG) {
            qbEvent.setEnvironment(QBEnvironment.DEVELOPMENT);
        }

        if (!TextUtils.isEmpty(name)) {
            qbEvent.setName(name);
        }

        QBEventType eventType = NotificationConstants.getQBEventType(type);
        qbEvent.setType(eventType);

        if (eventType == null) {
            result.error("Wrong type parameter", null, null);
            return;
        }

        if (eventType.equals(QBEventType.FIXED_DATE) || eventType.equals(QBEventType.PERIOD_DATE)) {
            if (date == null || date <= 0) {
                result.error("The date is required parameter for type " + eventType.toString(), null, null);
                return;
            } else {
                long modifiedDate = date.longValue();
                modifiedDate = modifiedDate / 1000;
                int eventDate = (int) modifiedDate;
                qbEvent.setDate(eventDate);
            }
        }

        if (!TextUtils.isEmpty(notificationType)) {
            QBNotificationType qbNotificationType = NotificationConstants.getQBNotificationType(notificationType);
            qbEvent.setNotificationType(qbNotificationType);
        }

        if (pushType != null && pushType > 0) {
            QBPushType qbPushType = NotificationConstants.getQBPushType(pushType);
            qbEvent.setPushType(qbPushType);
        }

        if (endDate != null && endDate > 0) {
            qbEvent.setEndDate(endDate);
        }

        if (!TextUtils.isEmpty(period)) {
            Integer qbEventPeriod = Integer.valueOf(period);
            qbEvent.setPeriod(qbEventPeriod);
        }

        if (occuredCount != null && occuredCount > 0) {
            qbEvent.setOccuredCount(occuredCount);
        }

        if (recipientsIds != null && recipientsIds.size() > 0) {
            StringifyArrayList<Integer> userIds = new StringifyArrayList<>();
            userIds.addAll(recipientsIds);
            qbEvent.setUserIds(userIds);
        }

        if (recipientsTagsAny != null && recipientsTagsAny.size() > 0) {
            StringifyArrayList<String> userTagsAny = new StringifyArrayList<>();
            Collections.addAll(recipientsTagsAny);
            qbEvent.setUserTagsAny(userTagsAny);
        }

        if (recipientsTagsAll != null && recipientsTagsAll.size() > 0) {
            StringifyArrayList<String> userTagsAll = new StringifyArrayList<>();
            userTagsAll.addAll(recipientsTagsAll);
            qbEvent.setUserTagsAll(userTagsAll);
        }

        if (recipientsTagsExclude != null && recipientsTagsExclude.size() > 0) {
            StringifyArrayList<String> userTagsExclude = new StringifyArrayList<>();
            userTagsExclude.addAll(recipientsTagsExclude);
            qbEvent.setUserTagsExclude(userTagsExclude);
        }

        if (payload != null && payload.size() > 0) {
            HashMap<String, Object> payloadMap = (HashMap<String, Object>) payload;
            JSONObject json = new JSONObject(payloadMap);
            String qbEventPayload = json.toString();
            qbEvent.setMessage(qbEventPayload);
        }

        QBPushNotifications.createEvent(qbEvent).performAsync(new QBEntityCallback<QBEvent>() {
            @Override
            public void onSuccess(QBEvent qbEvent, Bundle bundle) {
                List<Map> eventsMap = new ArrayList<>();
                Map map = NotificationMapper.qbEventToMap(qbEvent);
                eventsMap.add(map);
                result.success(eventsMap);
            }

            @Override
            public void onError(QBResponseException e) {
                result.error(e.getMessage(), null, null);
            }
        });
    }

    private void update(Map data, final MethodChannel.Result result) {
        Integer id = data != null && data.containsKey("id") ? (int) data.get("id") : null;
        Boolean active = data != null && data.containsKey("active") ? (boolean) data.get("active") : null;
        Map<String, Object> payload = data != null && data.containsKey("payload") ? (Map) data.get("payload") : null;
        Double date = data != null && data.containsKey("date") ? (double) data.get("date") : null;
        String period = data != null && data.containsKey("period") ? (String) data.get("period") : null;
        String name = data != null && data.containsKey("name") ? (String) data.get("name") : null;

        QBEvent qbEvent = new QBEvent();
        qbEvent.setNotificationType(QBNotificationType.PUSH);

        if (id == null || id <= 0) {
            result.error("The id is required parameter", null, null);
            return;
        }

        qbEvent.setEnvironment(QBEnvironment.PRODUCTION);
        if (BuildConfig.DEBUG) {
            qbEvent.setEnvironment(QBEnvironment.DEVELOPMENT);
        }

        qbEvent.setId(id);

        if (active != null) {
            qbEvent.setActive(active);
        }

        if (payload != null && payload.size() > 0) {
            HashMap<String, Object> payloadMap = (HashMap<String, Object>) payload;
            JSONObject json = new JSONObject(payloadMap);
            String qbEventPayload = json.toString();
            qbEvent.setMessage(qbEventPayload);
        }

        if (date != null && date > 0) {
            long modifiedDate = date.longValue();
            modifiedDate = modifiedDate / 1000;
            int eventDate = (int) modifiedDate;
            qbEvent.setDate(eventDate);
        }

        if (!TextUtils.isEmpty(period)) {
            Integer qbEventPeriod = Integer.valueOf(period);
            qbEvent.setPeriod(qbEventPeriod);
        }

        if (!TextUtils.isEmpty(name)) {
            qbEvent.setName(name);
        }

        QBPushNotifications.updateEvent(qbEvent).performAsync(new QBEntityCallback<QBEvent>() {
            @Override
            public void onSuccess(QBEvent qbEvent, Bundle bundle) {
                Map map = NotificationMapper.qbEventToMap(qbEvent);
                result.success(map);
            }

            @Override
            public void onError(QBResponseException e) {
                result.error(e.getMessage(), null, null);
            }
        });
    }

    private void remove(Map data, final MethodChannel.Result result) {
        Integer eventId = data != null && data.containsKey("id") ? (int) data.get("id") : null;

        if (eventId == null || eventId <= 0) {
            result.error("The parameter id is required", null, null);
            return;
        }

        QBPushNotifications.deleteEvent(eventId).performAsync(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                result.success(null);
            }

            @Override
            public void onError(QBResponseException e) {
                result.error(e.getMessage(), null, null);
            }
        });
    }

    private void getById(Map data, final MethodChannel.Result result) {
        Integer eventId = data != null && data.containsKey("id") ? (int) data.get("id") : null;

        if (eventId == null || eventId <= 0) {
            result.error("The parameter id is required", null, null);
            return;
        }

        QBPushNotifications.getEvent(eventId).performAsync(new QBEntityCallback<QBEvent>() {
            @Override
            public void onSuccess(QBEvent qbEvent, Bundle bundle) {
                Map map = NotificationMapper.qbEventToMap(qbEvent);
                result.success(map);
            }

            @Override
            public void onError(QBResponseException e) {
                result.error(e.getMessage(), null, null);
            }
        });
    }

    private void get(Map data, final MethodChannel.Result result) {
        int page = data != null && data.containsKey("page") ? (int) data.get("page") : 1;
        int perPage = data != null && data.containsKey("perPage") ? (int) data.get("perPage") : 10;

        QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
        requestBuilder.setPage(page);
        requestBuilder.setPerPage(perPage);

        QBPushNotifications.getEvents(requestBuilder, null).performAsync(new QBEntityCallback<ArrayList<QBEvent>>() {
            @Override
            public void onSuccess(ArrayList<QBEvent> qbEvents, Bundle bundle) {
                List<Map> eventsMap = new ArrayList<>();
                for (QBEvent qbEvent : qbEvents) {
                    Map map = NotificationMapper.qbEventToMap(qbEvent);
                    eventsMap.add(map);
                }
                result.success(eventsMap);
            }

            @Override
            public void onError(QBResponseException e) {
                result.error(e.getMessage(), null, null);
            }
        });
    }
}