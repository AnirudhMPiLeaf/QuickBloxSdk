import 'dart:async';

import 'package:flutter/services.dart';
import 'package:quickblox_sdk/mappers/qb_session_mapper.dart';
import 'package:quickblox_sdk/mappers/qb_user_mapper.dart';
import 'package:quickblox_sdk/models/qb_session.dart';
import 'package:quickblox_sdk/models/qb_user.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class Auth {
  const Auth();

  ///////////////////////////////////////////////////////////////////////////
  // AUTH MODULE
  ///////////////////////////////////////////////////////////////////////////

  //Channel name
  static const CHANNEL_NAME = "FlutterQBAuthChannel";

  //Methods
  static const LOGIN_METHOD = "login";
  static const LOGOUT_METHOD = "logout";
  static const LOGIN_WITH_EMAIL_METHOD = "loginWithEmail";
  static const LOGIN_WITH_FACEBOOK_METHOD = "loginWithFacebook";
  static const LOGIN_WITH_FIREBASE_METHOD = "loginWithFirebase";
  static const SET_SESSION_METHOD = "setSession";
  static const GET_SESSION_METHOD = "getSession";
  static const START_SESSION_WITH_TOKEN_METHOD = "startSessionWithToken";
  static const CLEAR_SESSION = "clearSession";

  //Module
  static const _authModule = const MethodChannel(CHANNEL_NAME);

  Future<QBLoginResult> login(String login, String password) async {
    Map<String, Object> data = Map();

    data["login"] = login;
    data["password"] = password;

    Map<Object?, Object?> map =
        await _authModule.invokeMethod(LOGIN_METHOD, data);

    Map<Object?, Object?> userMap =
        Map.from(map["user"] as Map<Object?, Object?>);
    Map<Object?, Object?> sessionMap =
        Map.from(map["session"] as Map<Object?, Object?>);

    QBUser? qbUser = QBUserMapper.mapToQBUser(userMap);
    QBSession? qbSession = QBSessionMapper.mapToQBSession(sessionMap);

    QBLoginResult result = QBLoginResult(qbUser, qbSession);

    return result;
  }

  Future<void> logout() async {
    await _authModule.invokeMethod(LOGOUT_METHOD);
  }

  Future<QBLoginResult> loginWithEmail(String email, String password) async {
    Map<Object?, Object?> map = await _authModule
        .invokeMethod(LOGIN_WITH_EMAIL_METHOD, [email, password]);

    Map<Object?, Object?> userMap =
        Map.from(map["user"] as Map<Object?, Object?>);
    Map<Object?, Object?> sessionMap =
        Map.from(map["session"] as Map<Object?, Object?>);

    QBUser? qbUser = QBUserMapper.mapToQBUser(userMap);
    QBSession? qbSession = QBSessionMapper.mapToQBSession(sessionMap);

    QBLoginResult result = QBLoginResult(qbUser, qbSession);

    return result;
  }

  Future<QBLoginResult> loginWithFacebook(String token) async {
    Map<Object?, Object?> map =
        await _authModule.invokeMethod(LOGIN_WITH_FACEBOOK_METHOD, [token]);

    Map<Object?, Object?> userMap =
        Map.from(map["user"] as Map<Object?, Object?>);
    Map<Object?, Object?> sessionMap =
        Map.from(map["session"] as Map<Object?, Object?>);

    QBUser? qbUser = QBUserMapper.mapToQBUser(userMap);
    QBSession? qbSession = QBSessionMapper.mapToQBSession(sessionMap);

    QBLoginResult result = QBLoginResult(qbUser, qbSession);

    return result;
  }

  Future<QBLoginResult> loginWithFirebase(
      String projectId, String token) async {
    Map<Object?, Object?> map = await _authModule
        .invokeMethod(LOGIN_WITH_FIREBASE_METHOD, [projectId, token]);

    Map<Object?, Object?> userMap =
        Map.from(map["user"] as Map<Object?, Object?>);
    Map<Object?, Object?> sessionMap =
        Map.from(map["session"] as Map<Object?, Object?>);

    QBUser? qbUser = QBUserMapper.mapToQBUser(userMap);
    QBSession? qbSession = QBSessionMapper.mapToQBSession(sessionMap);

    QBLoginResult result = QBLoginResult(qbUser, qbSession);

    return result;
  }

  Future<QBSession?> setSession(QBSession qbSession) async {
    Map<String, Object>? data = QBSessionMapper.qbSessionToMap(qbSession);

    Map<Object?, Object?> map =
        await _authModule.invokeMethod(SET_SESSION_METHOD, data);

    QBSession? session = QBSessionMapper.mapToQBSession(map);

    return session;
  }

  Future<QBSession?> getSession() async {
    Map<Object?, Object?> map =
        await _authModule.invokeMethod(GET_SESSION_METHOD);

    QBSession? session = QBSessionMapper.mapToQBSession(map);

    return session;
  }

  Future<dynamic> clearSession() async {
    await _authModule.invokeMethod(CLEAR_SESSION);
  }

  Future<QBSession?> startSessionWithToken(String token) async {
    Map<Object?, Object?> map = await _authModule
        .invokeMethod(START_SESSION_WITH_TOKEN_METHOD, [token]);

    QBSession? session = QBSessionMapper.mapToQBSession(map);

    return session;
  }

  Future<StreamSubscription<dynamic>> subscribeAuthEvent(
      String eventName, eventMethod,
      {onErrorMethod}) async {
    return EventChannel(eventName)
        .receiveBroadcastStream(eventName)
        .listen(eventMethod, onError: onErrorMethod);
  }
}

//todo need to refactor to more better solution like tuple
class QBLoginResult {
  QBLoginResult(this.qbUser, this.qbSession);

  QBUser? qbUser;
  QBSession? qbSession;
}
