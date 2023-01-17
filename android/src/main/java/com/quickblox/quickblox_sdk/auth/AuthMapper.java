package com.quickblox.quickblox_sdk.auth;

import android.text.TextUtils;

import com.quickblox.auth.session.QBSession;
import com.quickblox.quickblox_sdk.utils.DateUtil;
import com.quickblox.users.model.QBUser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class AuthMapper {

    private AuthMapper() {
        //empty
    }

    static Map<String, Object> qbUserToMap(QBUser user) {
        Map<String, Object> map = new HashMap<>();

        if (user.getFileId() != null && user.getFileId() > 0) {
            map.put("blobId", user.getFileId());
        }
        if (!TextUtils.isEmpty(user.getCustomData())) {
            map.put("customData", user.getCustomData());
        }
        if (!TextUtils.isEmpty(user.getEmail())) {
            map.put("email", user.getEmail());
        }
        if (!TextUtils.isEmpty(user.getExternalId())) {
            map.put("externalId", user.getExternalId());
        }
        if (!TextUtils.isEmpty(user.getFacebookId())) {
            map.put("facebookId", user.getFacebookId());
        }
        if (!TextUtils.isEmpty(user.getFullName())) {
            map.put("fullName", user.getFullName());
        }
        if (user.getId() != null && user.getId() != 0) {
            map.put("id", user.getId());
        }
        if (!TextUtils.isEmpty(user.getLogin())) {
            map.put("login", user.getLogin());
        }
        if (!TextUtils.isEmpty(user.getPhone())) {
            map.put("phone", user.getPhone());
        }
        if (user.getTags() != null && user.getTags().size() > 0) {
            map.put("tags", user.getTags());
        }
        if (!TextUtils.isEmpty(user.getTwitterId())) {
            map.put("twitterId", user.getTwitterId());
        }
        if (!TextUtils.isEmpty(user.getWebsite())) {
            map.put("website", user.getWebsite());
        }
        if (user.getLastRequestAt() != null) {
            Date date = user.getLastRequestAt();
            String lastRequestAt = DateUtil.convertDateToISO(date);
            map.put("lastRequestAt", lastRequestAt);
        }

        return map;
    }

    static Map<String, Object> qbSessionToMap(QBSession session) {
        Map<String, Object> map = new HashMap<>();

        if (!TextUtils.isEmpty(session.getToken())) {
            map.put("token", session.getToken());
        }

        if (session.getTokenExpirationDate() != null) {
            Date tokenExpirationDate = session.getTokenExpirationDate();
            String expirationDate = DateUtil.convertDateToISO(tokenExpirationDate);
            map.put("expirationDate", expirationDate);
        }

        if (session.getUserId() != 0) {
            map.put("userId", session.getUserId());
        }

        if (session.getAppId() != null) {
            map.put("applicationId", session.getAppId());
        }

        return map;
    }
}