import 'package:quickblox_sdk/webrtc/module.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class QBRTCSessionTypes {
  ///////////////////////////////////////////////////////////////////////////
  // SESSION TYPES
  ///////////////////////////////////////////////////////////////////////////
  static const VIDEO = 1;
  static const AUDIO = 2;
}

class QBRTCSessionStates {
  ///////////////////////////////////////////////////////////////////////////
  // SESSION STATES
  ///////////////////////////////////////////////////////////////////////////
  static const NEW = 0;
  static const PENDING = 1;
  static const CONNECTING = 2;
  static const CONNECTED = 3;
  static const CLOSED = 4;
}

class QBRTCEventTypes {
  ///////////////////////////////////////////////////////////////////////////
  // EVENT TYPES
  ///////////////////////////////////////////////////////////////////////////
  static const CALL = WebRTC.CHANNEL_NAME + "/CALL";
  static const CALL_END = WebRTC.CHANNEL_NAME + "/CALL_END";
  static const NOT_ANSWER = WebRTC.CHANNEL_NAME + "/NOT_ANSWER";
  static const REJECT = WebRTC.CHANNEL_NAME + "/REJECT";
  static const ACCEPT = WebRTC.CHANNEL_NAME + "/ACCEPT";
  static const HANG_UP = WebRTC.CHANNEL_NAME + "/HANG_UP";
  static const RECEIVED_VIDEO_TRACK = WebRTC.CHANNEL_NAME + "/RECEIVED_VIDEO_TRACK";
  static const PEER_CONNECTION_STATE_CHANGED = WebRTC.CHANNEL_NAME + "/PEER_CONNECTION_STATE_CHANGED";
  static const RECONNECTION_STATE_CHANGED = WebRTC.CHANNEL_NAME + "/RECONNECTION_STATE_CHANGED";
}

class QBRTCPeerConnectionStates {
  ///////////////////////////////////////////////////////////////////////////
  // CONNECTIONS STATES
  ///////////////////////////////////////////////////////////////////////////
  static const NEW = 0;
  static const CONNECTED = 1;
  static const FAILED = 2;
  static const DISCONNECTED = 3;
  static const CLOSED = 4;
}

class QBRTCReconnectionStates {
  ///////////////////////////////////////////////////////////////////////////
  // RECONNECTIONS STATES
  ///////////////////////////////////////////////////////////////////////////
  static const RECONNECTING = 0;
  static const RECONNECTED = 1;
  static const FAILED = 2;
}

class QBRTCViewScaleTypes {
  ///////////////////////////////////////////////////////////////////////////
  // VIEW SCALE TYPES
  ///////////////////////////////////////////////////////////////////////////
  static const FILL = 0;
  static const FIT = 1;
  static const AUTO = 2;
}

class QBRTCAudioOutputTypes {
  ///////////////////////////////////////////////////////////////////////////
  // AUDIO OUTPUTS
  ///////////////////////////////////////////////////////////////////////////
  static const EARSPEAKER = 0;
  static const LOUDSPEAKER = 1;
  static const HEADPHONES = 2;
  static const BLUETOOTH = 3;
}
