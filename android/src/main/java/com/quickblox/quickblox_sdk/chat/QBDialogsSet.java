package com.quickblox.quickblox_sdk.chat;

import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBChatDialogMessageSentListener;
import com.quickblox.chat.listeners.QBChatDialogParticipantListener;
import com.quickblox.chat.listeners.QBChatDialogTypingListener;
import com.quickblox.chat.model.QBChatDialog;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Injoit on 2020-01-14.
 * Copyright © 2019 Quickblox. All rights reserved.
 */
public class QBDialogsSet extends CopyOnWriteArraySet<QBChatDialog> {

    private final QBDialogAddToSetListener dialogAddToSetListeners;

    QBDialogsSet(QBDialogAddToSetListener dialogAddToSetListeners) {
        this.dialogAddToSetListeners = dialogAddToSetListeners;
    }

    @Override
    public boolean add(QBChatDialog dialog) {
        boolean added = super.add(dialog);
        if (added && dialogAddToSetListeners != null) {
            dialogAddToSetListeners.onAdded(dialog);
        }
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends QBChatDialog> collection) {
        boolean added = super.addAll(collection);
        if (added && dialogAddToSetListeners != null) {
            for (QBChatDialog dialog : collection) {
                dialogAddToSetListeners.onAdded(dialog);
            }
        }
        return added;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if (containsAll(collection)) {
            for (QBChatDialog dialog : (Collection<QBChatDialog>) collection) {
                QBChatDialog foundDialog = getDialogById(dialog.getDialogId());
                if (foundDialog != null) {
                    removeAllListeners(foundDialog);
                }
            }
        }

        return super.removeAll(collection);
    }

    @Override
    public boolean remove(Object object) {
        if (contains(object)) {
            QBChatDialog foundDialog = getDialogById(((QBChatDialog) object).getDialogId());
            if (foundDialog != null) {
                removeAllListeners(foundDialog);
            }
        }

        return super.remove(object);
    }

    private QBChatDialog getDialogById(String dialogId) {
        QBChatDialog foundDialog = null;
        for (QBChatDialog dialog : this) {
            if (dialog.getDialogId().equals(dialogId)) {
                foundDialog = dialog;
            }
        }

        return foundDialog;
    }

    private void removeAllListeners(QBChatDialog dialog) {
        if (dialog.getIsTypingListeners() != null) {
            for (QBChatDialogTypingListener listener : dialog.getIsTypingListeners()) {
                dialog.removeIsTypingListener(listener);
            }
        }

        if (dialog.getMessageListeners() != null) {
            for (QBChatDialogMessageListener listener : dialog.getMessageListeners()) {
                dialog.removeMessageListrener(listener);
            }
        }

        if (dialog.getMessageSentListeners() != null) {
            for (QBChatDialogMessageSentListener listener : dialog.getMessageSentListeners()) {
                dialog.removeMessageSentListener(listener);
            }
        }

        if (dialog.getParticipantListeners() != null) {
            for (QBChatDialogParticipantListener listener : dialog.getParticipantListeners()) {
                dialog.removeParticipantListener(listener);
            }
        }
    }

    @Override
    public void clear() {
        for (QBChatDialog dialog : this) {
            removeAllListeners(dialog);
        }
        super.clear();
    }

    interface QBDialogAddToSetListener {
        void onAdded(QBChatDialog chatDialog);
    }
}