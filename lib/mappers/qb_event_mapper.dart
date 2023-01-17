import 'package:quickblox_sdk/models/qb_event.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class QBEventMapper {
  static QBEvent? mapToQBEvent(Map<dynamic, dynamic>? map) {
    if (map == null || map.length <= 0) {
      return null;
    }

    QBEvent qbEvent = QBEvent();

    if (map.containsKey("id")) {
      qbEvent.id = map["id"] as int?;
    }
    if (map.containsKey("name")) {
      qbEvent.name = map["name"] as String?;
    }
    if (map.containsKey("active")) {
      qbEvent.active = map["active"] as bool?;
    }
    if (map.containsKey("notificationType")) {
      qbEvent.notificationType = map["notificationType"] as String?;
    }
    if (map.containsKey("pushType")) {
      qbEvent.pushType = map["pushType"] as int?;
    }
    if (map.containsKey("date")) {
      qbEvent.date = map["date"] as double?;
    }
    if (map.containsKey("endDate")) {
      qbEvent.endDate = map["endDate"] as int?;
    }
    if (map.containsKey("period")) {
      qbEvent.period = map["period"] as String?;
    }
    if (map.containsKey("occuredCount")) {
      qbEvent.occuredCount = map["occuredCount"] as int?;
    }
    if (map.containsKey("senderId")) {
      qbEvent.senderId = map["senderId"] as int?;
    }
    if (map.containsKey("recipientsIds")) {
      List<Object> objectsList = map["recipientsIds"] as List<Object>;
      qbEvent.recipientsIds = objectsList.cast<String>();
    }
    if (map.containsKey("recipientsTagsAny")) {
      List<Object> objectsList = map["recipientsTagsAny"] as List<Object>;
      qbEvent.recipientsTagsAny = objectsList.cast<String>();
    }
    if (map.containsKey("recipientsTagsAll")) {
      List<Object> objectsList = map["recipientsTagsAll"] as List<Object>;
      qbEvent.recipientsTagsAll = objectsList.cast<String>();
    }
    if (map.containsKey("recipientsTagsExclude")) {
      List<Object> objectsList = map["recipientsTagsExclude"] as List<Object>;
      qbEvent.recipientsTagsExclude = objectsList.cast<String>();
    }
    if (map.containsKey("payload")) {
      qbEvent.payload = map["payload"] as String?;
    }

    return qbEvent;
  }
}
