import 'dart:async';

import 'package:flutter/services.dart';
import 'package:quickblox_sdk/mappers/qb_setting_mapper.dart';
import 'package:quickblox_sdk/models/qb_settings.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class Settings {
  const Settings();

  ///////////////////////////////////////////////////////////////////////////
  // SETTINGS MODULE
  ///////////////////////////////////////////////////////////////////////////

  //Channel name
  static const CHANNEL_NAME = "FlutterQBSettingsChannel";

  //Methods
  static const INIT_METHOD = "init";
  static const GET_METHOD = "get";
  static const INIT_WITHOUT_AUTH_KEY_AND_SECRET_METHOD = "initWithoutAuthKeyAndSecret";
  static const ENABLE_CARBONS_METHOD = "enableCarbons";
  static const DISABLE_CARBONS_METHOD = "disableCarbons";
  static const INIT_STREAM_MANAGEMENT_METHOD = "initStreamManagement";
  static const ENABLE_AUTO_RECONNECT_METHOD = "enableAutoReconnect";
  static const ENABLE_LOGGING_METHOD = "enableLogging";
  static const DISABLE_LOGGING_METHOD = "disableLogging";
  static const ENABLE_XMPP_LOGGING_METHOD = "enableXMPPLogging";
  static const DISABLE_XMPP_LOGGING_METHOD = "disableXMPPLogging";

  //Module
  static const _settingsModule = const MethodChannel(CHANNEL_NAME);

  Future<void> init(String appId, String authKey, String authSecret, String accountKey,
      {String? apiEndpoint, String? chatEndpoint}) async {
    Map<String, Object> data = Map();

    data["appId"] = appId;
    data["authKey"] = authKey;
    data["authSecret"] = authSecret;
    data["accountKey"] = accountKey;

    if (apiEndpoint != null) {
      data["apiEndpoint"] = apiEndpoint;
    }
    if (chatEndpoint != null) {
      data["chatEndpoint"] = chatEndpoint;
    }
    await _settingsModule.invokeMethod(INIT_METHOD, data);
  }

  Future<void> initWithAppId(String appId,
      {String? accountKey, String? apiEndpoint, String? chatEndpoint}) async {
    Map<String, Object> data = Map();

    data["appId"] = appId;

    if (accountKey != null) {
      data["accountKey"] = accountKey;
    }
    if (apiEndpoint != null) {
      data["apiEndpoint"] = apiEndpoint;
    }
    if (chatEndpoint != null) {
      data["chatEndpoint"] = chatEndpoint;
    }
    await _settingsModule.invokeMethod(INIT_WITHOUT_AUTH_KEY_AND_SECRET_METHOD, data);
  }

  Future<QBSettings?> get() async {
    Map<Object?, Object?> map = await _settingsModule.invokeMethod(GET_METHOD);

    QBSettings? settings = QBSettingsMapper.mapToSettings(map);

    return settings;
  }

  Future<void> enableCarbons() async {
    await _settingsModule.invokeMethod(ENABLE_CARBONS_METHOD);
  }

  Future<void> disableCarbons() async {
    await _settingsModule.invokeMethod(DISABLE_CARBONS_METHOD);
  }

  Future<void> initStreamManagement(int messageTimeout, {bool? autoReconnect}) async {
    Map<String, Object> data = Map();

    data["messageTimeout"] = messageTimeout;

    if (autoReconnect != null) {
      data["autoReconnect"] = autoReconnect;
    }

    await _settingsModule.invokeMethod(INIT_STREAM_MANAGEMENT_METHOD, data);
  }

  Future<void> enableAutoReconnect(bool enable) async {
    Map<String, Object> data = Map();

    data["enable"] = enable;

    await _settingsModule.invokeMethod(ENABLE_AUTO_RECONNECT_METHOD, data);
  }

  Future<void> enableLogging() async {
    await _settingsModule.invokeMethod(ENABLE_LOGGING_METHOD);
  }

  Future<void> disableLogging() async {
    await _settingsModule.invokeMethod(DISABLE_LOGGING_METHOD);
  }

  Future<void> enableXMPPLogging() async {
    await _settingsModule.invokeMethod(ENABLE_XMPP_LOGGING_METHOD);
  }

  Future<void> disableXMPPLogging() async {
    await _settingsModule.invokeMethod(DISABLE_XMPP_LOGGING_METHOD);
  }
}
