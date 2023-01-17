package com.quickblox.quickblox_sdk.chat.utils;

import android.text.TextUtils;

import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.helper.CollectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Injoit on 2022-04-08.
 * Copyright © 2022 Quickblox. All rights reserved.
 */

public class ChatDialogUtils {
    public static final int QWE_11 = 109364779;
    public static final int QWE_22 = 109364799;
    public static final int QWE_33 = 110129179;
    public static final int QWE_44 = 110129330;

    private ChatDialogUtils() {
        //private
    }

    public static Map<String, Object> buildGroupDialog() {
        return buildDialog(QBDialogType.GROUP, null, Arrays.asList(QWE_11, QWE_22, QWE_33, QWE_44));
    }

    public static Map<String, Object> buildGroupDialog(String id) {
        return buildDialog(QBDialogType.GROUP, id, null);
    }

    public static Map<String, Object> buildPrivateDialog() {
        return buildDialog(QBDialogType.PRIVATE, null, Arrays.asList(QWE_11, QWE_44));
    }

    public static Map<String, Object> buildPrivateDialog(String id) {
        return buildDialog(QBDialogType.PRIVATE, id, null);
    }

    public static Map<String, Object> buildDialog(QBDialogType type, String id, List<Integer> occupants) {
        Map<String, Object> dialogMap = new HashMap<>();

        if (!CollectionUtils.isEmpty(occupants)) {
            dialogMap.put("occupantsIds", occupants);
        }

        if (!TextUtils.isEmpty(id)) {
            dialogMap.put("dialogId", id);
        }

        dialogMap.put("name", "Flutter Test Dialog " + System.currentTimeMillis());
        dialogMap.put("type", type.getCode());

        return dialogMap;
    }
}
