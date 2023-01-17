//
//  QBWebRTCConstants.m
//  quickblox_sdk
//
//  Created by Injoit on 03.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import <QuickbloxWebRTC/QuickbloxWebRTC.h>

#import "QBWebRTCConstants.h"

const struct QBWebRTCKeysStruct QBWebRTCKey = {
    .userInfo = @"userInfo",
    .userId = @"userId",
    .enabled = @"enabled",
    .sessionId = @"sessionId",
    .session = @"session",
    .enable = @"enable",
    .output = @"output",
    .state = @"state",
};

const struct QBWebRTCCallEventKeysStruct QBCallEventKey = {
    .call = @"CALL",
    .notAnswer = @"NOT_ANSWER",
    .reject = @"REJECT",
    .accept = @"ACCEPT",
    .hangUp = @"HANG_UP",
    .callEnd = @"CALL_END",
    .connectionState = @"PEER_CONNECTION_STATE_CHANGED",
    .reconnectionState = @"RECONNECTION_STATE_CHANGED",
    .remoteVideoTrack = @"RECEIVED_VIDEO_TRACK"
};

@implementation QBWebRTCConstants

+ (NSDictionary *)sessionType {
    static NSDictionary* _rtcSessionType = nil;
    static dispatch_once_t rtcSessionTypeToken;
    dispatch_once(&rtcSessionTypeToken, ^{
        _rtcSessionType = @{ @"VIDEO" : @(QBRTCConferenceTypeVideo),
                             @"AUDIO" : @(QBRTCConferenceTypeAudio) };
    });
    return _rtcSessionType;
}

+ (NSDictionary *)sessionState {
    static NSDictionary* _rtcSessionState = nil;
    static dispatch_once_t rtcSessionStateToken;
    dispatch_once(&rtcSessionStateToken, ^{
        _rtcSessionState = @{ @"NEW"        : @(QBRTCSessionStateNew),
                              @"PENDING"    : @(QBRTCSessionStatePending),
                              @"CONNECTING" : @(QBRTCSessionStateConnecting),
                              @"CONNECTED"  : @(QBRTCSessionStateConnected),
                              @"CLOSED"     : @(QBRTCSessionStateClosed) };
    });
    return _rtcSessionState;
}

+ (NSDictionary *)connectionState {
    static NSDictionary* _rtcSessionState = nil;
    static dispatch_once_t rtcSessionStateToken;
    dispatch_once(&rtcSessionStateToken, ^{
        _rtcSessionState = @{ @"NEW"          : @(QBRTCPeerStateNew),
                              @"CONNECTED"    : @(QBRTCPeerStateConnected),
                              @"DISCONNECTED" : @(QBRTCPeerStateDisconnected),
                              @"FAILED"       : @(QBRTCPeerStateFailed),
                              @"CLOSED"       : @(QBRTCPeerStateClosed) };
    });
    return _rtcSessionState;
}

+ (NSDictionary *)reconnectionState {
    static NSDictionary* _rtcReconnectionState = nil;
    static dispatch_once_t rtcReconnectionStateToken;
    dispatch_once(&rtcReconnectionStateToken, ^{
      _rtcReconnectionState = @{ @"RECONNECTING" : @(QBRTCReconnectionStateReconnecting),
                                 @"RECONNECTED"  : @(QBRTCReconnectionStateReconnected),
                                 @"FAILED"       : @(QBRTCReconnectionStateFailed) };
    });
    return _rtcReconnectionState;
}

+ (NSDictionary *)audioOutputs {
    static NSDictionary* _audioOutputs = nil;
    static dispatch_once_t audioOutputsToken;
    dispatch_once(&audioOutputsToken, ^{
        _audioOutputs = @{ @"EARSPEAKER": @(QB_AUDIO_OUTPUT_EARSPEAKER),
                           @"LOUDSPEAKER": @(QB_AUDIO_OUTPUT_LOUDSPEAKER),
                           @"HEADPHONES": @(QB_AUDIO_OUTPUT_HEADPHONES),
                           @"BLUETOOTH": @(QB_AUDIO_OUTPUT_BLUETOOTH) };
    });
    return _audioOutputs;
}

+ (NSDictionary *)scaleTypes {
    static NSDictionary* _scaleTypes = nil;
    static dispatch_once_t scaleTypesToken;
    dispatch_once(&scaleTypesToken, ^{
      _scaleTypes = @{ @"FILL": @(0),
                       @"FIT": @(1),
                       @"AUTO": @(2) };
    });
    return _scaleTypes;
}

@end
