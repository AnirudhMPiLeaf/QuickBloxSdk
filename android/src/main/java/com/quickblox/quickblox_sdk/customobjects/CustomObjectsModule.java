
package com.quickblox.quickblox_sdk.customobjects;

import android.os.Bundle;
import android.text.TextUtils;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.core.request.QBRequestUpdateBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.quickblox_sdk.base.BaseModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
public class CustomObjectsModule implements BaseModule {
    private static final String CHANNEL_NAME = "FlutterQBCustomObjectsChannel";

    private static final String CREATE_METHOD = "create";
    private static final String REMOVE_METHOD = "remove";
    private static final String GET_BY_IDS_METHOD = "getByIds";
    private static final String GET_METHOD = "get";
    private static final String UPDATE_METHOD = "update";

    @Override
    public void initEventHandler() {

    }

    @Override
    public String getChannelName() {
        return CHANNEL_NAME;
    }

    @Override
    public MethodChannel.MethodCallHandler getMethodHandler() {
        return this::handleMethod;
    }

    @Override
    public void handleMethod(MethodCall methodCall, MethodChannel.Result result) {
        Map<String, Object> data = methodCall.arguments();
        switch (methodCall.method) {
            case CREATE_METHOD:
                create(data, result);
                break;
            case REMOVE_METHOD:
                remove(data, result);
                break;
            case GET_BY_IDS_METHOD:
                getByIds(data, result);
                break;
            case GET_METHOD:
                get(data, result);
                break;
            case UPDATE_METHOD:
                update(data, result);
                break;
        }
    }

    private void create(Map data, final MethodChannel.Result result) {
        String className = data != null && data.containsKey("className") ? (String) data.get("className") : null;
        Map<String, Object> fields = data != null && data.containsKey("fields") ? (Map) data.get("fields") : null;
        List<Map<String, Object>> objects = data != null && data.containsKey("objects") ? (List<Map<String, Object>>) data.get("objects") : null;

        if (fields != null && fields.size() > 0) {
            QBCustomObject customObject = CustomObjectsMapper.buildCustomObjectFromMap(fields);
            if (!TextUtils.isEmpty(className)) {
                customObject.setClassName(className);
            }

            QBCustomObjects.createObject(customObject).performAsync(new QBEntityCallback<QBCustomObject>() {
                @Override
                public void onSuccess(QBCustomObject customObject, Bundle bundle) {
                    Map<String, Object> customObjectMap = CustomObjectsMapper.qbCustomObjectToMap(customObject);
                    result.success(customObjectMap);
                }

                @Override
                public void onError(QBResponseException e) {
                    result.error(e.getMessage(), null, null);
                }
            });
        } else if (objects != null && objects.size() > 0) {
            List<QBCustomObject> qbCustomObjects = new ArrayList<>();
            for (Object object : objects) {
                HashMap<String, Object> map = (HashMap<String, Object>) object;
                QBCustomObject customObject = CustomObjectsMapper.buildCustomObjectFromMap(map);
                if (!TextUtils.isEmpty(className)) {
                    customObject.setClassName(className);
                }
                qbCustomObjects.add(customObject);
            }
            QBCustomObjects.createObjects(qbCustomObjects).performAsync(new QBEntityCallback<ArrayList<QBCustomObject>>() {
                @Override
                public void onSuccess(ArrayList<QBCustomObject> qbCustomObjects, Bundle bundle) {
                    List<Map> customObjectsList = new ArrayList<>();
                    for (QBCustomObject customObject : qbCustomObjects) {
                        Map customObjectMap = CustomObjectsMapper.qbCustomObjectToMap(customObject);
                        customObjectsList.add(customObjectMap);
                    }
                    result.success(customObjectsList);
                }

                @Override
                public void onError(QBResponseException e) {
                    result.error(e.getMessage(), null, null);
                }
            });
        } else {
            result.error("The objects or fields are required parameters", null, null);
        }
    }

