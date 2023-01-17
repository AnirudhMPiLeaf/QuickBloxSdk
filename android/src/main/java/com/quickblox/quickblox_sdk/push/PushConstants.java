package com.quickblox.quickblox_sdk.push;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class PushConstants {

    static final String PUSH_CHANNEL = "PUSH_CHANNEL";

    private PushConstants() {
        //empty
    }

    ///////////////////////////////////////////////////////////////////////////
    // CHANNEL NAMES
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            ChannelNames.GCM,
            ChannelNames.APNS,
            ChannelNames.APNS_VOIP,
            ChannelNames.EMAIL
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface ChannelNames {
        String GCM = "gcm";
        String APNS = "apns";
        String APNS_VOIP = "apns_voip";
        String EMAIL = "email";
    }
}