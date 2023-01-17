import 'package:quickblox_sdk/models/qb_session.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class QBSessionMapper {
  static QBSession? mapToQBSession(Map<dynamic, dynamic>? map) {
    if (map == null || map.length <= 0) {
      return null;
    }

    QBSession qbSession = QBSession();

    if (map.containsKey("token")) {
      qbSession.token = map["token"] as String?;
    }

    if (map.containsKey("expirationDate")) {
      qbSession.expirationDate = map["expirationDate"] as String?;
    }

    if (map.containsKey("userId")) {
      qbSession.userId = map["userId"] as int?;
    }

    if (map.containsKey("applicationId")) {
      qbSession.applicationId = map["applicationId"] as int?;
    }

    return qbSession;
  }

  static Map<String, Object>? qbSessionToMap(QBSession? session) {
    if (session == null) {
      return null;
    }

    Map<String, Object> map = Map();

    if (session.token != null && session.token!.isNotEmpty) {
      map["token"] = session.token as Object;
    }

    if (session.expirationDate != null && session.expirationDate!.isNotEmpty) {
      map["expirationDate"] = session.expirationDate as Object;
    }

    if (session.userId != null && session.userId! > 0) {
      map["userId"] = session.userId as Object;
    }

    if (session.applicationId != null && session.applicationId! > 0) {
      map["applicationId"] = session.applicationId as Object;
    }

    return map;
  }
}
