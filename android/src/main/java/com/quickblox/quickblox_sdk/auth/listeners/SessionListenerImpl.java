package com.quickblox.quickblox_sdk.auth.listeners;

import com.quickblox.auth.session.QBSessionListenerImpl;
import com.quickblox.quickblox_sdk.auth.AuthConstants;
import com.quickblox.quickblox_sdk.event.EventHandler;
import com.quickblox.quickblox_sdk.utils.EventsUtil;

public class SessionListenerImpl extends QBSessionListenerImpl {
    private final String TAG = SessionListenerImpl.class.getSimpleName();

    @Override
    public void onSessionExpired() {
        String eventName = AuthConstants.Events.SESSION_EXPIRED;
        EventHandler.sendEvent(eventName, EventsUtil.buildPayload(eventName, null));
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals;
        if (obj instanceof SessionListenerImpl) {
            equals = TAG.equals(((SessionListenerImpl) obj).TAG);
        } else {
            equals = super.equals(obj);
        }
        return equals;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + TAG.hashCode();
        return hash;
    }
}