package com.quickblox.quickblox_sdk.event;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
public class EventHandler implements EventChannel.StreamHandler {

    private final static Set<EventHandler> eventHandlers = new HashSet<>();

    private EventChannel.EventSink eventSink;
    private String eventName;

    public static void init(List<String> eventNames, BinaryMessenger binaryMessenger) {
        for (String event : eventNames) {
            init(event, binaryMessenger);
        }
    }

    public static void init(String eventName, BinaryMessenger binaryMessenger) {
        EventHandler eventHandler = new EventHandler(eventName);
        eventHandler.init(binaryMessenger);
        eventHandlers.add(eventHandler);
    }

    public static <T> void sendEvent(String eventName, T payload) {
        if (!eventHandlers.contains(new EventHandler(eventName))) {
            return;
        }

        for (EventHandler eventHandler : eventHandlers) {
            if (eventHandler.eventName.equals(eventName) && eventHandler.eventSink != null) {
                eventHandler.eventSink.success(payload);
                break;
            }
        }
    }

    private EventHandler(String eventName) {
        this.eventName = eventName;
    }

    private void init(BinaryMessenger binaryMessenger) {
        new EventChannel(binaryMessenger, eventName).setStreamHandler(this);
    }

    @Override
    public void onListen(Object arguments, EventChannel.EventSink eventSink) {
        if (arguments != null) {
            this.eventName = arguments.toString();
        }
        this.eventSink = eventSink;

        // todo: when using the library (firebase_messaging) in FlutterPlugin onAttachedToEngine() method
        //  called twice (https://github.com/flutter/flutter/issues/69721).
        //  need to check the actual fix on library firebase_messaging and we have two instances of binary
        //  messengers, it's made wrong EventHandler instances.
        //  we should replace EventHandler every time.
        eventHandlers.remove(this);
        eventHandlers.add(this);
    }

    @Override
    public void onCancel(Object o) {
        this.eventSink = null;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals;
        if (obj instanceof EventHandler) {
            equals = this.eventName.equals(((EventHandler) obj).eventName);
        } else {
            equals = super.equals(obj);
        }
        return equals;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + eventName.hashCode();
        return hash;
    }
}