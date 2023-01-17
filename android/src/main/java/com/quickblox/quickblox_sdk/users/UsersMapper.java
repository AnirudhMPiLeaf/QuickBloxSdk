package com.quickblox.quickblox_sdk.users;

import android.text.TextUtils;

import com.quickblox.quickblox_sdk.utils.DateUtil;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class UsersMapper {

    private UsersMapper() {
        //empty
    }

    static Map qbUserToMap(QBUser user) {
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

    static String sortMapToSort(Map sortMap) {
        String sort = null;

        if (sortMap == null) {
            return sort;
        }

        boolean ascendingValue = sortMap.containsKey("ascending") && (boolean) sortMap.get("ascending");
        String sortAscDesc = UsersConstants.getAscDesc(ascendingValue);

        String sortField = sortMap.containsKey("field") ? (String) sortMap.get("field") : null;

        String sortType = sortMap.containsKey("type") ? (String) sortMap.get("type") : null;

        if (!TextUtils.isEmpty(sortAscDesc) && !TextUtils.isEmpty(sortType) && !TextUtils.isEmpty(sortField)) {
            sort = sortAscDesc + " " + sortType + " " + sortField;
        }

        return sort;
    }

    static String filterMapToFilter(Map filterMap) {
        if (filterMap == null) {
            return null;
        }

        String filter = null;

        String filterType = filterMap.containsKey("type") ? (String) filterMap.get("type") : "";
        String filterField = filterMap.containsKey("field") ? (String) filterMap.get("field") : "";
        String filterOperator = filterMap.containsKey("operator") ? (String) filterMap.get("operator") : "";

        if (!TextUtils.isEmpty(filterType) && !TextUtils.isEmpty(filterField) && !TextUtils.isEmpty(filterOperator)) {
            filter = filterType + " " + filterField + " " + filterOperator + " ";
        }

        return filter;
    }

    static ArrayList getFilterValueFromMap(Map filterMap) {
        ArrayList filterValue = null;
        if (filterMap != null && filterMap.containsKey("value")) {
            try {
                String filter = (String) filterMap.get("value");
                String[] filterArray = TextUtils.split(filter, ",");
                filterValue = new ArrayList(Arrays.asList(filterArray));
            } catch (ClassCastException e) {
                //ignore
            }
        }
        return filterValue;
    }
}