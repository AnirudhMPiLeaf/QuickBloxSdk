
package com.quickblox.quickblox_sdk.push;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.Utils;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBNotificationChannel;
import com.quickblox.messages.model.QBSubscription;
import com.quickblox.quickblox_sdk.BuildConfig;
import com.quickblox.quickblox_sdk.base.BaseModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
public class PushModule implements BaseModule {
    private static final String CHANNEL_NAME = "FlutterQBPushSubscriptionsChannel";

    private static final String CREATE_METHOD = "create";
    private static final String GET_METHOD = "get";
    private static final String REMOVE_METHOD = "remove";

    private Context reactContext;

    public PushModule(Context reactContext) {
        this.reactContext = reactContext;
    }

    @Override
    public void initEventHandler() {

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
            case GET_METHOD:
                get(data, result);
                break;
            case REMOVE_METHOD:
                remove(data, result);
                break;
        }
    }

    private void create(Map data, final MethodChannel.Result result) {
        String deviceToken = data != null && data.containsKey("deviceToken") ? (String) data.get("deviceToken") : null;
        String pushChannel = data != null && data.containsKey("pushChannel") ? (String) data.get("pushChannel") : null;

        if (TextUtils.isEmpty(deviceToken)) {
            result.error("The registrationId is required parameter", null, null);
            return;
        }

        QBSubscription qbSubscription = new QBSubscription();
        qbSubscription.setNotificationChannel(QBNotificationChannel.GCM);
        String deviceId = Utils.generateDeviceId();
        qbSubscription.setDeviceUdid(deviceId);
        qbSubscription.setRegistrationID(deviceToken);

        if (!TextUtils.isEmpty(pushChannel)) {
            QBNotificationChannel notificationChannel = PushMapper.getQBNotificationChannelByValue(pushChannel);
            qbSubscription.setNotificationChannel(notificationChannel);
        }

        qbSubscription.setEnvironment(QBEnvironment.PRODUCTION);
        if (BuildConfig.DEBUG) {
            qbSubscription.setEnvironment(QBEnvironment.DEVELOPMENT);
        }

        QBPushNotifications.createSubscription(qbSubscription).performAsync(new QBEntityCallback<ArrayList<QBSubscription>>() {
            @Override
            public void onSuccess(ArrayList<QBSubscription> qbSubscriptions, Bundle bundle) {
                List<Map> array = new ArrayList<>();
                for (QBSubscription subscription : qbSubscriptions) {
                    Map map = PushMapper.qbSubscriptionToMap(subscription);
                    array.add(map);
                }
                result.success(array);
            }

            @Override
            public void onError(QBResponseException e) {
                result.error(e.getMessage(), null, null);
            }
        });
    }

    private void get(Map<String, Object> data, final MethodChannel.Result result) {
        QBPushNotifications.getSubscriptions().performAsync(new QBEntityCallback<ArrayList<QBSubscription>>() {
            @Override
            public void onSuccess(ArrayList<QBSubscription> qbSubscriptions, Bundle bundle) {
                List<Map> subscriptionsList = new ArrayList<>();
                for (QBSubscription subscription : qbSubscriptions) {
                    Map map = PushMapper.qbSubscriptionToMap(subscription);
                    subscriptionsList.add(map);
                }
                result.success(subscriptionsList);
            }

            @Override
            public void onError(QBResponseException e) {
                result.error(e.getMessage(), null, null);
            }
        });
    }

    private void remove(Map data, final MethodChannel.Result result) {
        Integer subscriptionId = data != null && data.containsKey("id") ? (int) data.get("id") : null;

        if (subscriptionId == null) {
            result.error("The subscriptionId is required parameter", null, null);
            return;
        }

        QBPushNotifications.deleteSubscription(subscriptionId).performAsync(new QBEntityCallback<Void>() {
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
}