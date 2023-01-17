import 'dart:async';

import 'package:flutter/services.dart';
import 'package:quickblox_sdk/mappers/qb_subscription_mapper.dart';
import 'package:quickblox_sdk/models/qb_subscription.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class Push {
  const Push();

  ///////////////////////////////////////////////////////////////////////////
  // PUSH MODULE
  ///////////////////////////////////////////////////////////////////////////

  //Channel name
  static const CHANNEL_NAME = "FlutterQBPushSubscriptionsChannel";

  //Methods
  static const CREATE_METHOD = "create";
  static const GET_METHOD = "get";
  static const REMOVE_METHOD = "remove";

  //Module
  static const _pushModule = const MethodChannel(CHANNEL_NAME);

  Future<List<QBSubscription?>> create(
      String deviceToken, String pushChannel) async {
    Map<String, Object> data = Map();

    data["deviceToken"] = deviceToken;
    data["pushChannel"] = pushChannel;

    List<Object?> list = await _pushModule.invokeMethod(CREATE_METHOD, data);

    List<QBSubscription?> subscriptionList = [];

    for (final item in list) {
      QBSubscription? subscription = QBSubscriptionMapper.mapToQBSubscription(
          item as Map<dynamic, dynamic>);
      subscriptionList.add(subscription);
    }

    return subscriptionList;
  }

  Future<List<QBSubscription?>> get() async {
    List<Object?> list = await _pushModule.invokeMethod(GET_METHOD, null);

    List<QBSubscription?> subscriptionList = [];

    for (final item in list) {
      QBSubscription? subscription = QBSubscriptionMapper.mapToQBSubscription(
          item as Map<dynamic, dynamic>);
      subscriptionList.add(subscription);
    }

    return subscriptionList;
  }

  Future<void> remove(int id) async {
    Map<String, Object> data = Map();

    data["id"] = id;

    await _pushModule.invokeMethod(REMOVE_METHOD, data);
  }
}
