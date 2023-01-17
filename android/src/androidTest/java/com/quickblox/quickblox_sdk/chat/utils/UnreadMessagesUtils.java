package com.quickblox.quickblox_sdk.chat.utils;

import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UnreadMessagesUtils {
    private UnreadMessagesUtils() {
        //private
    }

    public static int calculateCountFromDialogs(List<QBChatDialog> dialogs) {
        int countOfMessages = 0;
        for (QBChatDialog dialog : dialogs) {
            countOfMessages += dialog.getUnreadMessageCount();
        }
        return countOfMessages;
    }

    public static List<Object> getIdsFromDialogs(List<QBChatDialog> dialogs) {
        List<Object> dialogIds = new ArrayList<>();

        for (QBChatDialog dialog : dialogs) {
            dialogIds.add(dialog.getDialogId());
        }

        return dialogIds;
    }

    public static List<Object> buildWrongDialogIds() {
        List<Object> dialogIds = new ArrayList<>();

        dialogIds.add(buildWrongDialogId());
        dialogIds.add(buildWrongDialogId());
        dialogIds.add(buildWrongDialogId());

        return dialogIds;
    }

    public static List<Object> build_3Integer_DialogIds() {
        List<Object> dialogIds = new ArrayList<>();

        dialogIds.add(7778777);
        dialogIds.add(8887888);
        dialogIds.add(8777778);

        return dialogIds;
    }

    public static List<Object> build_2String_1Integer_DialogIds() {
        List<Object> dialogIds = new ArrayList<>();

        dialogIds.add(buildWrongDialogId());
        dialogIds.add(buildWrongDialogId());
        dialogIds.add(8777778);

        return dialogIds;
    }

    public static String buildWrongDialogId() {
        return "wrong_dialog_id_" + System.currentTimeMillis();
    }

    public static int getCountFromArrayOfMap(List<Object> objects) {
        int count = 0;

        for (Object item : objects) {
            count += (Integer) ((HashMap) item).values().toArray()[0];
        }

        return count;
    }
}
