import 'package:quickblox_sdk/models/qb_conference_rtc_session.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class QBConferenceSessionMapper {
  static QBConferenceRTCSession? mapToQBConferenceSession(
      Map<dynamic, dynamic>? map) {
    if (map == null || map.length <= 0) {
      return null;
    }

    QBConferenceRTCSession session = QBConferenceRTCSession();

    if (map.containsKey("id")) {
      session.id = map["id"] as String?;
    }

    if (map.containsKey("roomId")) {
      session.roomId = map["roomId"] as String?;
    }

    if (map.containsKey("type")) {
      session.type = map["type"] as int?;
    }

    if (map.containsKey("state")) {
      session.state = map["state"] as int?;
    }

    if (map.containsKey("publishers")) {
      session.publishers =
          List<int>.from(map["publishers"] as Iterable<dynamic>);
    }

    return session;
  }
}
