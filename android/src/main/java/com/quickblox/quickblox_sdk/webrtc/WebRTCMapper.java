package com.quickblox.quickblox_sdk.webrtc;

import android.text.TextUtils;

import com.quickblox.videochat.webrtc.BaseSession;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCSessionDescription;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;

import org.webrtc.RendererCommon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
public class WebRTCMapper {

    private WebRTCMapper() {
        //empty
    }

    static Map<String, Object> qBRTCSessionToMap(QBRTCSession session) {
        Map<String, Object> map = new HashMap<>();

        if (session != null && session.getSessionDescription() != null) {
            QBRTCSessionDescription description = session.getSessionDescription();

            if (!TextUtils.isEmpty(description.getSessionId())) {
                String id = description.getSessionId();
                map.put("id", id);
            }
            if (session.getConferenceType() != null) {
                QBRTCTypes.QBConferenceType conferenceType = session.getConferenceType();
                Integer type = WebRTCConstants.getSessionType(conferenceType);
                map.put("type", type);
            }
            if (session.getState() != null) {
                BaseSession.QBRTCSessionState sessionState = session.getState();
                Integer state = WebRTCConstants.getSessionState(sessionState);

                //check on null because the android send the status QB_RTC_SESSION_GOING_TO_CLOSE and IOS
                //doesn't need this status
                if (state != null) {
                    map.put("state", state);
                }
            }
            if (description.getCallerID() != null) {
                Integer initiatorId = description.getCallerID();
                map.put("initiatorId", initiatorId);
            }
            if (description.getOpponents() != null && description.getOpponents().size() > 0) {
                List<Integer> opponentsIdsList = description.getOpponents();
                map.put("opponentsIds", opponentsIdsList);
            }
        }

        return map;
    }

    static Map userInfoToMap(QBRTCSession session) {
        Map<String, Object> map = new HashMap<>();

        if (session != null && session.getSessionDescription() != null
                && session.getSessionDescription().getUserInfo() != null
                && session.getSessionDescription().getUserInfo().size() > 0) {
            Map<String, String> userInfo = session.getSessionDescription().getUserInfo();

            for (Map.Entry<String, String> entry : userInfo.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                map.put(key, value);
            }
        }

        return map;
    }

    static Map qBRTCVideoTrackToMap(QBRTCVideoTrack videoTrack, Integer userId) {
        Map<String, Object> map = new HashMap<>();

        if (userId != null) {
            map.put("userId", userId);
        }
        if (videoTrack != null) {
            map.put("enabled", videoTrack.enabled());
        }

        return map;
    }

    static RendererCommon.ScalingType scalingTypeFromMap(Integer scalingTypeValue) {

        RendererCommon.ScalingType scalingType = null;

        if (scalingTypeValue != null) {
            if (scalingTypeValue.equals(WebRTCConstants.ViewScaleTypes.AUTO)) {
                scalingType = RendererCommon.ScalingType.SCALE_ASPECT_BALANCED;
            } else if (scalingTypeValue.equals(WebRTCConstants.ViewScaleTypes.FILL)) {
                scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;
            } else if (scalingTypeValue.equals(WebRTCConstants.ViewScaleTypes.FIT)) {
                scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FIT;
            } else {
                scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;
            }
        }

        return scalingType;
    }
}