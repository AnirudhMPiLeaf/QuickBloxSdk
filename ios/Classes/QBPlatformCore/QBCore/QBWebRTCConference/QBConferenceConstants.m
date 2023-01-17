//
//  QBWebRTCConferenceConstants.m
//  quickblox_sdk
//
//  Created by Injoit on 11.02.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import "QBConferenceConstants.h"
#import <QuickbloxWebRTC/QuickbloxWebRTC.h>

const struct QBWebRTCConferenceEventKeysStruct QBConferenceEventKey = {
    .participantReceived = @"CONFERENCE_PARTICIPANT_RECEIVED",
    .participantLeft = @"CONFERENCE_PARTICIPANT_LEFT",
    .error = @"CONFERENCE_ERROR_RECEIVED",
    .close = @"CONFERENCE_CLOSED",
    .sessionState = @"CONFERENCE_STATE_CHANGED",
    .videoTrack = @"CONFERENCE_VIDEO_TRACK_RECEIVED"
};

@implementation QBConferenceConstants

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
        _rtcSessionState = @{ @"NEW"        : @(QBRTCConfereceStateNew),
                              @"PENDING"    : @(QBRTCConfereceStatePending),
                              @"CONNECTING" : @(QBRTCConfereceStateConnecting),
                              @"CONNECTED"  : @(QBRTCConfereceStateConnected),
                              @"CLOSED"     : @(QBRTCConfereceStateClosed) };
    });
    return _rtcSessionState;
}

+ (NSDictionary *)audioOutputs {
    static NSDictionary* _audioOutputs = nil;
    static dispatch_once_t audioOutputsToken;
    dispatch_once(&audioOutputsToken, ^{
        _audioOutputs = @{ @"EARSPEAKER": @(QB_CONFERENCE_EARSPEAKER),
                           @"LOUDSPEAKER": @(QB_CONFERENCE_LOUDSPEAKER),
                           @"HEADPHONES": @(QB_CONFERENCE_HEADPHONES),
                           @"BLUETOOTH": @(QB_CONFERENCE_BLUETOOTH) };
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

