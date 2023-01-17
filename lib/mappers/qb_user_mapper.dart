import 'package:quickblox_sdk/models/qb_user.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class QBUserMapper {
  static QBUser? mapToQBUser(Map<dynamic, dynamic>? map) {
    if (map == null || map.length <= 0) {
      return null;
    }

    QBUser qbUser = QBUser();

    if (map.containsKey("blobId")) {
      qbUser.blobId = map["blobId"] as int?;
    }
    if (map.containsKey("customData")) {
      qbUser.customData = map["customData"] as String?;
    }
    if (map.containsKey("email")) {
      qbUser.email = map["email"] as String?;
    }
    if (map.containsKey("externalId")) {
      qbUser.externalId = map["externalId"] as String?;
    }
    if (map.containsKey("facebookId")) {
      qbUser.facebookId = map["facebookId"] as String?;
    }
    if (map.containsKey("fullName")) {
      qbUser.fullName = map["fullName"] as String?;
    }
    if (map.containsKey("id")) {
      qbUser.id = map["id"] as int?;
    }
    if (map.containsKey("login")) {
      qbUser.login = map["login"] as String?;
    }
    if (map.containsKey("phone")) {
      qbUser.phone = map["phone"] as String?;
    }
    if (map.containsKey("tags")) {
      List<String> tagsList = List.from(map["tags"] as Iterable<dynamic>);
      qbUser.tags = tagsList;
    }
    if (map.containsKey("twitterId")) {
      qbUser.twitterId = map["twitterId"] as String?;
    }
    if (map.containsKey("website")) {
      qbUser.website = map["website"] as String?;
    }
    if (map.containsKey("lastRequestAt")) {
      qbUser.lastRequestAt = map["lastRequestAt"] as String?;
    }

    return qbUser;
  }
}
