import 'package:quickblox_sdk/models/qb_file.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class QBFileMapper {
  static QBFile? mapToQBFile(Map<dynamic, dynamic>? map) {
    if (map == null || map.length <= 0) {
      return null;
    }

    QBFile file = QBFile();

    if (map.containsKey("id")) {
      file.id = map["id"] as int?;
    }
    if (map.containsKey("uid")) {
      file.uid = map["uid"] as String?;
    }
    if (map.containsKey("contentType")) {
      file.contentType = map["contentType"] as String?;
    }
    if (map.containsKey("name")) {
      file.name = map["name"] as String?;
    }
    if (map.containsKey("size")) {
      file.size = map["size"] as int?;
    }
    if (map.containsKey("completedAt")) {
      file.completedAt = map["completedAt"] as String?;
    }
    if (map.containsKey("isPublic")) {
      file.isPublic = map["isPublic"] as bool?;
    }
    if (map.containsKey("lastReadAccessTime")) {
      file.lastReadAccessTime = map["lastReadAccessTime"] as String?;
    }
    if (map.containsKey("tags")) {
      file.tags = map["tags"] as String?;
    }

    return file;
  }
}
