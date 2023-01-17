package com.quickblox.quickblox_sdk.webrtc;

import com.quickblox.videochat.webrtc.QBRTCTypes;

public class ReconnectionMapper {
    private ReconnectionMapper() {
        // private
    }

    static public int parseQBRTCReconnectionState(QBRTCTypes.QBRTCReconnectionState state) {
        int eventName;

        switch (state) {
            case QB_RTC_RECONNECTION_STATE_RECONNECTING:
                eventName = WebRTCConstants.ReconnectionStates.RECONNECTING;
                break;
            case QB_RTC_RECONNECTION_STATE_RECONNECTED:
                eventName = WebRTCConstants.ReconnectionStates.RECONNECTED;
                break;
            default:
                eventName = WebRTCConstants.ReconnectionStates.FAILED;
        }

        return eventName;
    }
}
