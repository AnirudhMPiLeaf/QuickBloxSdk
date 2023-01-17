package com.quickblox.quickblox_sdk.file;

import android.text.TextUtils;

import com.quickblox.content.model.QBFile;
import com.quickblox.quickblox_sdk.utils.DateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class FileMapper {

    private FileMapper() {
        //empty
    }

    static Map qbFileToMap(QBFile file) {
        Map<String, Object> map = new HashMap<>();

        if (file.getId() != null && file.getId() > 0) {
            map.put("id", file.getId());
        }
        if (!TextUtils.isEmpty(file.getUid())) {
            map.put("uid", file.getUid());
        }
        if (!TextUtils.isEmpty(file.getContentType())) {
            map.put("contentType", file.getContentType());
        }
        if (!TextUtils.isEmpty(file.getName())) {
            map.put("name", file.getName());
        }
        if (file.getSize() > 0) {
            map.put("size", file.getSize());
        }
        if (file.getCompletedAt() != null && !TextUtils.isEmpty(file.getCompletedAt().toString())) {
            Date date = file.getCompletedAt();
            String completedAt = DateUtil.convertDateToISO(date);
            map.put("completedAt", completedAt);
        }
        if (file.isPublic() != null) {
            map.put("isPublic", file.isPublic());
        }
        if (file.getLastReadAccessTime() != null && !TextUtils.isEmpty(file.getLastReadAccessTime().toString())) {
            Date date = file.getLastReadAccessTime();
            String lastReadAccessTime = DateUtil.convertDateToISO(date);
            map.put("lastReadAccessTime", lastReadAccessTime);
        }
        if (!TextUtils.isEmpty(file.getTags())) {
            map.put("tags", file.getTags());
        }

        return map;
    }
}