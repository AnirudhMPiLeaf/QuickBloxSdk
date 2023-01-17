import 'dart:async';
import 'dart:collection';

import 'package:flutter/services.dart';
import 'package:quickblox_sdk/mappers/qb_custom_object_mapper.dart';
import 'package:quickblox_sdk/mappers/qb_filter_mapper.dart';
import 'package:quickblox_sdk/mappers/qb_sort_mapper.dart';
import 'package:quickblox_sdk/models/qb_custom_object.dart';
import 'package:quickblox_sdk/models/qb_filter.dart';
import 'package:quickblox_sdk/models/qb_sort.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class CustomObjects {
  const CustomObjects();

  ///////////////////////////////////////////////////////////////////////////
  // CUSTOM OBJECTS MODULE
  ///////////////////////////////////////////////////////////////////////////

  //Channel name
  static const CHANNEL_NAME = "FlutterQBCustomObjectsChannel";

  //Methods
  static const CREATE_METHOD = "create";
  static const REMOVE_METHOD = "remove";
  static const GET_BY_IDS_METHOD = "getByIds";
  static const GET_METHOD = "get";
  static const UPDATE_METHOD = "update";

  //Module
  static const _customObjectsModule = const MethodChannel(CHANNEL_NAME);

  Future<List<QBCustomObject?>> create(
      {String? className, Map<String, Object>? fields, List<Map<String, Object>>? objects}) async {
    Map<String, Object> data = Map();

    if (className != null) {
      data["className"] = className;
    }
    if (fields != null) {
      data["fields"] = fields;
    }
    if (objects != null) {
      data["objects"] = objects;
    }

    Object? result = await _customObjectsModule.invokeMethod(CREATE_METHOD, data);

    List<QBCustomObject?> customObjects = [];

    if (result is LinkedHashMap) {
      QBCustomObject? customObject = QBCustomObjectMapper.mapToQBCustomObject(result);
      customObjects.add(customObject);
    }

    if (result is List) {
      for (final item in result) {
        QBCustomObject? customObject = QBCustomObjectMapper.mapToQBCustomObject(item as Map<dynamic, dynamic>);
        customObjects.add(customObject);
      }
    }

    return customObjects;
  }

  Future<void> remove(String className, List<String> ids) async {
    Map<String, Object> data = Map();

    data["className"] = className;
    data["ids"] = ids;

    return await _customObjectsModule.invokeMethod(REMOVE_METHOD, data);
  }

  Future<List<QBCustomObject?>> getByIds(String className, List<String> ids) async {
    Map<String, Object> data = Map();

    data["className"] = className;
    data["objectsIds"] = ids;

    List<Object?> list = await _customObjectsModule.invokeMethod(GET_BY_IDS_METHOD, data);

    List<QBCustomObject?> customObjects = [];

    for (final item in list) {
      QBCustomObject? customObject = QBCustomObjectMapper.mapToQBCustomObject(item as Map<dynamic, dynamic>);
      customObjects.add(customObject);
    }

    return customObjects;
  }

  Future<List<QBCustomObject?>> get(String className,
      {QBSort? sort,
      QBFilter? filter,
      int limit = 100,
      int skip = 0,
      List<String>? exclude,
      List<String>? include}) async {
    Map<String, Object> data = Map();

    data["className"] = className;
    data["limit"] = limit;
    data["skip"] = skip;

    if (sort != null) {
      data["sort"] = QBSortMapper.sortToMap(sort)!;
    }
    if (filter != null) {
      data["filter"] = QBFilterMapper.filterToMap(filter)!;
    }

    if (include != null) {
      data["include"] = include;
    }
    if (exclude != null) {
      data["exclude"] = exclude;
    }

    List<Object?> list = await _customObjectsModule.invokeMethod(GET_METHOD, data);

    List<QBCustomObject?> customObjects = [];

    for (final item in list) {
      QBCustomObject? customObject = QBCustomObjectMapper.mapToQBCustomObject(item as Map<dynamic, dynamic>);
      customObjects.add(customObject);
    }

    return customObjects;
  }

  Future<List<QBCustomObject?>> update(String className,
      {String? id, Map<String, Object>? fields, List<Map<String, Object>?>? objects}) async {
    Map<String, Object> data = Map();

    data["className"] = className;

    if (id != null) {
      data["id"] = id;
    }
    if (fields != null) {
      data["fields"] = fields;
    }
    if (objects != null) {
      data["objects"] = objects;
    }

    Object? result = await _customObjectsModule.invokeMethod(UPDATE_METHOD, data);

    List<QBCustomObject?> customObjects = [];

    if (result is LinkedHashMap) {
      QBCustomObject? customObject = QBCustomObjectMapper.mapToQBCustomObject(result);
      customObjects.add(customObject);
    }

    if (result is List) {
      for (final item in result) {
        QBCustomObject? customObject = QBCustomObjectMapper.mapToQBCustomObject(item as Map<dynamic, dynamic>);
        customObjects.add(customObject);
      }
    }

    return customObjects;
  }
}
