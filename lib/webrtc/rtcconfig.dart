/// Created by Injoit on 2021-01-06.
/// Copyright © 2019 Quickblox. All rights reserved.
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:quickblox_sdk/mappers/qb_ice_server_mapper.dart';
import 'package:quickblox_sdk/models/qb_ice_server.dart';

class RTCConfig {
  const RTCConfig();

  ///////////////////////////////////////////////////////////////////////////
  // QBRTCConfig MODULE
  ///////////////////////////////////////////////////////////////////////////

  //Channel name
  static const CHANNEL_NAME = "FlutterQBRTCConfigChannel";

  //QBRTCConfig Methods
  static const SET_ANSWER_TIME_INTERVAL_METHOD = "setAnswerTimeInterval";
  static const GET_ANSWER_TIME_INTERVAL_METHOD = "getAnswerTimeInterval";
  static const SET_DIALING_TIME_INTERVAL_METHOD = "setDialingTimeInterval";
  static const GET_DIALING_TIME_INTERVAL_METHOD = "getDialingTimeInterval";
  static const SET_RECONNECTION_TIME_INTERVAL_METHOD = "setReconnectionTimeInterval";
  static const GET_RECONNECTION_TIME_INTERVAL_METHOD = "getReconnectionTimeInterval";
  static const SET_ICE_SERVERS_METHOD = "setICEServers";
  static const GET_ICE_SERVERS_METHOD = "getICEServers";

  //Module
  static const _rtcConfigModule = const MethodChannel(CHANNEL_NAME);

  Future<void> setAnswerTimeInterval(int interval) async {
    await _rtcConfigModule.invokeMethod(SET_ANSWER_TIME_INTERVAL_METHOD, interval);
  }

  Future<int?> getAnswerTimeInterval() async {
    int? interval = await _rtcConfigModule.invokeMethod(GET_ANSWER_TIME_INTERVAL_METHOD, null);
    return interval;
  }

  Future<void> setDialingTimeInterval(int interval) async {
    await _rtcConfigModule.invokeMethod(SET_DIALING_TIME_INTERVAL_METHOD, interval);
  }

  Future<int?> getDialingTimeInterval() async {
    int? interval = await _rtcConfigModule.invokeMethod(GET_DIALING_TIME_INTERVAL_METHOD, null);
    return interval;
  }

  Future<void> setReconnectionTimeInterval(int interval) async {
    await _rtcConfigModule.invokeMethod(SET_RECONNECTION_TIME_INTERVAL_METHOD, interval);
  }

  Future<int?> getReconnectionTimeInterval() async {
    int? interval = await _rtcConfigModule.invokeMethod(GET_RECONNECTION_TIME_INTERVAL_METHOD, null);
    return interval;
  }

  Future<void> setIceServers(List<QBIceServer> iceServers) async {
    List<Map<String, Object>> iceServerMaps = [];

    for (final iceServer in iceServers) {
      Map<String, Object>? iceServerMap = QBIceServerMapper.qbIceServerToMap(iceServer);
      if (iceServerMap != null) {
        iceServerMaps.add(iceServerMap);
      }
    }

    await _rtcConfigModule.invokeMethod(SET_ICE_SERVERS_METHOD, iceServerMaps);
  }

  Future<List<QBIceServer>> getIceServers() async {
    List<dynamic> list = await _rtcConfigModule.invokeMethod(GET_ICE_SERVERS_METHOD, null);

    List<QBIceServer> servers = [];

    for (final map in list) {
      QBIceServer? iceServer = QBIceServerMapper.mapToQBIceServer(map);
      if (iceServer != null) {
        servers.add(iceServer);
      }
    }

    return servers;
  }
}
