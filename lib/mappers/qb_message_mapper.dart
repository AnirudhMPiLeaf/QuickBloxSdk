import 'dart:collection';

import 'package:quickblox_sdk/mappers/qb_attachment_mapper.dart';
import 'package:quickblox_sdk/models/qb_attachment.dart';
import 'package:quickblox_sdk/models/qb_message.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class QBMessageMapper {
  static QBMessage? mapToQBMessage(Map<dynamic, dynamic>? map) {
    if (map == null || map.length <= 0) {
      return null;
    }

    QBMessage qbMessage = QBMessage();

    if (map.containsKey("id")) {
      qbMessage.id = map["id"] as String?;
    }
    if (map.containsKey("attachments")) {
      List<Object?> attachmentsMapsList = map["attachments"] as List<Object?>;

      List<QBAttachment?> attachmentsList = [];
      for (final attachmentDynamicMap in attachmentsMapsList) {
        Map<String, Object> attachmentMap =
            Map<String, Object>.from(attachmentDynamicMap as Map<dynamic, dynamic>);
        QBAttachment? qbAttachment = QBAttachmentMapper.mapToQBAttachment(attachmentMap);
        attachmentsList.add(qbAttachment);
      }
      qbMessage.attachments = attachmentsList;
    }
    if (map.containsKey("properties")) {
      LinkedHashMap<dynamic, dynamic> hashMap =
          map["properties"] as LinkedHashMap<dynamic, dynamic>;

      Map<String, String> propertiesMap =
          hashMap.map((key, value) => MapEntry(key as String, value.toString()));

      qbMessage.properties = propertiesMap;
    }
    if (map.containsKey("dateSent")) {
      qbMessage.dateSent = map["dateSent"] as int?;
    }
    if (map.containsKey("senderId")) {
      qbMessage.senderId = map["senderId"] as int?;
    }
    if (map.containsKey("recipientId")) {
      qbMessage.recipientId = map["recipientId"] as int?;
    }
    if (map.containsKey("readIds")) {
      qbMessage.readIds = List.from(map["readIds"] as Iterable<dynamic>);
    }
    if (map.containsKey("deliveredIds")) {
      qbMessage.deliveredIds = List.from(map["deliveredIds"] as Iterable<dynamic>);
    }
    if (map.containsKey("dialogId")) {
      qbMessage.dialogId = map["dialogId"] as String?;
    }
    if (map.containsKey("markable")) {
      qbMessage.markable = map["markable"] as bool?;
    }
    if (map.containsKey("delayed")) {
      qbMessage.delayed = map["delayed"] as bool?;
    }
    if (map.containsKey("body")) {
      qbMessage.body = map["body"] as String?;
    }

    return qbMessage;
  }

  static Map<String, Object>? qbMessageToMap(QBMessage? qbMessage) {
    if (qbMessage == null) {
      return null;
    }

    Map<String, Object> messageMap = Map();

    messageMap["id"] = qbMessage.id as Object;

    List<Map<String, Object>?> attachmentMapsList = [];

    if (qbMessage.attachments != null) {
      for (QBAttachment? attachment in qbMessage.attachments as List<QBAttachment?>) {
        Map<String, Object>? attachmentMap = QBAttachmentMapper.qbAttachmentToMap(attachment);
        if (attachmentMap != null) {
          attachmentMapsList.add(attachmentMap);
        }
      }
    }

    messageMap["attachments"] = attachmentMapsList;

    if (qbMessage.properties != null) {
      messageMap["properties"] = qbMessage.properties as Object;
    }
    if (qbMessage.dateSent != null) {
      messageMap["dateSent"] = qbMessage.dateSent as Object;
    }
    if (qbMessage.senderId != null) {
      messageMap["senderId"] = qbMessage.senderId as Object;
    }
    if (qbMessage.recipientId != null) {
      messageMap["recipientId"] = qbMessage.recipientId as Object;
    }
    if (qbMessage.readIds != null) {
      messageMap["readIds"] = qbMessage.readIds as Object;
    }
    if (qbMessage.deliveredIds != null) {
      messageMap["deliveredIds"] = qbMessage.deliveredIds as Object;
    }
    if (qbMessage.dialogId != null) {
      messageMap["dialogId"] = qbMessage.dialogId as Object;
    }
    if (qbMessage.markable != null) {
      messageMap["markable"] = qbMessage.markable as Object;
    }
    if (qbMessage.delayed != null) {
      messageMap["delayed"] = qbMessage.delayed as Object;
    }
    if (qbMessage.body != null) {
      messageMap["body"] = qbMessage.body as Object;
    }

    return messageMap;
  }
}