    private void remove(Map data, final MethodChannel.Result result) {
        String className = data != null && data.containsKey("className") ? (String) data.get("className") : null;
        List<String> idsArray = data != null && data.containsKey("ids") ? (List<String>) data.get("ids") : null;

        if (TextUtils.isEmpty(className) || idsArray == null || idsArray.size() == 0) {
            result.error("The className and ids are required parameters", null, null);
            return;
        }

        ArrayList arrayList = new ArrayList<>(idsArray);

        StringifyArrayList<String> idsList = new StringifyArrayList<>();
        for (Object item : arrayList) {
            String customObjectId = (String) item;
            idsList.add(customObjectId);
        }

        QBCustomObjects.deleteObjects(className, idsList).performAsync(new QBEntityCallback<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> ids, Bundle bundle) {
                result.success(null);
            }

            @Override
            public void onError(QBResponseException e) {
                result.error(e.getMessage(), null, null);
            }
        });
    }

    private void getByIds(Map data, final MethodChannel.Result result) {
        String className = data != null && data.containsKey("className") ? (String) data.get("className") : null;
        List<String> objectsIds = data != null && data.containsKey("objectsIds") ? (List<String>) data.get("objectsIds") : null;

        if (TextUtils.isEmpty(className) || objectsIds == null || objectsIds.size() == 0) {
            result.error("The className and objectsIds are required parameters", null, null);
            return;
        }

        StringifyArrayList<String> idsList = new StringifyArrayList<>();
        idsList.addAll(objectsIds);

        QBCustomObjects.getObjectsByIds(className, idsList).performAsync(new QBEntityCallback<ArrayList<QBCustomObject>>() {
            @Override
            public void onSuccess(ArrayList<QBCustomObject> qbCustomObjects, Bundle bundle) {
                List<Map> array = new ArrayList<>();
                for (QBCustomObject customObject : qbCustomObjects) {
                    Map map = CustomObjectsMapper.qbCustomObjectToMap(customObject);
                    array.add(map);
                }
                result.success(array);
            }

            @Override
            public void onError(QBResponseException e) {
                result.error(e.getMessage(), null, null);
            }
        });
    }

    private void get(Map data, final MethodChannel.Result result) {
        String className = data != null && data.containsKey("className") ? (String) data.get("className") : null;
        Map sortMap = data != null && data.containsKey("sort") ? (Map) data.get("sort") : null;
        Map filterMap = data != null && data.containsKey("filter") ? (Map) data.get("filter") : null;
        int limit = data != null && data.containsKey("limit") ? (int) data.get("limit") : 100;
        int skip = data != null && data.containsKey("skip") ? (int) data.get("skip") : 0;
        List<String> include = data != null && data.containsKey("include") ? (List<String>) data.get("include") : null;
        List<String> exclude = data != null && data.containsKey("exclude") ? (List<String>) data.get("exclude") : null;

        if (TextUtils.isEmpty(className)) {
            result.error("The className is required parameter", null, null);
            return;
        }

        QBRequestGetBuilder requestGetBuilder = new QBRequestGetBuilder();
        requestGetBuilder.setLimit(limit);
        requestGetBuilder.setSkip(skip);

        CustomObjectsMapper.addFilterToRequestBuilder(requestGetBuilder, filterMap);
        CustomObjectsMapper.addSortToRequestBuilder(requestGetBuilder, sortMap);

        if (include != null && include.size() > 0) {
            requestGetBuilder.outputInclude(include);
        }

        if (exclude != null && exclude.size() > 0) {
            requestGetBuilder.outputExclude(exclude);
        }

        QBCustomObjects.getObjects(className, requestGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBCustomObject>>() {
            @Override
            public void onSuccess(ArrayList<QBCustomObject> qbCustomObjects, Bundle bundle) {
                List<Map> array = new ArrayList<>();
                for (QBCustomObject customObject : qbCustomObjects) {
                    Map map = CustomObjectsMapper.qbCustomObjectToMap(customObject);
                    array.add(map);
                }
                result.success(array);
            }

            @Override
            public void onError(QBResponseException e) {
                result.error(e.getMessage(), null, null);
            }
        });
    }

    private void update(Map data, final MethodChannel.Result result) {
        String className = data != null && data.containsKey("className") ? (String) data.get("className") : null;
        String customObjectId = data != null && data.containsKey("id") ? (String) data.get("id") : null;
        Map<String, Object> fields = data != null && data.containsKey("fields") ? (Map) data.get("fields") : null;
        List<Map<String, Object>> objects = data != null && data.containsKey("objects") ? (List) data.get("objects") : null;

        if (TextUtils.isEmpty(className)) {
            result.error("The className is required parameters", null, null);
            return;
        }

        if (objects != null && objects.size() > 0) {
            List<QBCustomObject> customObjectList = new ArrayList<>();
            for (int index = 0; index < objects.size(); index++) {

                // TODO: 2019-10-21
                Map objectsMap = objects.get(index);
                String objectId = objectsMap != null && objectsMap.containsKey("id") ? (String) objectsMap.get("id") : null;

                Map<String, Object> objectsFields = objectsMap != null && objectsMap.containsKey("fields") ? (Map) objectsMap.get("fields") : null;

                QBCustomObject qbCustomObject = new QBCustomObject();
                qbCustomObject.setCustomObjectId(objectId);
                qbCustomObject.setClassName(className);

                if (TextUtils.isEmpty(objectId) || objectsFields == null) {
                    result.error("The id and fields parameter is required if you send objects parameter", null, null);
                    return;
                }

                for (Map.Entry<String, Object> entry : objectsFields.entrySet()) {
                    String fieldName = entry.getKey();
                    Object fieldValue = entry.getValue();
                    qbCustomObject.put(fieldName, fieldValue);
                }

                customObjectList.add(qbCustomObject);
            }
            updateObjects(customObjectList, result);
        } else {
            QBCustomObject qbCustomObject = new QBCustomObject();
            qbCustomObject.setCustomObjectId(customObjectId);
            qbCustomObject.setClassName(className);

            QBRequestUpdateBuilder requestBuilder = new QBRequestUpdateBuilder();

            if (fields != null && fields.size() > 0) {
                for (Map.Entry<String, Object> entry : fields.entrySet()) {
                    String fieldName = entry.getKey();
                    Object fieldValue = entry.getValue();

                    if (fieldValue instanceof Map) {
                        HashMap<String, Object> fieldValues = (HashMap<String, Object>) fieldValue;
                        CustomObjectsMapper.addFieldsToUpdateBuilder(requestBuilder, fieldName, fieldValues);
                    } else {
                        qbCustomObject.put(fieldName, fieldValue);
                    }
                }
            }
            updateObject(qbCustomObject, requestBuilder, result);
        }
    }

    private void updateObject(QBCustomObject customObject, QBRequestUpdateBuilder requestBuilder, final MethodChannel.Result result) {
        QBCustomObjects.updateObject(customObject, requestBuilder).performAsync(new QBEntityCallback<QBCustomObject>() {
            @Override
            public void onSuccess(QBCustomObject customObject, Bundle bundle) {
                Map map = CustomObjectsMapper.qbCustomObjectToMap(customObject);
                result.success(map);
            }

            @Override
            public void onError(QBResponseException e) {
                result.error(e.getMessage(), null, null);
            }
        });
    }

    private void updateObjects(List<QBCustomObject> customObjectList, final MethodChannel.Result result) {
        QBCustomObjects.updateObjects(customObjectList).performAsync(new QBEntityCallback<ArrayList<QBCustomObject>>() {
            @Override
            public void onSuccess(ArrayList<QBCustomObject> qbCustomObjects, Bundle bundle) {
                List<Map> customObjectsArray = new ArrayList<>();
                for (QBCustomObject customObject : qbCustomObjects) {
                    Map customObjectMap = CustomObjectsMapper.qbCustomObjectToMap(customObject);
                    customObjectsArray.add(customObjectMap);
                }
                result.success(customObjectsArray);
            }

            @Override
            public void onError(QBResponseException e) {
                result.error(e.getMessage(), null, null);
            }
        });
    }
}