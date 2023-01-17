import 'dart:collection';

import '../models/qb_messages_counter.dart';

class QBMessagesCounterMapper {
  QBMessagesCounterMapper._();

  static QBMessagesCounter? mapToQBMessageCounter(Map<String, Object>? map) {
    if (map == null || map.isEmpty) {
      return null;
    }

    QBMessagesCounter counter = QBMessagesCounter();

    if (map.containsKey("totalCount")) {
      counter.total = map["totalCount"] as int?;
    }

    if (map.containsKey("dialogsCount")) {
      List<Object?>? listObjects = map["dialogsCount"] as List<Object?>?;
      counter.dialogs = listObjectToListDialogsCount(listObjects);
    }

    return counter;
  }

  static List<Map<String, int>>? listObjectToListDialogsCount(List<Object?>? listObjects) {
    List<Map<String, int>> resultList = [];

    if (listObjects == null || listObjects.isEmpty) {
      return null;
    }

    listObjects.forEach((element) {
      Map<String, int> dialogMap = HashMap.from(element as Map<Object?, Object?>);
      resultList.add(dialogMap);
    });

    return resultList;
  }
}
