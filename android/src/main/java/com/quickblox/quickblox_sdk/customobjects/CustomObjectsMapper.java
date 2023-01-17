package com.quickblox.quickblox_sdk.customobjects;

import android.text.TextUtils;

import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.core.request.QBRequestUpdateBuilder;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.customobjects.model.QBPermissions;
import com.quickblox.customobjects.model.QBPermissionsLevel;
import com.quickblox.quickblox_sdk.utils.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class CustomObjectsMapper {

    private CustomObjectsMapper() {
        //empty
    }

    static Map<String, Object> qbCustomObjectToMap(QBCustomObject customObject) {
        Map<String, Object> map = new HashMap<>();

        if (!TextUtils.isEmpty(customObject.getCustomObjectId())) {
            map.put("id", customObject.getCustomObjectId());
        }
        if (!TextUtils.isEmpty(customObject.getParentId()) && !customObject.getParentId().equals("null")) {
            map.put("parentId", customObject.getParentId());
        }
        if (customObject.getCreatedAt() != null) {
            Date date = customObject.getCreatedAt();
            String createdAt = DateUtil.convertDateToISO(date);
            map.put("createdAt", createdAt);
        }
        if (customObject.getUpdatedAt() != null) {
            Date date = customObject.getUpdatedAt();
            String updatedAt = DateUtil.convertDateToISO(date);
            map.put("updatedAt", updatedAt);
        }
        if (!TextUtils.isEmpty(customObject.getClassName())) {
            map.put("className", customObject.getClassName());
        }
        if (customObject.getUserId() != null && customObject.getUserId() > 0) {
            map.put("userId", customObject.getUserId());
        }
        if (customObject.getFields() != null && customObject.getFields().size() > 0) {
            HashMap<String, Object> fields = customObject.getFields();
            Map fieldsMap = getPropertiesMap(fields);
            map.put("fields", fieldsMap);
        }
        if (customObject.getPermission() != null) {
            QBPermissions permissions = customObject.getPermission();
            Map permissionMap = qbPermissionsToMap(permissions);
            map.put("permission", permissionMap);
        }

        return map;
    }

    private static Map getPropertiesMap(HashMap<String, Object> fields) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();

            if (fieldName != null && fieldValue != null && !fieldValue.toString().equals("null")) {
                map.put(fieldName, fieldValue);
            }
        }
        return map;
    }

    private static Map qbPermissionsToMap(QBPermissions permissions) {
        Map<String, Object> map = new HashMap<>();

        if (!TextUtils.isEmpty(permissions.getCustomObjectId())) {
            map.put("customObjectId", permissions.getCustomObjectId());
        }
        if (permissions.getReadLevel() != null) {
            Map readLevel = qbPermissionsLevelToMap(permissions.getDeleteLevel());
            map.put("readLevel", readLevel);
        }
        if (permissions.getUpdateLevel() != null) {
            Map updateLevel = qbPermissionsLevelToMap(permissions.getUpdateLevel());
            map.put("updateLevel", updateLevel);
        }
        if (permissions.getDeleteLevel() != null) {
            Map deleteLevel = qbPermissionsLevelToMap(permissions.getDeleteLevel());
            map.put("deleteLevel", deleteLevel);
        }

        return map;
    }

    private static Map qbPermissionsLevelToMap(QBPermissionsLevel permissionsLevel) {
        Map<String, Object> map = new HashMap<>();

        if (!TextUtils.isEmpty(permissionsLevel.getAccess())) {
            String access = permissionsLevel.getAccess();
            map.put("access", access);
        }
        if (permissionsLevel.getUsersIds() != null && permissionsLevel.getUsersIds().size() > 0) {
            ArrayList<String> userIdsList = permissionsLevel.getUsersIds();
            map.put("usersIds", userIdsList);
        }
        if (permissionsLevel.usersGroups != null && permissionsLevel.usersGroups.size() > 0) {
            ArrayList<String> usersGroupsList = permissionsLevel.usersGroups;
            map.put("usersGroups", usersGroupsList);
        }

        return map;
    }

    static QBCustomObject buildCustomObjectFromMap(Map<String, Object> fields) {
        QBCustomObject customObject = new QBCustomObject();

        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            customObject.put(fieldName, fieldValue);
        }

        return customObject;
    }

    static QBRequestGetBuilder addFilterToRequestBuilder(QBRequestGetBuilder requestGetBuilder, Map filterMap) {
        if (filterMap != null) {
            String fieldName = filterMap.containsKey("field") ? (String) filterMap.get("field") : null;
            String filterOperator = filterMap.containsKey("operator") ? (String) filterMap.get("operator") : null;
            String fieldValue = filterMap.containsKey("value") ? (String) filterMap.get("value") : null;

            if (TextUtils.isEmpty(fieldName) || TextUtils.isEmpty(fieldValue) || TextUtils.isEmpty(filterOperator)) {
                return requestGetBuilder;
            }

            if (filterOperator.equals(CustomObjectsConstants.IntegerSearchTypes.LT)) {
                requestGetBuilder.lt(fieldName, fieldValue);
            } else if (filterOperator.equals(CustomObjectsConstants.IntegerSearchTypes.LTE)) {
                requestGetBuilder.lte(fieldName, fieldValue);
            } else if (filterOperator.equals(CustomObjectsConstants.IntegerSearchTypes.GT)) {
                requestGetBuilder.gt(fieldName, fieldValue);
            } else if (filterOperator.equals(CustomObjectsConstants.IntegerSearchTypes.NIN)) {
                requestGetBuilder.nin(fieldName, fieldValue);
            } else if (filterOperator.equals(CustomObjectsConstants.FloatSearchTypes.GTE)) {
                requestGetBuilder.gte(fieldName, fieldValue);
            } else if (filterOperator.equals(CustomObjectsConstants.StringSearchTypes.NE)) {
                requestGetBuilder.ne(fieldName, fieldValue);
            } else if (filterOperator.equals(CustomObjectsConstants.StringSearchTypes.IN)) {
                requestGetBuilder.in(fieldName, fieldValue);
            } else if (filterOperator.equals(CustomObjectsConstants.StringSearchTypes.OR)) {
                requestGetBuilder.or(fieldName, fieldValue);
            } else if (filterOperator.equals(CustomObjectsConstants.StringSearchTypes.CTN)) {
                requestGetBuilder.ctn(fieldName, fieldValue);
            } else if (filterOperator.equals(CustomObjectsConstants.ArraySearchTypes.ALL)) {
                requestGetBuilder.all(fieldName, fieldValue);
            }
        }

        return requestGetBuilder;
    }

    static QBRequestGetBuilder addSortToRequestBuilder(QBRequestGetBuilder requestGetBuilder, Map sortMap) {
        if (sortMap != null) {
            String fieldValue = sortMap.containsKey("field") ? (String) sortMap.get("field") : null;

            boolean ascendingValue = sortMap.containsKey("ascending") && (boolean) sortMap.get("ascending");
            if (ascendingValue) {
                requestGetBuilder.sortAsc(fieldValue);
            } else {
                requestGetBuilder.sortDesc(fieldValue);
            }
        }

        return requestGetBuilder;
    }

    static QBRequestUpdateBuilder addFieldsToUpdateBuilder(QBRequestUpdateBuilder requestBuilder,
                                                           String fieldName, HashMap<String, Object> fieldValues) {
        Object value = null;
        Integer index = null;
        String operator = null;
        String pullFilter = null;

        for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
            String fieldKey = entry.getKey();
            Object fieldValue = entry.getValue();

            if (fieldKey.equals("value")) {
                value = fieldValue;
            }
            if (fieldKey.equals("index")) {
                index = (Integer) fieldValue;
            }
            if (fieldKey.equals("operator")) {
                operator = (String) fieldValue;
            }
            if (fieldKey.equals("pullFilter")) {
                pullFilter = (String) fieldValue;
            }
        }

        if (index != null) {
            requestBuilder.updateArrayValue(fieldName, index, value);
        }
        if (!TextUtils.isEmpty(operator)) {
            addOperator(requestBuilder, operator, fieldName, value);
        }
        if (!TextUtils.isEmpty(pullFilter)) {
            addPullFilter(requestBuilder, pullFilter, fieldName, value);
        }

        return requestBuilder;
    }

    private static QBRequestUpdateBuilder addOperator(QBRequestUpdateBuilder requestBuilder,
                                                      String operator, String fieldName, Object fieldValue) {

        if (operator.equals(CustomObjectsConstants.IntegerUpdateTypes.INC)) {
            requestBuilder.inc(fieldName, fieldValue);
        } else if (operator.equals(CustomObjectsConstants.ArrayUpdateTypes.ADD_TO_SET)) {
            requestBuilder.addToSet(fieldName, fieldValue);
        } else if (operator.equals(CustomObjectsConstants.ArrayUpdateTypes.POP)) {
            requestBuilder.pop(fieldName, fieldValue);
        } else if (operator.equals(CustomObjectsConstants.ArrayUpdateTypes.PULL)) {
            requestBuilder.pull(fieldName, fieldValue);
        } else if (operator.equals(CustomObjectsConstants.ArrayUpdateTypes.PULL_ALL)) {
            requestBuilder.pullAll(fieldName, fieldValue);
        } else if (operator.equals(CustomObjectsConstants.ArrayUpdateTypes.PUSH)) {
            requestBuilder.push(fieldName, fieldValue);
        }

        return requestBuilder;
    }

    private static QBRequestUpdateBuilder addPullFilter(QBRequestUpdateBuilder requestBuilder,
                                                        String pullFilter, String fieldName, Object fieldValue) {

        requestBuilder.addRule(fieldName, "[" + pullFilter.toLowerCase() + "]", fieldValue);
        return requestBuilder;
    }
}