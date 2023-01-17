package com.quickblox.quickblox_sdk.base;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
public interface BaseModule {

    void initEventHandler();

    String getChannelName();

    MethodChannel.MethodCallHandler getMethodHandler();

    void handleMethod(MethodCall methodCall, MethodChannel.Result result);
}