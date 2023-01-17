package com.quickblox.quickblox_sdk.webrtc;

import android.content.Context;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

/**
 * Created by Injoit on 2020-01-11.
 * Copyright © 2019 Quickblox. All rights reserved.
 */
public class WebRTCViewFactory extends PlatformViewFactory {
    private BinaryMessenger messenger;

    public WebRTCViewFactory(BinaryMessenger messenger) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
    }

    @Override
    public PlatformView create(Context context, int viewId, Object args) {
        return new WebRTCVideoView(context, messenger, viewId);
    }
}
