package com.quickblox.quickblox_sdk.webrtc;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import com.quickblox.videochat.webrtc.BaseSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class WebRTCConstants {

    private WebRTCConstants() {
        //empty
    }

    ///////////////////////////////////////////////////////////////////////////
    // SESSION TYPES
    ///////////////////////////////////////////////////////////////////////////
    @IntDef({
            SessionTypes.VIDEO,
            SessionTypes.AUDIO
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface SessionTypes {
        int VIDEO = 1;
        int AUDIO = 2;
    }

    static Integer getSessionType(QBRTCTypes.QBConferenceType type) {
        Integer sessionType = null;
        switch (type) {
            case QB_CONFERENCE_TYPE_AUDIO:
                sessionType = SessionTypes.AUDIO;
                break;
            case QB_CONFERENCE_TYPE_VIDEO:
                sessionType = SessionTypes.VIDEO;
                break;
        }
        return sessionType;
    }

    static QBRTCTypes.QBConferenceType getSessionType(Integer type) {
        QBRTCTypes.QBConferenceType sessionType = null;
        switch (type) {
            case SessionTypes.VIDEO:
                sessionType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;
                break;
            case SessionTypes.AUDIO:
                sessionType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
                break;
        }
        return sessionType;
    }

    ///////////////////////////////////////////////////////////////////////////
    // SESSION STATES
    ///////////////////////////////////////////////////////////////////////////
    @IntDef({
            SessionStates.NEW,
            SessionStates.PENDING,
            SessionStates.CONNECTING,
            SessionStates.CONNECTED,
            SessionStates.CLOSED
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface SessionStates {
        int NEW = 0;
        int PENDING = 1;
        int CONNECTING = 2;
        int CONNECTED = 3;
        int CLOSED = 4;
    }

    static Integer getSessionState(BaseSession.QBRTCSessionState state) {
        Integer sessionState = null;
        switch (state) {
            case QB_RTC_SESSION_NEW:
                sessionState = SessionStates.NEW;
                break;
            case QB_RTC_SESSION_PENDING:
                sessionState = SessionStates.PENDING;
                break;
            case QB_RTC_SESSION_CONNECTING:
                sessionState = SessionStates.CONNECTING;
                break;
            case QB_RTC_SESSION_CONNECTED:
                sessionState = SessionStates.CONNECTED;
                break;
            case QB_RTC_SESSION_CLOSED:
                sessionState = SessionStates.CLOSED;
                break;
        }
        return sessionState;
    }

    ///////////////////////////////////////////////////////////////////////////
    // CALL EVENT TYPES
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            Events.CALL,
            Events.CALL_END,
            Events.NOT_ANSWER,
            Events.REJECT,
            Events.ACCEPT,
            Events.HANG_UP,
            Events.RECEIVED_VIDEO_TRACK,
            Events.PEER_CONNECTION_STATE_CHANGED
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface Events {
        String CALL = WebRTCModule.CHANNEL_NAME + "/CALL";
        String CALL_END = WebRTCModule.CHANNEL_NAME + "/CALL_END";
        String NOT_ANSWER = WebRTCModule.CHANNEL_NAME + "/NOT_ANSWER";
        String REJECT = WebRTCModule.CHANNEL_NAME + "/REJECT";
        String ACCEPT = WebRTCModule.CHANNEL_NAME + "/ACCEPT";
        String HANG_UP = WebRTCModule.CHANNEL_NAME + "/HANG_UP";
        String RECEIVED_VIDEO_TRACK = WebRTCModule.CHANNEL_NAME + "/RECEIVED_VIDEO_TRACK";
        String PEER_CONNECTION_STATE_CHANGED = WebRTCModule.CHANNEL_NAME + "/PEER_CONNECTION_STATE_CHANGED";
        String RECONNECTION_STATE_CHANGED = WebRTCModule.CHANNEL_NAME + "/RECONNECTION_STATE_CHANGED";
    }

    static List<String> getAllEvents() {
        List<String> events = new ArrayList<>();

        events.add(Events.CALL);
        events.add(Events.CALL_END);
        events.add(Events.NOT_ANSWER);
        events.add(Events.REJECT);
        events.add(Events.ACCEPT);
        events.add(Events.HANG_UP);
        events.add(Events.RECEIVED_VIDEO_TRACK);
        events.add(Events.PEER_CONNECTION_STATE_CHANGED);
        events.add(Events.RECONNECTION_STATE_CHANGED);

        return events;
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONNECTIONS STATES
    ///////////////////////////////////////////////////////////////////////////
    @IntDef({
            PeerConnectionStates.NEW,
            PeerConnectionStates.CONNECTED,
            PeerConnectionStates.FAILED,
            PeerConnectionStates.DISCONNECTED,
            PeerConnectionStates.CLOSED
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface PeerConnectionStates {
        int NEW = 0;
        int CONNECTED = 1;
        int FAILED = 2;
        int DISCONNECTED = 3;
        int CLOSED = 4;
    }

    ///////////////////////////////////////////////////////////////////////////
    // RECONNECTION STATES
    ///////////////////////////////////////////////////////////////////////////
    @IntDef({
            ReconnectionStates.RECONNECTING,
            ReconnectionStates.RECONNECTED,
            ReconnectionStates.FAILED,
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface ReconnectionStates {
        int RECONNECTING = 0;
        int RECONNECTED = 1;
        int FAILED = 2;
    }

    ///////////////////////////////////////////////////////////////////////////
    // VIEW SCALE TYPES
    ///////////////////////////////////////////////////////////////////////////
    @IntDef({
            ViewScaleTypes.FILL,
            ViewScaleTypes.FIT,
            ViewScaleTypes.AUTO
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface ViewScaleTypes {
        int FILL = 0;
        int FIT = 1;
        int AUTO = 2;
    }
}