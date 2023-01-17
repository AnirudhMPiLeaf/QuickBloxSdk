import 'dart:async';
import 'dart:collection';

import 'package:flutter/services.dart';
import 'package:quickblox_sdk/mappers/qb_attachment_mapper.dart';
import 'package:quickblox_sdk/mappers/qb_dialog_mapper.dart';
import 'package:quickblox_sdk/mappers/qb_filter_mapper.dart';
import 'package:quickblox_sdk/mappers/qb_message_mapper.dart';
import 'package:quickblox_sdk/mappers/qb_messages_counter_mapper.dart';
import 'package:quickblox_sdk/mappers/qb_sort_mapper.dart';
import 'package:quickblox_sdk/models/qb_attachment.dart';
import 'package:quickblox_sdk/models/qb_dialog.dart';
import 'package:quickblox_sdk/models/qb_filter.dart';
import 'package:quickblox_sdk/models/qb_message.dart';
import 'package:quickblox_sdk/models/qb_sort.dart';

import '../models/qb_messages_counter.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class Chat {
  const Chat();

  ///////////////////////////////////////////////////////////////////////////
  // CHAT MODULE
  ///////////////////////////////////////////////////////////////////////////

  //Channel name
  static const CHANNEL_NAME = "FlutterQBChatChannel";

  //Methods
  static const CONNECT_METHOD = "connect";
  static const DISCONNECT_METHOD = "disconnect";
  static const IS_CONNECTED_METHOD = "isConnected";
  static const PING_SERVER_METHOD = "pingServer";
  static const PING_USER_METHOD = "pingUser";
  static const GET_DIALOGS_METHOD = "getDialogs";
  static const GET_DIALOGS_COUNT_METHOD = "getDialogsCount";
  static const UPDATE_DIALOG_METHOD = "updateDialog";
  static const CREATE_DIALOG_METHOD = "createDialog";
  static const DELETE_DIALOG_METHOD = "deleteDialog";
  static const LEAVE_DIALOG_METHOD = "leaveDialog";
  static const JOIN_DIALOG_METHOD = "joinDialog";
  static const IS_JOINED_DIALOG_METHOD = "isJoinedDialog";
  static const GET_ONLINE_USERS_METHOD = "getOnlineUsers";
  static const SEND_MESSAGE_METHOD = "sendMessage";
  static const SEND_SYSTEM_MESSAGE_METHOD = "sendSystemMessage";
  static const MARK_MESSAGE_READ_METHOD = "markMessageRead";
  static const SEND_IS_TYPING_METHOD = "sendIsTyping";
  static const SEND_STOPPED_TYPING_METHOD = "sendStoppedTyping";
  static const MARK_MESSAGE_DELIVERED_METHOD = "markMessageDelivered";
  static const GET_DIALOG_MESSAGES_METHOD = "getDialogMessages";
  static const GET_TOTAL_UNREAD_MESSAGES_COUNT_METHOD = "getTotalUnreadMessagesCount";

  //Module
  static const _chatModule = const MethodChannel(CHANNEL_NAME);

  Future<void> connect(int userId, String password) async {
    Map<String, Object> data = Map();

    data["userId"] = userId;
    data["password"] = password;

    await _chatModule.invokeMethod(CONNECT_METHOD, data);
  }

  Future<void> disconnect() async {
    await _chatModule.invokeMethod(DISCONNECT_METHOD);
  }

  Future<bool?> isConnected() async {
    return _chatModule.invokeMethod(IS_CONNECTED_METHOD);
  }

  Future<bool> pingServer() async {
    return await _chatModule.invokeMethod(PING_SERVER_METHOD);
  }

  Future<bool> pingUser(int userId) async {
    Map<String, Object> data = Map();

    data["userId"] = userId;

    return await _chatModule.invokeMethod(PING_USER_METHOD, data);
  }

  Future<List<QBDialog?>> getDialogs(
      {QBSort? sort, QBFilter? filter, int? limit, int? skip}) async {
    Map<String, Object> data = Map();

    if (sort != null) {
      data["sort"] = QBSortMapper.sortToMap(sort)!;
    }
    if (filter != null) {
      data["filter"] = QBFilterMapper.filterToMap(filter)!;
    }
    if (limit != null) {
      data["limit"] = limit;
    }
    if (skip != null) {
      data["skip"] = skip;
    }

    Map<Object?, Object?> map = await _chatModule.invokeMethod(GET_DIALOGS_METHOD, data);

    List<Object?> list = map["dialogs"] as List<Object?>;

    List<QBDialog?> dialogList = [];

    for (final dialogMap in list) {
      QBDialog? qbDialog = QBDialogMapper.mapToQBDialog(dialogMap as Map<Object?, Object?>);
      dialogList.add(qbDialog);
    }

    return dialogList;
  }

  Future<int?> getDialogsCount({QBFilter? qbFilter, int? limit, int? skip}) async {
    Map<String, Object> data = Map();

    if (qbFilter != null) {
      data["filter"] = QBFilterMapper.filterToMap(qbFilter)!;
    }
    if (limit != null) {
      data["limit"] = limit;
    }
    if (skip != null) {
      data["skip"] = skip;
    }

    return await _chatModule.invokeMethod(GET_DIALOGS_COUNT_METHOD, data);
  }

  Future<QBDialog?> updateDialog(String dialogId,
      {List<int>? addUsers,
      List<int>? removeUsers,
      String? dialogName,
      String? dialogPhoto,
      Map<String, Object>? customData}) async {
    Map<String, Object> data = Map();

    data["dialogId"] = dialogId;

    if (addUsers != null) {
      data["addUsers"] = addUsers;
    }
    if (removeUsers != null) {
      data["removeUsers"] = removeUsers;
    }
    if (dialogName != null) {
      data["name"] = dialogName;
    }
    if (dialogPhoto != null) {
      data["photo"] = dialogPhoto;
    }
    if (customData != null) {
      data["customData"] = customData;
    }

    Map<Object?, Object?> map = await _chatModule.invokeMethod(UPDATE_DIALOG_METHOD, data);

    QBDialog? updatedDialog = QBDialogMapper.mapToQBDialog(map);

    return updatedDialog;
  }

  Future<QBDialog?> createDialog(int dialogType,
      {List<int>? occupantsIds,
      String? dialogName,
      String? dialogPhoto,
      Map<String, Object>? customData}) async {
    Map<String, Object> data = Map();

    data["type"] = dialogType;

    if (dialogName != null) {
      data["name"] = dialogName;
    }
    if (occupantsIds != null) {
      data["occupantsIds"] = occupantsIds;
    }
    if (dialogPhoto != null) {
      data["photo"] = dialogPhoto;
    }
    if (customData != null) {
      data["customData"] = customData;
    }

    Map<Object?, Object?> map = await _chatModule.invokeMethod(CREATE_DIALOG_METHOD, data);

    QBDialog? createdDialog = QBDialogMapper.mapToQBDialog(map);

    return createdDialog;
  }

  Future<void> deleteDialog(String dialogId, {bool? force}) async {
    Map<String, Object> data = Map();

    data["dialogId"] = dialogId;

    if (force != null) {
      data["force"] = force;
    }

    await _chatModule.invokeMethod(DELETE_DIALOG_METHOD, data);
  }

  Future<void> leaveDialog(String dialogId) async {
    Map<String, Object> data = Map();

    data["dialogId"] = dialogId;

    await _chatModule.invokeMethod(LEAVE_DIALOG_METHOD, data);
  }

  Future<QBDialog?> joinDialog(String dialogId) async {
    Map<String, Object> data = Map();

    data["dialogId"] = dialogId;

    Map<Object?, Object?> map = await _chatModule.invokeMethod(JOIN_DIALOG_METHOD, data);

    QBDialog? createdDialog = QBDialogMapper.mapToQBDialog(map);

    return createdDialog;
  }

  Future<bool> isJoinedDialog(String dialogId) async {
    Map<String, Object> data = Map();

    data["dialogId"] = dialogId;

    return await _chatModule.invokeMethod(IS_JOINED_DIALOG_METHOD, data);
  }

  Future<dynamic> getOnlineUsers(String dialogId) async {
    Map<String, Object> data = Map();

    data["dialogId"] = dialogId;

    List<dynamic>? onlineUsers = await _chatModule.invokeMethod(GET_ONLINE_USERS_METHOD, data);
    return onlineUsers;
  }

  Future<void> sendMessage(String dialogId,
      {String? body,
      List<QBAttachment>? attachments,
      Map<String, String>? properties,
      bool markable = false,
      String? dateSent,
      bool? saveToHistory}) async {
    Map<String, Object> data = Map();

    data["dialogId"] = dialogId;

    data["markable"] = markable;

    if (body != null) {
      data["body"] = body;
    }
    if (attachments != null && attachments.length > 0) {
      List<Map<String, Object>?> attachmentMapList = [];
      for (final attachment in attachments) {
        Map<String, Object>? attachmentMap = QBAttachmentMapper.qbAttachmentToMap(attachment);

        attachmentMapList.add(attachmentMap);
      }
      data["attachments"] = attachmentMapList;
    }
    if (properties != null && properties.length > 0) {
      data["properties"] = properties;
    }
    if (dateSent != null) {
      data["dateSent"] = dateSent;
    }
    if (saveToHistory != null) {
      data["saveToHistory"] = saveToHistory;
    }
    await _chatModule.invokeMethod(SEND_MESSAGE_METHOD, data);
  }

  Future<void> sendSystemMessage(int recipientId, {Map<String, String>? properties}) async {
    Map<String, Object> data = Map();

    data["recipientId"] = recipientId;

    if (properties != null) {
      data["properties"] = properties;
    }
    await _chatModule.invokeMethod(SEND_SYSTEM_MESSAGE_METHOD, data);
  }

  Future<void> markMessageRead(QBMessage qbMessage) async {
    Map<String, Object> data = Map();

    Map<String, Object> messageMap = QBMessageMapper.qbMessageToMap(qbMessage)!;

    data["message"] = messageMap;

    await _chatModule.invokeMethod(MARK_MESSAGE_READ_METHOD, data);
  }

  Future<void> markMessageDelivered(QBMessage qbMessage) async {
    Map<String, Object> data = Map();

    Map<String, Object> messageMap = QBMessageMapper.qbMessageToMap(qbMessage)!;

    data["message"] = messageMap;

    await _chatModule.invokeMethod(MARK_MESSAGE_DELIVERED_METHOD, data);
  }

  Future<void> sendIsTyping(String dialogId) async {
    Map<String, Object> data = Map();

    data["dialogId"] = dialogId;

    await _chatModule.invokeMethod(SEND_IS_TYPING_METHOD, data);
  }

  Future<void> sendStoppedTyping(String dialogId) async {
    Map<String, Object> data = Map();

    data["dialogId"] = dialogId;

    await _chatModule.invokeMethod(SEND_STOPPED_TYPING_METHOD, data);
  }

  Future<List<QBMessage?>> getDialogMessages(String dialogId,
      {QBSort? sort,
      QBFilter? filter,
      int limit = 100,
      int skip = 0,
      bool markAsRead = false}) async {
    Map<String, Object> data = Map();

    data["dialogId"] = dialogId;
    data["limit"] = limit;
    data["skip"] = skip;
    data["markAsRead"] = markAsRead;

    if (sort != null) {
      data["sort"] = QBSortMapper.sortToMap(sort)!;
    }
    if (filter != null) {
      data["filter"] = QBFilterMapper.filterToMap(filter)!;
    }

    Map<Object?, Object?> map = await _chatModule.invokeMethod(GET_DIALOG_MESSAGES_METHOD, data);

    List<Object?> list = map["messages"] as List<Object?>;

    List<QBMessage?> messagesList = [];

    for (final messageMap in list) {
      QBMessage? qbMessage = QBMessageMapper.mapToQBMessage(messageMap as Map<Object?, Object?>);
      messagesList.add(qbMessage);
    }

    return messagesList;
  }

  Future<QBMessagesCounter?> getTotalUnreadMessagesCount({List<String>? dialogIds}) async {
    Map<Object?, Object?> response =
        await _chatModule.invokeMethod(GET_TOTAL_UNREAD_MESSAGES_COUNT_METHOD, dialogIds);

    Map<String, Object>? resultMap = HashMap.from(response);

    QBMessagesCounter? messageCounter = QBMessagesCounterMapper.mapToQBMessageCounter(resultMap);

    return messageCounter;
  }

  Future<StreamSubscription<dynamic>> subscribeChatEvent(String eventName, eventMethod,
      {onErrorMethod}) async {
    return EventChannel(eventName)
        .receiveBroadcastStream(eventName)
        .listen(eventMethod, onError: onErrorMethod);
  }
}
