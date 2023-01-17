import 'package:quickblox_sdk/chat/module.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class QBChatDialogTypes {
  ///////////////////////////////////////////////////////////////////////////
  // DIALOG TYPES
  ///////////////////////////////////////////////////////////////////////////
  static const PUBLIC_CHAT = 1;
  static const GROUP_CHAT = 2;
  static const CHAT = 3;
}

class QBChatEvents {
  ///////////////////////////////////////////////////////////////////////////
  // EVENTS
  ///////////////////////////////////////////////////////////////////////////
  static const CONNECTED = Chat.CHANNEL_NAME + "/CONNECTED";
  static const CONNECTION_CLOSED = Chat.CHANNEL_NAME + "/CONNECTION_CLOSED";
  static const RECONNECTION_FAILED = Chat.CHANNEL_NAME + "/RECONNECTION_FAILED";
  static const RECONNECTION_SUCCESSFUL =
      Chat.CHANNEL_NAME + "/RECONNECTION_SUCCESSFUL";
  static const RECEIVED_NEW_MESSAGE =
      Chat.CHANNEL_NAME + "/RECEIVED_NEW_MESSAGE";
  static const RECEIVED_SYSTEM_MESSAGE =
      Chat.CHANNEL_NAME + "/RECEIVED_SYSTEM_MESSAGE";
  static const MESSAGE_DELIVERED = Chat.CHANNEL_NAME + "/MESSAGE_DELIVERED";
  static const MESSAGE_READ = Chat.CHANNEL_NAME + "/MESSAGE_READ";
  static const USER_IS_TYPING = Chat.CHANNEL_NAME + "/USER_IS_TYPING";
  static const USER_STOPPED_TYPING = Chat.CHANNEL_NAME + "/USER_STOPPED_TYPING";
}

class QBChatDialogSorts {
  ///////////////////////////////////////////////////////////////////////////
  // DIALOG SORTS
  ///////////////////////////////////////////////////////////////////////////
  static const LAST_MESSAGE_DATE_SENT = "last_message_date_sent";
}

class QBChatDialogFilterFields {
  ///////////////////////////////////////////////////////////////////////////
  // DIALOG FILTER FIELDS
  ///////////////////////////////////////////////////////////////////////////
  static const ID = "_id";
  static const TYPE = "type";
  static const NAME = "name";
  static const LAST_MESSAGE_DATE_SENT = "last_message_date_sent";
  static const CREATED_AT = "created_at";
  static const UPDATED_AT = "updated_at";
}

class QBChatDialogFilterOperators {
  ///////////////////////////////////////////////////////////////////////////
  // DIALOG FILTER OPERATORS
  ///////////////////////////////////////////////////////////////////////////
  static const LT = "lt";
  static const LTE = "lte";
  static const GT = "gt";
  static const GTE = "gte";
  static const NE = "ne";
  static const IN = "in";
  static const NIN = "nin";
  static const ALL = "all";
  static const CTN = "ctn";
}

class QBChatMessageSorts {
  ///////////////////////////////////////////////////////////////////////////
  // MESSAGE SORTS
  ///////////////////////////////////////////////////////////////////////////
  static const DATE_SENT = "date_sent";
}

class QBChatMessageFilterFields {
  ///////////////////////////////////////////////////////////////////////////
  // MESSAGE FILTER FIELDS
  ///////////////////////////////////////////////////////////////////////////
  static const ID = "_id";
  static const BODY = "message";
  static const DATE_SENT = "date_sent";
  static const SENDER_ID = "sender_id";
  static const RECIPIENT_ID = "recipient_id";
  static const ATTACHMENTS_TYPE = "attachments_type";
  static const UPDATED_AT = "updated_at";
}

class QBChatMessageFilterOperators {
  ///////////////////////////////////////////////////////////////////////////
  // MESSAGE FILTER OPERATORS
  ///////////////////////////////////////////////////////////////////////////
  static const LT = "lt";
  static const LTE = "lte";
  static const GT = "gt";
  static const GTE = "gte";
  static const NE = "ne";
  static const IN = "in";
  static const NIN = "nin";
  static const OR = "or";
  static const CTN = "ctn";
}
