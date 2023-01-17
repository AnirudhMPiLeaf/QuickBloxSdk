import 'package:quickblox_sdk/models/qb_sort.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class QBSortMapper {
  static Map<String, Object>? sortToMap(QBSort? qbSort) {
    if (qbSort == null) {
      return null;
    }

    Map<String, Object> map = Map();

    if (qbSort.field != null) {
      map["field"] = qbSort.field as Object;
    }
    if (qbSort.ascending != null) {
      map["ascending"] = qbSort.ascending as Object;
    }
    if (qbSort.type != null) {
      map["type"] = qbSort.type as Object;
    }

    return map;
  }
}
