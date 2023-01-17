package com.quickblox.quickblox_sdk.conference;

import android.text.TextUtils;

import com.quickblox.conference.ConferenceSession;
import com.quickblox.videochat.webrtc.BaseSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;

import org.webrtc.RendererCommon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Injoit on 1/28/21.
 * Copyright © 2020 Quickblox. All rights reserved.
 */
class ConferenceMapper {

    private ConferenceMapper() {
        //empty
    }

    static Map<String, Object> qbConferenceSessionToMap(SessionWrapper sessionWrapper) {
        Map<String, Object> map = new HashMap<>();

        if (sessionWrapper == null) {
            return map;
        }

        if (!TextUtils.isEmpty(sessionWrapper.getId())) {
            map.put("id", sessionWrapper.getId());
        }

        if (sessionWrapper.getConferenceSession() != null) {
            ConferenceSession session = sessionWrapper.getConferenceSession();

            //roomId
            map.put("roomId", session.getDialogID());

            //type
            QBRTCTypes.QBConferenceType conferenceType = session.getConferenceType();
            Integer type = ConferenceConstants.getSessionType(conferenceType);
            map.put("type", type);

            //state
            BaseSession.QBRTCSessionState sessionState = session.getState();
            Integer state = ConferenceConstants.getSessionState(sessionState);

            //check on null because the android send the status QB_RTC_SESSION_GOING_TO_CLOSE and IOS
            //doesn't need this status
            if (state != null) {
                map.put("state", state);
            }

            //publishers
            List<Integer> publishers = new ArrayList<>(session.getActivePublishers());
            map.put("publishers", publishers);
        }

        return map;
    }

    static Map<String, Object> qBRTCVideoTrackToMap(QBRTCVideoTrack videoTrack, Integer userId, String sessionId) {
        Map<String, Object> map = new HashMap<>();

        if (userId != null) {
            map.put("userId", userId);
        }
        if (videoTrack != null) {
            map.put("enabled", videoTrack.enabled());
        }
        if (!TextUtils.isEmpty(sessionId)) {
            map.put("sessionId", sessionId);
        }

        return map;
    }

    static RendererCommon.ScalingType scalingTypeFromMap(Integer scalingTypeValue) {

        RendererCommon.ScalingType scalingType = null;

        if (scalingTypeValue != null) {
            if (scalingTypeValue.equals(ConferenceConstants.ViewScaleTypes.AUTO)) {
                scalingType = RendererCommon.ScalingType.SCALE_ASPECT_BALANCED;
            } else if (scalingTypeValue.equals(ConferenceConstants.ViewScaleTypes.FILL)) {
                scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;
            } else if (scalingTypeValue.equals(ConferenceConstants.ViewScaleTypes.FIT)) {
                scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FIT;
            } else {
                scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;
            }
        }

        return scalingType;
    }
}