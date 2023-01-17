import 'package:quickblox_sdk/models/qb_custom_object.dart';
import 'package:quickblox_sdk/models/qb_custom_object_permission.dart';
import 'package:quickblox_sdk/models/qb_custom_object_permission_level.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class QBCustomObjectMapper {
  static QBCustomObject? mapToQBCustomObject(Map<dynamic, dynamic>? map) {
    if (map == null || map.length <= 0) {
      return null;
    }

    QBCustomObject customObject = QBCustomObject();

    if (map.containsKey("id")) {
      customObject.id = map["id"] as String?;
    }
    if (map.containsKey("parentId")) {
      customObject.parentId = map["parentId"] as String?;
    }
    if (map.containsKey("createdAt")) {
      customObject.createdAt = map["createdAt"] as String?;
    }
    if (map.containsKey("updatedAt")) {
      customObject.updatedAt = map["updatedAt"] as String?;
    }
    if (map.containsKey("className")) {
      customObject.className = map["className"] as String?;
    }
    if (map.containsKey("userId")) {
      customObject.userId = map["userId"] as int?;
    }
    if (map.containsKey("fields")) {
      Map<String, Object> fieldsMap =
          Map.from(map["fields"] as Map<dynamic, dynamic>);
      customObject.fields = fieldsMap;
    }
    if (map.containsKey("permission")) {
      Map<String, Object> permissionMap =
          Map.from(map["permission"] as Map<dynamic, dynamic>);
      QBCustomObjectPermission permission =
          mapToQBCustomObjectPermission(permissionMap);
      customObject.permission = permission;
    }

    return customObject;
  }

  static QBCustomObjectPermission mapToQBCustomObjectPermission(
      Map<String, Object> map) {
    QBCustomObjectPermission permission = QBCustomObjectPermission();

    if (map.containsKey("customObjectId")) {
      permission.customObjectId = map["customObjectId"] as String?;
    }
    if (map.containsKey("readLevel")) {
      Map<String, Object> permissionMap =
          Map.from(map["readLevel"] as Map<dynamic, dynamic>);
      QBCustomObjectPermissionLevel permissionLevel =
          mapToQBCustomObjectPermissionLevel(permissionMap);
      permission.readLevel = permissionLevel;
    }
    if (map.containsKey("updateLevel")) {
      Map<String, Object> permissionMap =
          Map.from(map["updateLevel"] as Map<dynamic, dynamic>);
      QBCustomObjectPermissionLevel permissionLevel =
          mapToQBCustomObjectPermissionLevel(permissionMap);
      permission.updateLevel = permissionLevel;
    }
    if (map.containsKey("deleteLevel")) {
      Map<String, Object> permissionMap =
          Map.from(map["deleteLevel"] as Map<dynamic, dynamic>);
      QBCustomObjectPermissionLevel permissionLevel =
          mapToQBCustomObjectPermissionLevel(permissionMap);
      permission.deleteLevel = permissionLevel;
    }

    return permission;
  }

  static QBCustomObjectPermissionLevel mapToQBCustomObjectPermissionLevel(
      Map<String, Object> map) {
    QBCustomObjectPermissionLevel permissionLevel =
        QBCustomObjectPermissionLevel();

    if (map.containsKey("access")) {
      permissionLevel.access = map["access"] as String?;
    }
    if (map.containsKey("usersIds")) {
      permissionLevel.usersIds = map["usersIds"] as List<String>?;
    }
    if (map.containsKey("usersGroups")) {
      permissionLevel.usersGroups = map["usersGroups"] as List<String>?;
    }

    return permissionLevel;
  }
}
