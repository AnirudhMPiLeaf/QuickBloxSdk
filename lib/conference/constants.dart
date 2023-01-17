import 'package:quickblox_sdk/conference/module.dart';

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class QBConferenceSessionTypes {
  ///////////////////////////////////////////////////////////////////////////
  // SESSION TYPES
  ///////////////////////////////////////////////////////////////////////////
  static const VIDEO = 1;
  static const AUDIO = 2;
}

class QBConferenceSessionStates {
  ///////////////////////////////////////////////////////////////////////////
  // SESSION STATES
  ///////////////////////////////////////////////////////////////////////////
  static const NEW = 0;
  static const PENDING = 1;
  static const CONNECTING = 2;
  static const CONNECTED = 3;
  static const CLOSED = 4;
}

class QBConferenceEventTypes {
  ///////////////////////////////////////////////////////////////////////////
  // EVENT TYPES
  ///////////////////////////////////////////////////////////////////////////
  static const CONFERENCE_VIDEO_TRACK_RECEIVED =
      Conference.CHANNEL_NAME + "/CONFERENCE_VIDEO_TRACK_RECEIVED";
  static const CONFERENCE_PARTICIPANT_RECEIVED =
      Conference.CHANNEL_NAME + "/CONFERENCE_PARTICIPANT_RECEIVED";
  static const CONFERENCE_PARTICIPANT_LEFT =
      Conference.CHANNEL_NAME + "/CONFERENCE_PARTICIPANT_LEFT";
  static const CONFERENCE_ERROR_RECEIVED =
      Conference.CHANNEL_NAME + "/CONFERENCE_ERROR_RECEIVED";
  static const CONFERENCE_CLOSED =
      Conference.CHANNEL_NAME + "/CONFERENCE_CLOSED";
  static const CONFERENCE_STATE_CHANGED =
      Conference.CHANNEL_NAME + "/CONFERENCE_STATE_CHANGED";
}

class QBConferenceViewScaleTypes {
  ///////////////////////////////////////////////////////////////////////////
  // VIEW SCALE TYPES
  ///////////////////////////////////////////////////////////////////////////
  static const FILL = 0;
  static const FIT = 1;
  static const AUTO = 2;
}

class QBConferenceAudioOutputTypes {
  ///////////////////////////////////////////////////////////////////////////
  // AUDIO OUTPUTS
  ///////////////////////////////////////////////////////////////////////////
  static const EARSPEAKER = 0;
  static const LOUDSPEAKER = 1;
  static const HEADPHONES = 2;
  static const BLUETOOTH = 3;
}
