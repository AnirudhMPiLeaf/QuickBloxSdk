import 'package:quickblox_sdk/models/qb_filter.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class QBFilterMapper {
  static Map<String, Object>? filterToMap(QBFilter? qbFilter) {
    if (qbFilter == null) {
      return null;
    }

    Map<String, Object> map = Map();

    if (qbFilter.field != null) {
      map["field"] = qbFilter.field as Object;
    }
    if (qbFilter.operator != null) {
      map["operator"] = qbFilter.operator as Object;
    }
    if (qbFilter.value != null) {
      map["value"] = qbFilter.value as Object;
    }
    if (qbFilter.type != null) {
      map["type"] = qbFilter.type as Object;
    }

    return map;
  }
}
