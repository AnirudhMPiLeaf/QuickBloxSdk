package com.quickblox.quickblox_sdk.chat.listeners;

import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.quickblox_sdk.chat.ChatConstants;
import com.quickblox.quickblox_sdk.chat.ChatMapper;
import com.quickblox.quickblox_sdk.event.EventHandler;
import com.quickblox.quickblox_sdk.utils.EventsUtil;

import java.util.Map;

public class SystemMessageListener implements QBSystemMessageListener {
    private final String TAG = SystemMessageListener.class.getSimpleName();

    @Override
    public void processMessage(QBChatMessage qbChatMessage) {
        String eventName = ChatConstants.Events.RECEIVED_SYSTEM_MESSAGE;
        Map<String, Object> message = ChatMapper.qbChatMessageToMap(qbChatMessage);
        Map<String, Object> payload = EventsUtil.buildPayload(eventName, message);
        EventHandler.sendEvent(eventName, payload);
    }

    @Override
    public void processError(QBChatException e, QBChatMessage qbChatMessage) {
        //ignore
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals;
        if (obj instanceof SystemMessageListener) {
            equals = TAG.equals(((SystemMessageListener) obj).TAG);
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