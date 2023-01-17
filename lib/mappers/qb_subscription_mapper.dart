import 'package:quickblox_sdk/models/qb_subscription.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class QBSubscriptionMapper {
  static QBSubscription? mapToQBSubscription(Map<dynamic, dynamic>? map) {
    if (map == null || map.length <= 0) {
      return null;
    }

    QBSubscription subscription = QBSubscription();

    if (map.containsKey("id")) {
      subscription.id = map["id"] as int?;
    }
    if (map.containsKey("deviceUdid")) {
      subscription.deviceUdid = map["deviceUdid"] as String?;
    }
    if (map.containsKey("deviceToken")) {
      subscription.deviceToken = map["deviceToken"] as String?;
    }
    if (map.containsKey("devicePlatform")) {
      subscription.devicePlatform = map["devicePlatform"] as String?;
    }
    if (map.containsKey("pushChannel")) {
      subscription.pushChannel = map["pushChannel"] as String?;
    }

    return subscription;
  }
}
