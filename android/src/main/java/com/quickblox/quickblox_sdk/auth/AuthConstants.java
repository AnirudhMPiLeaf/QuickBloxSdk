package com.quickblox.quickblox_sdk.auth;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Injoit on 2020-01-14.
 * Copyright © 2019 Quickblox. All rights reserved.
 */
public class AuthConstants {

    private AuthConstants() {
        // empty
    }

    ///////////////////////////////////////////////////////////////////////////
    // EVENTS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            Events.SESSION_EXPIRED
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Events {
        String SESSION_EXPIRED = AuthModule.CHANNEL_NAME + "/SESSION_EXPIRED";
    }

    public static List<String> getAllEvents() {
        List<String> events = new ArrayList<>();

        events.add(Events.SESSION_EXPIRED);
        return events;
    }
}