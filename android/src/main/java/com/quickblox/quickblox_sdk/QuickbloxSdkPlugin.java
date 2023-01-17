package com.quickblox.quickblox_sdk;

import android.content.Context;

import com.quickblox.quickblox_sdk.auth.AuthModule;
import com.quickblox.quickblox_sdk.chat.ChatModule;
import com.quickblox.quickblox_sdk.conference.ConferenceModule;
import com.quickblox.quickblox_sdk.conference.ConferenceViewFactory;
import com.quickblox.quickblox_sdk.customobjects.CustomObjectsModule;
import com.quickblox.quickblox_sdk.file.FileModule;
import com.quickblox.quickblox_sdk.notification.NotificationModule;
import com.quickblox.quickblox_sdk.push.PushModule;
import com.quickblox.quickblox_sdk.settings.SettingsModule;
import com.quickblox.quickblox_sdk.users.UsersModule;
import com.quickblox.quickblox_sdk.webrtc.QBRTCConfigModule;
import com.quickblox.quickblox_sdk.webrtc.WebRTCModule;
import com.quickblox.quickblox_sdk.webrtc.WebRTCViewFactory;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.platform.PlatformViewRegistry;

/**
 * Created by Injoit on 2020-01-14.
 * Copyright © 2019 Quickblox. All rights reserved.
 */
public class QuickbloxSdkPlugin implements FlutterPlugin {
    private static final String WEB_RTC_VIEW_TYPE_ID = "QBWebRTCViewFactory";
    private static final String CONFERENCE_VIEW_TYPE_ID = "QBConferenceViewFactory";

    //Modules
    private AuthModule authModule;
    private SettingsModule settingsModule;
    private UsersModule usersModule;
    private ChatModule chatModule;
    private CustomObjectsModule customObjectsModule;
    private FileModule fileModule;
    private NotificationModule notificationModule;
    private PushModule pushModule;
    private WebRTCModule webRTCModule;
    private QBRTCConfigModule qbrtcConfigModule;
    private ConferenceModule conferenceModule;

    //View Managers
    private WebRTCViewFactory webRTCViewFactory;
    private ConferenceViewFactory conferenceViewFactory;

    private static QuickbloxSdkPlugin sdkPlugin;

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        initPlugin();
        sdkPlugin.initModules(registrar.context(), registrar.messenger());
        sdkPlugin.initViewsFactory(registrar.platformViewRegistry(), registrar.messenger());
    }

    private static synchronized void initPlugin() {
        if (sdkPlugin == null) {
            sdkPlugin = new QuickbloxSdkPlugin();
        }
    }

    @Override
    public void onAttachedToEngine(FlutterPlugin.FlutterPluginBinding flutterPluginBinding) {
        initPlugin();
        sdkPlugin.initModules(flutterPluginBinding.getApplicationContext(), flutterPluginBinding.getBinaryMessenger());
        sdkPlugin.initViewsFactory(flutterPluginBinding.getPlatformViewRegistry(), flutterPluginBinding.getBinaryMessenger());
    }

    @Override
    public void onDetachedFromEngine(FlutterPlugin.FlutterPluginBinding flutterPluginBinding) {
        //empty
    }

    private void initModules(Context context, BinaryMessenger binaryMessenger) {
        //AuthModule
        authModule = new AuthModule(binaryMessenger);
        MethodChannel authChannel = new MethodChannel(binaryMessenger, authModule.getChannelName());
        authChannel.setMethodCallHandler(authModule.getMethodHandler());

        //SettingsModule
        settingsModule = new SettingsModule(context);
        MethodChannel settingsChannel = new MethodChannel(binaryMessenger, settingsModule.getChannelName());
        settingsChannel.setMethodCallHandler(settingsModule.getMethodHandler());

        //UsersModule
        usersModule = new UsersModule();
        MethodChannel usersChannel = new MethodChannel(binaryMessenger, usersModule.getChannelName());
        usersChannel.setMethodCallHandler(usersModule.getMethodHandler());

        //Chat Module
        chatModule = new ChatModule(binaryMessenger);
        MethodChannel chatChannel = new MethodChannel(binaryMessenger, chatModule.getChannelName());
        chatChannel.setMethodCallHandler(chatModule.getMethodHandler());

        //Custom Objects Module
        customObjectsModule = new CustomObjectsModule();
        MethodChannel customObjectsChannel = new MethodChannel(binaryMessenger, customObjectsModule.getChannelName());
        customObjectsChannel.setMethodCallHandler(customObjectsModule.getMethodHandler());

        //File Module
        fileModule = new FileModule(context, binaryMessenger);
        MethodChannel fileChannel = new MethodChannel(binaryMessenger, fileModule.getChannelName());
        fileChannel.setMethodCallHandler(fileModule.getMethodHandler());

        //Notification Module
        notificationModule = new NotificationModule();
        MethodChannel notificationChannel = new MethodChannel(binaryMessenger, notificationModule.getChannelName());
        notificationChannel.setMethodCallHandler(notificationModule.getMethodHandler());

        //Push Module
        pushModule = new PushModule(context);
        MethodChannel pushChannel = new MethodChannel(binaryMessenger, pushModule.getChannelName());
        pushChannel.setMethodCallHandler(pushModule.getMethodHandler());

        //WebRTC Module
        webRTCModule = new WebRTCModule(binaryMessenger, context);
        MethodChannel webRTCChannel = new MethodChannel(binaryMessenger, webRTCModule.getChannelName());
        webRTCChannel.setMethodCallHandler(webRTCModule.getMethodHandler());

        //RTCConfig Module
        qbrtcConfigModule = new QBRTCConfigModule(binaryMessenger, context);
        MethodChannel qbRTCConfigChannel = new MethodChannel(binaryMessenger, qbrtcConfigModule.getChannelName());
        qbRTCConfigChannel.setMethodCallHandler(qbrtcConfigModule.getMethodHandler());

        //Conference Module
        conferenceModule = new ConferenceModule(binaryMessenger, context);
        MethodChannel conferenceChannel = new MethodChannel(binaryMessenger, conferenceModule.getChannelName());
        conferenceChannel.setMethodCallHandler(conferenceModule.getMethodHandler());
    }

    private void initViewsFactory(PlatformViewRegistry platformViewRegistry, BinaryMessenger binaryMessenger) {
        //View Factory
        webRTCViewFactory = new WebRTCViewFactory(binaryMessenger);
        platformViewRegistry.registerViewFactory(WEB_RTC_VIEW_TYPE_ID, webRTCViewFactory);

        conferenceViewFactory = new ConferenceViewFactory(binaryMessenger);
        platformViewRegistry.registerViewFactory(CONFERENCE_VIEW_TYPE_ID, conferenceViewFactory);
    }
}