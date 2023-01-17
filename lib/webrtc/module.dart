///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:quickblox_sdk/mappers/qb_rtc_session_mapper.dart';
import 'package:quickblox_sdk/models/qb_rtc_session.dart';

class WebRTC {
  const WebRTC();

  ///////////////////////////////////////////////////////////////////////////
  // WebRTC MODULE
  ///////////////////////////////////////////////////////////////////////////

  //Channel name
  static const CHANNEL_NAME = "FlutterQBWebRTCChannel";

  //WebRTC Methods
  static const INIT_METHOD = "init";
  static const RELEASE_METHOD = "release";
  static const GET_SESSION_METHOD = "getSession";
  static const CALL_METHOD = "call";
  static const ACCEPT_METHOD = "accept";
  static const REJECT_METHOD = "reject";
  static const HANGUP_METHOD = "hangUp";
  static const ENABLE_VIDEO_METHOD = "enableVideo";
  static const ENABLE_AUDIO_METHOD = "enableAudio";
  static const SWITCH_CAMERA_METHOD = "switchCamera";
  static const MIRROR_CAMERA_METHOD = "mirrorCamera";
  static const SWITCH_AUDIO_OUTPUT = "switchAudioOutput";
  static const SUBSCRIBE_EVENTS_METHOD = "subscribeEvents";
  static const UNSUBSCRIBE_EVENTS_METHOD = "unsubscribeEvents";

  //Module
  static const _webRtcModule = const MethodChannel(CHANNEL_NAME);

  Future<void> init() async {
    await _webRtcModule.invokeMethod(INIT_METHOD, null);
  }

  Future<void> release() async {
    await _webRtcModule.invokeMethod(RELEASE_METHOD, null);
  }

  Future<QBRTCSession?> getSession(String sessionId) async {
    Map<String, Object> values = Map();

    values["sessionId"] = sessionId;

    Map<Object?, Object?> map =
        await _webRtcModule.invokeMethod(GET_SESSION_METHOD, values);

    QBRTCSession? qbSession = QBRTCSessionMapper.mapToQBRtcSession(map);

    return qbSession;
  }

  Future<QBRTCSession?> call(List<int> opponentsIds, int sessionType,
      {Map<String, Object>? userInfo}) async {
    Map<String, Object> values = Map();

    values["opponentsIds"] = opponentsIds;
    values["type"] = sessionType;

    if (userInfo != null) {
      values["userInfo"] = userInfo;
    }

    Map<Object?, Object?> map =
        await _webRtcModule.invokeMethod(CALL_METHOD, values);

    QBRTCSession? qbSession = QBRTCSessionMapper.mapToQBRtcSession(map);

    return qbSession;
  }

  Future<QBRTCSession?> accept(String sessionId,
      {Map<String, Object>? userInfo}) async {
    Map<String, Object> values = Map();

    values["sessionId"] = sessionId;

    if (userInfo != null) {
      values["userInfo"] = userInfo;
    }

    Map<Object?, Object?> map =
        await _webRtcModule.invokeMethod(ACCEPT_METHOD, values);

    QBRTCSession? qbSession = QBRTCSessionMapper.mapToQBRtcSession(map);

    return qbSession;
  }

  Future<QBRTCSession?> reject(String sessionId,
      {Map<String, Object>? userInfo}) async {
    Map<String, Object> values = Map();

    values["sessionId"] = sessionId;

    if (userInfo != null) {
      values["userInfo"] = userInfo;
    }

    Map<Object?, Object?> map =
        await _webRtcModule.invokeMethod(REJECT_METHOD, values);

    QBRTCSession? qbSession = QBRTCSessionMapper.mapToQBRtcSession(map);

    return qbSession;
  }

  Future<QBRTCSession?> hangUp(String sessionId,
      {Map<String, Object>? userInfo}) async {
    Map<String, Object> values = Map();

    values["sessionId"] = sessionId;

    if (userInfo != null) {
      values["userInfo"] = userInfo;
    }

    Map<Object?, Object?> map =
        await _webRtcModule.invokeMethod(HANGUP_METHOD, values);

    QBRTCSession? qbSession = QBRTCSessionMapper.mapToQBRtcSession(map);

    return qbSession;
  }

  Future<void> enableVideo(String sessionId,
      {bool? enable, double? userId}) async {
    Map<String, Object> values = Map();

    values["sessionId"] = sessionId;

    if (enable != null) {
      values["enable"] = enable;
    }
    if (userId != null) {
      values["userId"] = userId;
    }

    await _webRtcModule.invokeMethod(ENABLE_VIDEO_METHOD, values);
  }

  Future<void> enableAudio(String sessionId,
      {bool? enable, double? userId}) async {
    Map<String, Object> values = Map();

    values["sessionId"] = sessionId;

    if (enable != null) {
      values["enable"] = enable;
    }
    if (userId != null) {
      values["userId"] = userId;
    }

    await _webRtcModule.invokeMethod(ENABLE_AUDIO_METHOD, values);
  }

  Future<void> switchCamera(String sessionId) async {
    Map<String, Object> values = Map();

    values["sessionId"] = sessionId;

    await _webRtcModule.invokeMethod(SWITCH_CAMERA_METHOD, values);
  }

  Future<void> mirrorCamera(int userId, bool mirror) async {
    Map<String, Object> values = Map();

    values["userId"] = userId;
    values["mirror"] = mirror;

    await _webRtcModule.invokeMethod(MIRROR_CAMERA_METHOD, values);
  }

  Future<void> switchAudioOutput(int output) async {
    Map<String, Object> values = Map();

    values["output"] = output;

    await _webRtcModule.invokeMethod(SWITCH_AUDIO_OUTPUT, values);
  }

  Future<StreamSubscription<dynamic>> subscribeRTCEvent(
      String eventName, eventMethod,
      {onErrorMethod}) async {
    return EventChannel(eventName)
        .receiveBroadcastStream(eventName)
        .listen(eventMethod, onError: onErrorMethod);
  }
}
