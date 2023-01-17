///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:quickblox_sdk/mappers/qb_conference_session_mapper.dart';
import 'package:quickblox_sdk/models/qb_conference_rtc_session.dart';

class Conference {
  const Conference();

  ///////////////////////////////////////////////////////////////////////////
  // WebRTC MODULE
  ///////////////////////////////////////////////////////////////////////////

  //Channel name
  static const CHANNEL_NAME = "FlutterQBConferenceChannel";

  //WebRTC Methods
  static const INIT_METHOD = "init";
  static const RELEASE_METHOD = "release";
  static const CREATE_METHOD = "create";
  static const JOIN_AS_PUBLISHER_METHOD = "joinAsPublisher";
  static const GET_ONLINE_PARTICIPANTS_METHOD = "getOnlineParticipants";
  static const SUBSCRIBE_TO_PARTICIPANT_METHOD = "subscribeToParticipant";
  static const UNSUBSCRIBE_FROM_PARTICIPANT_METHOD =
      "unsubscribeFromParticipant";
  static const LEAVE_METHOD = "leave";
  static const ENABLE_VIDEO_METHOD = "enableVideo";
  static const ENABLE_AUDIO_METHOD = "enableAudio";
  static const SWITCH_CAMERA_METHOD = "switchCamera";
  static const SWITCH_AUDIO_OUTPUT = "switchAudioOutput";
  static const SUBSCRIBE_EVENTS_METHOD = "subscribeEvents";
  static const UNSUBSCRIBE_EVENTS_METHOD = "unsubscribeEvents";

  //Module
  static const _conferenceModule = const MethodChannel(CHANNEL_NAME);

  Future<void> init(final String endpoint) async {
    await _conferenceModule.invokeMethod(INIT_METHOD, endpoint);
  }

  Future<void> release() async {
    await _conferenceModule.invokeMethod(RELEASE_METHOD, null);
  }

  Future<QBConferenceRTCSession?> create(
      final String roomId, final int sessionType) async {
    Map<Object?, Object?> map = await _conferenceModule
        .invokeMethod(CREATE_METHOD, [roomId, sessionType]);

    QBConferenceRTCSession? session =
        QBConferenceSessionMapper.mapToQBConferenceSession(map);

    return session;
  }

  Future<List<int?>> joinAsPublisher(String id) async {
    Object? object =
        await _conferenceModule.invokeMethod(JOIN_AS_PUBLISHER_METHOD, id);

    List<int?> publishers = List.castFrom(object as List<dynamic>);

    return publishers;
  }

  Future<List<int?>> getOnlineParticipants(String id) async {
    Object? object = await _conferenceModule.invokeMethod(
        GET_ONLINE_PARTICIPANTS_METHOD, id);

    List<int?> participants = List.castFrom(object as List<dynamic>);

    return participants;
  }

  Future<void> subscribeToParticipant(String sessionId, int userId) async {
    await _conferenceModule
        .invokeMethod(SUBSCRIBE_TO_PARTICIPANT_METHOD, [userId, sessionId]);
  }

  Future<void> unsubscribeFromParticipant(String sessionId, int userId) async {
    await _conferenceModule
        .invokeMethod(UNSUBSCRIBE_FROM_PARTICIPANT_METHOD, [userId, sessionId]);
  }

  Future<void> leave(String id) async {
    await _conferenceModule.invokeMethod(LEAVE_METHOD, id);
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

    await _conferenceModule.invokeMethod(ENABLE_VIDEO_METHOD, values);
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

    await _conferenceModule.invokeMethod(ENABLE_AUDIO_METHOD, values);
  }

  Future<void> switchCamera(String sessionId) async {
    Map<String, Object> values = Map();

    values["sessionId"] = sessionId;

    await _conferenceModule.invokeMethod(SWITCH_CAMERA_METHOD, values);
  }

  Future<void> switchAudioOutput(int output) async {
    Map<String, Object> values = Map();

    values["output"] = output;

    await _conferenceModule.invokeMethod(SWITCH_AUDIO_OUTPUT, values);
  }

  Future<StreamSubscription<dynamic>> subscribeConferenceEvent(
      String eventName, eventMethod,
      {onErrorMethod}) async {
    return EventChannel(eventName)
        .receiveBroadcastStream(eventName)
        .listen(eventMethod, onError: onErrorMethod);
  }
}
