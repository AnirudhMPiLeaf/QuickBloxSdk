///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class QBEvent {
  int? id;
  String? name;
  bool? active;
  String? notificationType;
  int? pushType;
  double? date;
  int? endDate;
  String? period;
  int? occuredCount;
  int? senderId;
  List<String>? recipientsIds;
  List<String>? recipientsTagsAny;
  List<String>? recipientsTagsAll;
  List<String>? recipientsTagsExclude;
  String? payload;
}
