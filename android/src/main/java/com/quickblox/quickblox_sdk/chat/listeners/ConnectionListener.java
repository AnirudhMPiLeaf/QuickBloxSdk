package com.quickblox.quickblox_sdk.chat.listeners;

import com.quickblox.quickblox_sdk.chat.ChatConstants;
import com.quickblox.quickblox_sdk.event.EventHandler;
import com.quickblox.quickblox_sdk.utils.EventsUtil;

import org.jivesoftware.smack.XMPPConnection;

public class ConnectionListener implements org.jivesoftware.smack.ConnectionListener {
    private final String TAG = ConnectionListener.class.getSimpleName();

    @Override
    public void connected(XMPPConnection xmppConnection) {
        String eventName = ChatConstants.Events.CONNECTED;
        EventHandler.sendEvent(eventName, EventsUtil.buildPayload(eventName, null));
    }

    @Override
    public void authenticated(XMPPConnection xmppConnection, boolean b) {
        //ignore
    }

    @Override
    public void connectionClosed() {
        String eventName = ChatConstants.Events.CONNECTION_CLOSED;
        EventHandler.sendEvent(eventName, EventsUtil.buildPayload(eventName, null));
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        String eventName = ChatConstants.Events.RECONNECTION_FAILED;
        EventHandler.sendEvent(eventName, EventsUtil.buildPayload(eventName, null));
    }

    @Override
    public void reconnectionSuccessful() {
        String eventName = ChatConstants.Events.RECONNECTION_SUCCESSFUL;
        EventHandler.sendEvent(eventName, EventsUtil.buildPayload(eventName, null));
    }

    @Override
    public void reconnectingIn(int i) {
        //ignore
    }

    @Override
    public void reconnectionFailed(Exception e) {
        String eventName = ChatConstants.Events.RECONNECTION_FAILED;
        EventHandler.sendEvent(eventName, EventsUtil.buildPayload(eventName, null));
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals;
        if (obj instanceof ConnectionListener) {
            equals = TAG.equals(((ConnectionListener) obj).TAG);
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