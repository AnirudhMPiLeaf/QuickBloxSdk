import 'dart:async';

import 'package:flutter/services.dart';
import 'package:quickblox_sdk/mappers/qb_filter_mapper.dart';
import 'package:quickblox_sdk/mappers/qb_sort_mapper.dart';
import 'package:quickblox_sdk/mappers/qb_user_mapper.dart';
import 'package:quickblox_sdk/models/qb_filter.dart';
import 'package:quickblox_sdk/models/qb_sort.dart';
import 'package:quickblox_sdk/models/qb_user.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class Users {
  const Users();

  ///////////////////////////////////////////////////////////////////////////
  // USERS MODULE
  ///////////////////////////////////////////////////////////////////////////

  //Channel name
  static const CHANNEL_NAME = "FlutterQBUsersChannel";

  //Methods
  static const CREATE_METHOD = "create";
  static const GET_METHOD = "getUsers";
  static const GET_METHOD_BY_TAG = "getUsersByTag";
  static const UPDATE_METHOD = "update";

  //Module
  static const _usersModule = const MethodChannel(CHANNEL_NAME);

  Future<QBUser?> createUser(String login, String password,
      {String? email,
      int? blobId,
      int? externalUserId,
      int? facebookId,
      int? twitterId,
      String? fullName,
      String? phone,
      String? website,
      String? customData,
      String? tagList}) async {
    Map<String, Object> data = Map();

    data["login"] = login;
    data["password"] = password;

    if (email != null) {
      data["email"] = email;
    }
    if (blobId != null) {
      data["blobId"] = blobId;
    }
    if (externalUserId != null) {
      data["externalUserId"] = externalUserId;
    }
    if (facebookId != null) {
      data["facebookId"] = facebookId;
    }
    if (twitterId != null) {
      data["twitterId"] = twitterId;
    }
    if (fullName != null) {
      data["fullName"] = fullName;
    }
    if (phone != null) {
      data["phone"] = phone;
    }
    if (website != null) {
      data["website"] = website;
    }
    if (customData != null) {
      data["customData"] = customData;
    }
    if (tagList != null) {
      data["tagList"] = tagList;
    }

    Map<Object?, Object?> map =
        await _usersModule.invokeMethod(CREATE_METHOD, data);

    QBUser? qbUser = QBUserMapper.mapToQBUser(map);

    return qbUser;
  }

  Future<List<QBUser?>> getUsers(
      {QBSort? sort, QBFilter? filter, int page = 1, int perPage = 100}) async {
    Map<String, Object> data = Map();

    data["page"] = page;
    data["perPage"] = perPage;

    if (sort != null) {
      data["sort"] = QBSortMapper.sortToMap(sort)!;
    }
    if (filter != null) {
      data["filter"] = QBFilterMapper.filterToMap(filter)!;
    }

    Map<Object?, Object?> map =
        await _usersModule.invokeMethod(GET_METHOD, data);

    List<Object?> list = map["users"] as List<Object?>;

    List<QBUser?> userList = [];

    for (final item in list) {
      QBUser? qbUser = QBUserMapper.mapToQBUser(item as Map<dynamic, dynamic>);
      userList.add(qbUser);
    }

    return userList;
  }

  Future<List<QBUser?>> getUsersByTag(List<String> tags,
      {int page = 1, int perPage = 100}) async {
    Map<String, Object> data = Map();

    data["tags"] = tags;
    data["page"] = page;
    data["perPage"] = perPage;

    Map<Object?, Object?> map =
        await _usersModule.invokeMethod(GET_METHOD_BY_TAG, data);

    List<Object?> list = map["users"] as List<Object?>;

    List<QBUser?> userList = [];

    for (final item in list) {
      QBUser? qbUser = QBUserMapper.mapToQBUser(item as Map<dynamic, dynamic>);
      userList.add(qbUser);
    }

    return userList;
  }

  Future<QBUser?> updateUser(
      {String? login,
      String? newPassword,
      String? password,
      String? email,
      int? blobId,
      int? externalUserId,
      int? facebookId,
      int? twitterId,
      String? fullName,
      String? phone,
      String? website,
      String? customData,
      String? tagList}) async {
    Map<String, Object> data = Map();

    if (login != null) {
      data["login"] = login;
    }
    if (newPassword != null) {
      data["newPassword"] = newPassword;
    }
    if (password != null) {
      data["password"] = password;
    }
    if (email != null) {
      data["email"] = email;
    }
    if (blobId != null) {
      data["blobId"] = blobId;
    }
    if (externalUserId != null) {
      data["externalUserId"] = externalUserId;
    }
    if (facebookId != null) {
      data["facebookId"] = facebookId;
    }
    if (twitterId != null) {
      data["twitterId"] = twitterId;
    }
    if (fullName != null) {
      data["fullName"] = fullName;
    }
    if (phone != null) {
      data["phone"] = phone;
    }
    if (website != null) {
      data["website"] = website;
    }
    if (customData != null) {
      data["customData"] = customData;
    }
    if (tagList != null) {
      data["tagList"] = tagList;
    }

    Map<Object?, Object?> map =
        await _usersModule.invokeMethod(UPDATE_METHOD, data);
    QBUser? qbUser = QBUserMapper.mapToQBUser(map);

    return qbUser;
  }
}
