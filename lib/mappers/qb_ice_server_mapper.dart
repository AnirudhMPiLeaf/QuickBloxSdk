import 'package:quickblox_sdk/models/qb_ice_server.dart';

///Created by Injoit on 2022-04-06.
///Copyright © 2022 Quickblox. All rights reserved.

class QBIceServerMapper {
  static QBIceServer? mapToQBIceServer(Map<dynamic, dynamic>? map) {
    if (map == null || map.length <= 0) {
      return null;
    }

    QBIceServer qbIceServer = QBIceServer();

    if (map.containsKey("url")) {
      qbIceServer.url = map["url"] as String?;
    }
    if (map.containsKey("userName")) {
      qbIceServer.userName = map["userName"] as String?;
    }
    if (map.containsKey("password")) {
      qbIceServer.password = map["password"] as String?;
    }

    return qbIceServer;
  }

  static Map<String, Object>? qbIceServerToMap(QBIceServer? qbIceServer) {
    if (qbIceServer == null) {
      return null;
    }

    Map<String, Object> iceServerMap = Map();

    if (qbIceServer.url != null) {
      iceServerMap["url"] = qbIceServer.url as String;
    }
    if (qbIceServer.userName != null) {
      iceServerMap["userName"] = qbIceServer.userName as String;
    }
    if (qbIceServer.password != null) {
      iceServerMap["password"] = qbIceServer.password as String;
    }

    return iceServerMap;
  }
}
