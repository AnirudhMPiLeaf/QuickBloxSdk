import 'package:quickblox_sdk/models/qb_settings.dart';

class QBSettingsMapper {
  static QBSettings? mapToSettings(Map<dynamic, dynamic>? map) {
    if (map == null || map.length <= 0) {
      return null;
    }

    QBSettings qbSettings = QBSettings();

    if (map.containsKey("authKey")) {
      qbSettings.authKey = map["authKey"] as String?;
    }
    if (map.containsKey("authSecret")) {
      qbSettings.authSecret = map["authSecret"] as String?;
    }
    if (map.containsKey("apiEndpoint")) {
      qbSettings.apiEndpoint = map["apiEndpoint"] as String?;
    }
    if (map.containsKey("appId")) {
      qbSettings.appId = map["appId"] as String?;
    }
    if (map.containsKey("sdkVersion")) {
      qbSettings.sdkVersion = map["sdkVersion"] as String?;
    }
    if (map.containsKey("chatEndpoint")) {
      qbSettings.chatEndpoint = map["chatEndpoint"] as String?;
    }
    if (map.containsKey("accountKey")) {
      qbSettings.accountKey = map["accountKey"] as String?;
    }

    return qbSettings;
  }
}
