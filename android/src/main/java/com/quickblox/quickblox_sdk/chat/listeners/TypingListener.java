package com.quickblox.quickblox_sdk.chat.listeners;

import com.quickblox.chat.listeners.QBChatDialogTypingListener;
import com.quickblox.quickblox_sdk.chat.ChatConstants;
import com.quickblox.quickblox_sdk.event.EventHandler;
import com.quickblox.quickblox_sdk.utils.EventsUtil;

import java.util.HashMap;
import java.util.Map;

public class TypingListener implements QBChatDialogTypingListener {
    private static final String DIALOG_ID = "dialogId";
    private static final String USER_ID = "userId";

    private final String dialogId;

    public TypingListener(String dialogId) {
        this.dialogId = dialogId;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals;
        if (obj instanceof TypingListener) {
            equals = this.dialogId.equals(((TypingListener) obj).dialogId);
        } else {
            equals = super.equals(obj);
        }
        return equals;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + dialogId.hashCode();
        return hash;
    }

    @Override
    public void processUserIsTyping(String dialogId, Integer userId) {
        Map<String, Object> data = new HashMap<>();
        data.put(DIALOG_ID, dialogId);
        data.put(USER_ID, userId);

        String eventName = ChatConstants.Events.USER_IS_TYPING;
        Map<String, Object> payload = EventsUtil.buildPayload(eventName, data);
        EventHandler.sendEvent(eventName, payload);
    }

    @Override
    public void processUserStopTyping(String dialogId, Integer userId) {
        Map<String, Object> data = new HashMap<>();
        data.put(DIALOG_ID, dialogId);
        data.put(USER_ID, userId);

        String eventName = ChatConstants.Events.USER_STOPPED_TYPING;
        Map<String, Object> payload = EventsUtil.buildPayload(eventName, data);
        EventHandler.sendEvent(eventName, payload);
    }
}
