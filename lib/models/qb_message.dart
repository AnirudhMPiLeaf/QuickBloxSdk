import 'package:quickblox_sdk/models/qb_attachment.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class QBMessage {
  String? id;
  List<QBAttachment?>? attachments;
  Map<String, String>? properties;
  int? dateSent;
  int? senderId;
  int? recipientId;
  List<int>? readIds;
  List<int>? deliveredIds;
  String? dialogId;
  bool? markable;
  bool? delayed;
  String? body;
}
