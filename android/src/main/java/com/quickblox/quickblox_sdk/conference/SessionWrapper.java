package com.quickblox.quickblox_sdk.conference;

import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.conference.ConferenceSession;
import com.quickblox.conference.WsException;
import com.quickblox.conference.callbacks.ConferenceSessionCallbacks;
import com.quickblox.videochat.webrtc.BaseSession;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientVideoTracksCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionStateCallback;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;

import java.util.ArrayList;

/**
 * Created by Injoit on 2/3/21.
 * Copyright © 2020 Quickblox. All rights reserved.
 */
public class SessionWrapper {
    private final ConferenceSession session;
    private final String id;
    private ConferenceSessionCallbacks conferenceSessionListener;
    private QBRTCClientVideoTracksCallbacks<ConferenceSession> conferenceVideoTrackListener;
    private QBRTCSessionStateCallback<ConferenceSession> conferenceSessionStateListener;

    public SessionWrapper(final String id, final ConferenceSession session) {
        this.id = id;
        this.session = session;
    }

    public ConferenceSession getConferenceSession() {
        return session;
    }

    public String getId() {
        return id;
    }

    public void addSessionListener(final SessionListener sessionListener) {
        if (conferenceSessionListener != null) {
            removeSessionListener();
        }

        conferenceSessionListener = new ConferenceSessionCallbacks() {
            @Override
            public void onPublishersReceived(ArrayList<Integer> arrayList) {
                sessionListener.onPublisherReceived(id, arrayList.get(0));
            }

            @Override
            public void onPublisherLeft(Integer userId) {
                sessionListener.onPublisherLeft(id, userId);
            }

            @Override
            public void onMediaReceived(String s, boolean b) {
                //empty
            }

            @Override
            public void onSlowLinkReceived(boolean b, int i) {
                //empty
            }

            @Override
            public void onError(WsException e) {
                sessionListener.onError(id, e.getMessage());
            }

            @Override
            public void onSessionClosed(ConferenceSession conferenceSession) {
                sessionListener.onClosed(id);
            }
        };

        session.addConferenceSessionListener(this.conferenceSessionListener);
    }

    public void removeSessionListener() {
        if (conferenceSessionListener != null) {
            session.removeConferenceSessionListener(conferenceSessionListener);
        }
    }

    public void addVideoTrackListener(final VideoTrackListener videoTrackListener) {
        if (conferenceVideoTrackListener != null) {
            removeVideoTrackListener();
        }

        conferenceVideoTrackListener = new QBRTCClientVideoTracksCallbacks<ConferenceSession>() {
            @Override
            public void onLocalVideoTrackReceive(ConferenceSession conferenceSession, QBRTCVideoTrack qbrtcVideoTrack) {
                Integer localUserId = QBSessionManager.getInstance().getActiveSession().getUserId();
                videoTrackListener.onReceive(id, localUserId, qbrtcVideoTrack.enabled());
            }

            @Override
            public void onRemoteVideoTrackReceive(ConferenceSession conferenceSession, QBRTCVideoTrack qbrtcVideoTrack, Integer userId) {
                videoTrackListener.onReceive(id, userId, qbrtcVideoTrack.enabled());
            }
        };

        session.addVideoTrackCallbacksListener(this.conferenceVideoTrackListener);
    }

    public void removeVideoTrackListener() {
        if (conferenceVideoTrackListener != null) {
            session.removeVideoTrackCallbacksListener(conferenceVideoTrackListener);
        }
    }

    public void addSessionStateListener(final SessionStateListener sessionStateListener) {
        if (conferenceSessionStateListener != null) {
            removeSessionStateListener();
        }

        conferenceSessionStateListener = new QBRTCSessionStateCallback<ConferenceSession>() {
            @Override
            public void onStateChanged(ConferenceSession conferenceSession, BaseSession.QBRTCSessionState qbrtcSessionState) {
                int state = ConferenceConstants.getSessionState(qbrtcSessionState);
                sessionStateListener.onChanged(id, state);
            }

            @Override
            public void onConnectedToUser(ConferenceSession conferenceSession, Integer integer) {
                //empty
            }

            @Override
            public void onDisconnectedFromUser(ConferenceSession conferenceSession, Integer integer) {
                //empty
            }

            @Override
            public void onConnectionClosedForUser(ConferenceSession conferenceSession, Integer integer) {
                //empty
            }
        };

        session.addSessionCallbacksListener(this.conferenceSessionStateListener);
    }

    public void removeSessionStateListener() {
        if (conferenceSessionStateListener != null) {
            session.removeSessionCallbacksListener(conferenceSessionStateListener);
        }
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals;
        if (obj instanceof SessionWrapper) {
            equals = this.id.equals(((SessionWrapper) obj).id);
        } else {
            equals = super.equals(obj);
        }
        return equals;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + id.hashCode();
        return hash;
    }

    public interface SessionListener {
        void onPublisherReceived(String sessionId, Integer userId);

        void onPublisherLeft(String sessionId, Integer userId);

        void onError(String sessionId, String errorMessage);

        void onClosed(String sessionId);
    }

    public interface VideoTrackListener {
        void onReceive(String sessionId, Integer userId, boolean enabled);
    }

    public interface SessionStateListener {
        void onChanged(String sessionId, int state);
    }
}
