package com.quickblox.quickblox_sdk.chat.listeners;

import com.quickblox.chat.listeners.QBMessageStatusListener;
import com.quickblox.quickblox_sdk.chat.ChatConstants;
import com.quickblox.quickblox_sdk.event.EventHandler;
import com.quickblox.quickblox_sdk.utils.EventsUtil;

import java.util.HashMap;
import java.util.Map;

public class StatusMessageListener implements QBMessageStatusListener {
    private static final String MESSAGE_ID = "messageId";
    private static final String DIALOG_ID = "dialogId";
    private static final String USER_ID = "userId";
    private final String TAG = StatusMessageListener.class.getSimpleName();

    @Override
    public void processMessageDelivered(String messageId, String dialogId, Integer userId) {
        Map<String, Object> data = new HashMap<>();
        data.put(MESSAGE_ID, messageId);
        data.put(DIALOG_ID, dialogId);
        data.put(USER_ID, userId);

        String eventName = ChatConstants.Events.MESSAGE_DELIVERED;
        Map<String, Object> payload = EventsUtil.buildPayload(eventName, data);
        EventHandler.sendEvent(eventName, payload);
    }

    @Override
    public void processMessageRead(String messageId, String dialogId, Integer userId) {
        Map<String, Object> data = new HashMap<>();
        data.put(MESSAGE_ID, messageId);
        data.put(DIALOG_ID, dialogId);
        data.put(USER_ID, userId);

        String eventName = ChatConstants.Events.MESSAGE_READ;
        Map<String, Object> payload = EventsUtil.buildPayload(eventName, data);
        EventHandler.sendEvent(eventName, payload);
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals;
        if (obj instanceof StatusMessageListener) {
            equals = TAG.equals(((StatusMessageListener) obj).TAG);
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
