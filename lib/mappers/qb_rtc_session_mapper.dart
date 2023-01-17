import 'package:quickblox_sdk/models/qb_rtc_session.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class QBRTCSessionMapper {
  static QBRTCSession? mapToQBRtcSession(Map<dynamic, dynamic>? map) {
    if (map == null || map.length <= 0) {
      return null;
    }

    QBRTCSession qbrtcSession = QBRTCSession();

    if (map.containsKey("id")) {
      qbrtcSession.id = map["id"] as String?;
    }
    if (map.containsKey("type")) {
      qbrtcSession.type = map["type"] as int?;
    }
    if (map.containsKey("state")) {
      qbrtcSession.state = map["state"] as int?;
    }
    if (map.containsKey("initiatorId")) {
      qbrtcSession.initiatorId = map["initiatorId"] as int?;
    }
    if (map.containsKey("opponentsIds")) {
      List<int> opponentIdsList =
          List.from(map["opponentsIds"] as Iterable<dynamic>);
      qbrtcSession.opponentsIds = opponentIdsList;
    }

    return qbrtcSession;
  }
}
