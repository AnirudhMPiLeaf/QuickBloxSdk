package com.quickblox.quickblox_sdk.conference;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import com.quickblox.videochat.webrtc.BaseSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Injoit on 1/28/21.
 * Copyright © 2020 Quickblox. All rights reserved.
 */
public class ConferenceConstants {

    private ConferenceConstants() {
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
    // EVENT TYPES
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            Events.CONFERENCE_VIDEO_TRACK_RECEIVED,
            Events.CONFERENCE_PARTICIPANT_RECEIVED,
            Events.CONFERENCE_PARTICIPANT_LEFT,
            Events.CONFERENCE_ERROR_RECEIVED,
            Events.CONFERENCE_CLOSED,
            Events.CONFERENCE_STATE_CHANGED
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface Events {
        String CONFERENCE_VIDEO_TRACK_RECEIVED = ConferenceModule.CHANNEL_NAME + "/CONFERENCE_VIDEO_TRACK_RECEIVED";
        String CONFERENCE_PARTICIPANT_RECEIVED = ConferenceModule.CHANNEL_NAME + "/CONFERENCE_PARTICIPANT_RECEIVED";
        String CONFERENCE_PARTICIPANT_LEFT = ConferenceModule.CHANNEL_NAME + "/CONFERENCE_PARTICIPANT_LEFT";
        String CONFERENCE_ERROR_RECEIVED = ConferenceModule.CHANNEL_NAME + "/CONFERENCE_ERROR_RECEIVED";
        String CONFERENCE_CLOSED = ConferenceModule.CHANNEL_NAME + "/CONFERENCE_CLOSED";
        String CONFERENCE_STATE_CHANGED = ConferenceModule.CHANNEL_NAME + "/CONFERENCE_STATE_CHANGED";
    }

    static List<String> getAllEvents() {
        List<String> events = new ArrayList<>();

        events.add(Events.CONFERENCE_VIDEO_TRACK_RECEIVED);
        events.add(Events.CONFERENCE_PARTICIPANT_RECEIVED);
        events.add(Events.CONFERENCE_PARTICIPANT_LEFT);
        events.add(Events.CONFERENCE_ERROR_RECEIVED);
        events.add(Events.CONFERENCE_CLOSED);
        events.add(Events.CONFERENCE_STATE_CHANGED);

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

    ///////////////////////////////////////////////////////////////////////////
    // AUDIO OUTPUT TYPES
    ///////////////////////////////////////////////////////////////////////////
    @IntDef({
            AudioOutputTypes.EARSPEAKER,
            AudioOutputTypes.LOUDSPEAKER,
            AudioOutputTypes.HEADPHONES,
            AudioOutputTypes.BLUETOOTH
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface AudioOutputTypes {
        int EARSPEAKER = 0;
        int LOUDSPEAKER = 1;
        int HEADPHONES = 2;
        int BLUETOOTH = 3;
    }

    static ConferenceAudioManager.AudioDevice appRTCAudioDeviceFromValue(int value) {
        ConferenceAudioManager.AudioDevice audioDevice = null;
        if (value == ConferenceConstants.AudioOutputTypes.BLUETOOTH) {
            audioDevice = ConferenceAudioManager.AudioDevice.BLUETOOTH;
        }
        if (value == ConferenceConstants.AudioOutputTypes.EARSPEAKER) {
            audioDevice = ConferenceAudioManager.AudioDevice.EARPIECE;
        }
        if (value == ConferenceConstants.AudioOutputTypes.HEADPHONES) {
            audioDevice = ConferenceAudioManager.AudioDevice.WIRED_HEADSET;
        }
        if (value == ConferenceConstants.AudioOutputTypes.LOUDSPEAKER) {
            audioDevice = ConferenceAudioManager.AudioDevice.SPEAKER_PHONE;
        }
        return audioDevice;
    }

    static String getDeviceNameFromValue(Integer value) {
        String name = null;
        if (value.equals(ConferenceConstants.AudioOutputTypes.EARSPEAKER)) {
            name = "EARSPEAKER";
        }
        if (value.equals(ConferenceConstants.AudioOutputTypes.LOUDSPEAKER)) {
            name = "LOUDSPEAKER";
        }
        if (value.equals(ConferenceConstants.AudioOutputTypes.HEADPHONES)) {
            name = "HEADPHONES";
        }
        if (value.equals(ConferenceConstants.AudioOutputTypes.BLUETOOTH)) {
            name = "BLUETOOTH";

        }
        return name;
    }
}