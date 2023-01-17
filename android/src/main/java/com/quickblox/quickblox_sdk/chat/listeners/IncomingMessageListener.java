package com.quickblox.quickblox_sdk.chat.listeners;

import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.quickblox_sdk.chat.ChatConstants;
import com.quickblox.quickblox_sdk.chat.ChatMapper;
import com.quickblox.quickblox_sdk.event.EventHandler;
import com.quickblox.quickblox_sdk.utils.EventsUtil;

import java.util.Map;

public class IncomingMessageListener implements QBChatDialogMessageListener {
    private final String TAG = IncomingMessageListener.class.getSimpleName();

    @Override
    public void processMessage(String dialogId, QBChatMessage qbChatMessage, Integer integer) {
        Integer loggedUserId = QBSessionManager.getInstance().getSessionParameters().getUserId();

        if (!qbChatMessage.getSenderId().equals(loggedUserId)) {
            Map<String, Object> data = ChatMapper.qbChatMessageToMap(qbChatMessage);
            String eventName = ChatConstants.Events.RECEIVED_NEW_MESSAGE;
            Map<String, Object> payload = EventsUtil.buildPayload(eventName, data);
            EventHandler.sendEvent(eventName, payload);
        }
    }

    @Override
    public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
        //empty
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals;
        if (obj instanceof IncomingMessageListener) {
            equals = TAG.equals(((IncomingMessageListener) obj).TAG);
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
