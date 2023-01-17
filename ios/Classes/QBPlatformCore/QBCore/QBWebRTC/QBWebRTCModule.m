//
//  QBWebRTCModule.m
//  quickblox_sdk
//
//  Created by Injoit on 03.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import "QBWebRTCModule.h"
#import "QBRTCSession+QBSerializer.h"
#import "QBWebRTCConstants.h"

@interface QBWebRTCModule () <QBRTCClientDelegate>

@end

@interface QBWebRTCModule (Call)
@end

@interface QBWebRTCModule (Connection)
@end

@interface QBWebRTCModule (Video)
@end

@implementation QBWebRTCModule

- (instancetype)init {
    self = [super init];
    if (self) {
        [QBRTCClient initializeRTC];
        _cache = @{}.mutableCopy;
        [[QBRTCClient instance] addDelegate:self];
    }
    return self;
}

- (void)dealloc {
    [[QBRTCClient instance] removeDelegate:self];
}

- (NSArray<NSString *> *)events {
    return @[ QBCallEventKey.call,
              QBCallEventKey.notAnswer,
              QBCallEventKey.reject,
              QBCallEventKey.accept,
              QBCallEventKey.hangUp,
              QBCallEventKey.callEnd,
              QBCallEventKey.connectionState,
              QBCallEventKey.reconnectionState,
              QBCallEventKey.remoteVideoTrack ];
}

- (void)init:(QBResolveBlock)resolve
    rejecter:(QBRejectBlock)reject {
    //This method used in the Android platform
    if (resolve) {
        resolve(nil);
    }
}

- (void)release:(QBResolveBlock)resolve
       rejecter:(QBRejectBlock)reject {
    if (QBRTCAudioSession.instance.isActive) { [QBRTCAudioSession.instance setActive:NO]; }
    if (resolve) {
        resolve(nil);
    }
}

- (void)subscribeEvents:(NSDictionary *)info
               resolver:(QBResolveBlock)resolve
               rejecter:(QBRejectBlock)reject {
    if (resolve) {
        resolve(nil);
    }
}


- (void)unsubscribeEvents:(NSDictionary *)info
                 resolver:(QBResolveBlock)resolve
                 rejecter:(QBRejectBlock)reject {
    if (resolve) {
        resolve(nil);
    }
}

- (void)getSession:(NSDictionary *)info
          resolver:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject {
    [self sessionWithInfo:info success:^(QBRTCSession *session) {
        [session toQBResultDataWithResolver:resolve rejecter:reject];
    } rejecter:reject];
}

- (void)call:(NSDictionary *)info
    resolver:(QBResolveBlock)resolve
    rejecter:(QBRejectBlock)reject {
    if (!QBChat.instance.isConnected) {
        [NSError reject:reject
                message:@"You have to be logged in in order to use Chat API"];
        return;
    }
    NSObject *opponentsIdsObject = info[QBRTCSessionKey.opponentsIds];
    if ([NSError reject:reject
           checkerClass:NSArray.class
                 object:opponentsIdsObject
              objectKey:QBRTCSessionKey.opponentsIds]) {
        return;
    }
    NSArray<NSNumber *>*opponentsIds = (NSArray<NSNumber *>*)opponentsIdsObject;
    
    NSObject *typeObject = info[QBRTCSessionKey.type];
    
    NSInteger typeInteger =
    ([typeObject isKindOfClass:NSString.class] ||
     [typeObject isKindOfClass:NSNumber.class]) ?
    [(NSString *)typeObject integerValue] :
    QBRTCConferenceTypeVideo;
    
    QBRTCConferenceType type = QBRTCConferenceTypeVideo;
    if (typeInteger >= QBRTCConferenceTypeAudio || typeInteger <= QBRTCConferenceTypeVideo) {
        type = typeInteger;
    }
    
    QBWebRTCSessionController *sessionController =
    [[QBWebRTCSessionController alloc] initWithOpponents:opponentsIds
                                             sessionType:type];
    
    NSDictionary *userInfo = info[QBWebRTCKey.userInfo];
    NSString *sessionId = sessionController.session.id;
    NSNumber *userId = sessionController.currentUserId;
    self.cache[sessionId] = sessionController;
    
    [sessionController sessionToRNQBObjectWithResolver:^(id result) {
        QBRTCAudioSession *audioSession = [QBRTCAudioSession instance];
        QBRTCAudioSessionConfiguration *config = [self audioSessionConfiguration:sessionController.session];
        [audioSession setConfiguration:config];
        
        [sessionController.session startCall:userInfo];
        resolve(result);
        QBRTCLocalVideoTrack *videoTrack = sessionController.localVideoTrack;
        if (videoTrack) {
            [self postQBEventWithName:QBCallEventKey.remoteVideoTrack
                                 body:@{ QBWebRTCKey.userId: userId,
                                         QBWebRTCKey.enabled: @(videoTrack.enabled),
                                         QBWebRTCKey.sessionId: sessionId }];
        }
    } rejecter:reject];
}

- (void)accept:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
    [self sessionWithInfo:info
                  success:^(QBRTCSession *session) {
        NSDictionary *userInfo = info[QBWebRTCKey.userInfo];
        
        [session toQBResultDataWithResolver:^(id result) {
            
            QBRTCAudioSession *audioSession = [QBRTCAudioSession instance];
            QBRTCAudioSessionConfiguration *config = [self audioSessionConfiguration:session];
            [audioSession setConfiguration:config];
            
            if (audioSession.isActive == NO) { [audioSession setActive:YES]; }
            
            [session acceptCall:userInfo];
            resolve(result);
            QBRTCLocalVideoTrack *videoTrack = session.localMediaStream.videoTrack;
            if (videoTrack) {
                [self postQBEventWithName:QBCallEventKey.remoteVideoTrack
                                     body:@{ QBWebRTCKey.userId: session.currentUserID,
                                             QBWebRTCKey.enabled: @(videoTrack.enabled),
                                             QBWebRTCKey.sessionId: session.id }];
            }
        } rejecter:reject];
    } rejecter:reject];
}

- (void)reject:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
    [self sessionWithInfo:info
                  success:^(QBRTCSession *session) {
        NSDictionary *userInfo = info[QBWebRTCKey.userInfo];
        [session rejectCall:userInfo];
        [session toQBResultDataWithResolver:resolve rejecter:reject];
    } rejecter:reject];
}

- (void)hangUp:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
    [self sessionWithInfo:info
                  success:^(QBRTCSession *session) {
        NSDictionary *userInfo = info[QBWebRTCKey.userInfo];
        [session hangUp:userInfo];
        [session toQBResultDataWithResolver:resolve rejecter:reject];
    } rejecter:reject];
}

- (void)enableVideo:(NSDictionary *)info
           resolver:(QBResolveBlock)resolve
           rejecter:(QBRejectBlock)reject {
    NSObject *userIdObject = info[QBWebRTCKey.userId];
    NSNumber *userId = nil;
    if ([userIdObject isKindOfClass:NSString.class]) {
        NSString *userIdString = (NSString *)userIdObject;
        userId = @(userIdString.integerValue);
    } else if ([userIdObject isKindOfClass:NSNumber.class]) {
        userId = (NSNumber *)userIdObject;
    }
    
    BOOL enable = YES;
    NSObject *enableObject = info[QBWebRTCKey.enable];
    if ([enableObject isKindOfClass:NSString.class] ||
        [enableObject isKindOfClass:NSNumber.class]) {
        NSString *enableString = (NSString *)enableObject;
        enable = enableString.boolValue;
    }
    [self sessionWithInfo:info success:^(QBRTCSession *session) {
        if (!userId || [session.currentUserID isEqualToNumber:userId]) {
            session.localMediaStream.videoTrack.enabled = enable;
            QBRTCCameraCapture *capture = (QBRTCCameraCapture *)session.localMediaStream
            .videoTrack
            .videoCapture;
            if (enable && !capture.hasStarted && !capture.isRunning) {
                dispatch_async(dispatch_get_main_queue(), ^{
                    [capture startSession:nil];
                });
            } else {
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)),
                               dispatch_get_main_queue(), ^{
                    [capture stopSession:nil];
                });
            }
        } else {
            QBRTCVideoTrack *remote = [session remoteVideoTrackWithUserID:userId];
            remote.enabled = enable;
        }
        if (resolve) { resolve(nil); }
    } rejecter:reject];
}

- (void)enableAudio:(NSDictionary *)info
           resolver:(QBResolveBlock)resolve
           rejecter:(QBRejectBlock)reject {
    NSObject *userIdObject = info[QBWebRTCKey.userId];
    NSNumber *userId = nil;
    if ([userIdObject isKindOfClass:NSString.class]) {
        NSString *userIdString = (NSString *)userIdObject;
        userId = @(userIdString.integerValue);
    } else if ([userIdObject isKindOfClass:NSNumber.class]) {
        userId = (NSNumber *)userIdObject;
    }
    
    BOOL enable = YES;
    NSObject *enableObject = info[QBWebRTCKey.enable];
    if ([enableObject isKindOfClass:NSString.class] ||
        [enableObject isKindOfClass:NSNumber.class]) {
        NSString *enableString = (NSString *)enableObject;
        enable = enableString.boolValue;
    }
    
    [self sessionWithInfo:info success:^(QBRTCSession *session) {
        if (!userId || [session.currentUserID isEqualToNumber:userId]) {
            session.localMediaStream.audioTrack.enabled = enable;
        } else {
            QBRTCAudioTrack *remote = [session remoteAudioTrackWithUserID:userId];
            remote.enabled = enable;
        }
        if (resolve) { resolve(nil); }
    } rejecter:reject];
}

- (void)switchCamera:(NSDictionary *)info
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject {
    [self sessionWithInfo:info success:^(QBRTCSession *session) {
        QBRTCCameraCapture *capture = (QBRTCCameraCapture *)session.localMediaStream
        .videoTrack
        .videoCapture;
        AVCaptureDevicePosition position = capture.position;
        AVCaptureDevicePosition newPosition = position == AVCaptureDevicePositionBack ?
        AVCaptureDevicePositionFront :
        AVCaptureDevicePositionBack;
        
        if ([capture hasCameraForPosition:newPosition]) {
            
            CATransition *animation = [CATransition animation];
            animation.duration = .75f;
            animation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
            animation.type = @"oglFlip";
            
            if (position == AVCaptureDevicePositionFront) {
                
                animation.subtype = kCATransitionFromRight;
            }
            else if(position == AVCaptureDevicePositionBack) {
                
                animation.subtype = kCATransitionFromLeft;
            }
            
            [capture.previewLayer.superlayer addAnimation:animation forKey:nil];
            capture.position = newPosition;
        }
        if (resolve) { resolve(nil); }
    } rejecter:reject];
}

- (void)switchAudioOutput:(NSDictionary *)info
                 resolver:(QBResolveBlock)resolve
                 rejecter:(QBRejectBlock)reject {
    NSObject *outputObject = info[QBWebRTCKey.output];
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:outputObject
              objectKey:QBWebRTCKey.output]) {
        return;
    }
    NSNumber *outputNumber = (NSNumber *)outputObject;
    QB_AUDIO_OUTPUT output = (QB_AUDIO_OUTPUT)outputNumber.integerValue;
    QBRTCAudioSession *audioSession = [QBRTCAudioSession instance];
    switch (output) {
        case QB_AUDIO_OUTPUT_EARSPEAKER:
        case QB_AUDIO_OUTPUT_BLUETOOTH:
        case QB_AUDIO_OUTPUT_HEADPHONES:
            [audioSession overrideOutputAudioPort:AVAudioSessionPortOverrideNone];
            break;
        case QB_AUDIO_OUTPUT_LOUDSPEAKER:
            [audioSession overrideOutputAudioPort:AVAudioSessionPortOverrideSpeaker];
            break;
        default: {
            if (reject) {
                [NSError reject:reject message:@"unsupported type"];
            }
            return;
        }
    }
    if (resolve) { resolve(nil); };
}

//MARK: Help

- (QBRTCAudioSessionConfiguration *)audioSessionConfiguration:(QBRTCSession *)session {
    QBRTCAudioSessionConfiguration *configuration = [[QBRTCAudioSessionConfiguration alloc] init];
    configuration.categoryOptions |= AVAudioSessionCategoryOptionDuckOthers;
    
    // adding blutetooth support
    configuration.categoryOptions |= AVAudioSessionCategoryOptionAllowBluetooth;
    configuration.categoryOptions |= AVAudioSessionCategoryOptionAllowBluetoothA2DP;
    
    // adding airplay support
    configuration.categoryOptions |= AVAudioSessionCategoryOptionAllowAirPlay;
    
    if (session.conferenceType == QBRTCConferenceTypeVideo) {
        configuration.mode = AVAudioSessionModeVideoChat;
    }
    
    return configuration;
}

- (void)sessionWithInfo:(NSDictionary *)info
                success:(void(^)(QBRTCSession *session))success
               rejecter:(QBRejectBlock)reject {
    NSObject *IDObject = info[QBWebRTCKey.sessionId];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:IDObject
              objectKey:QBWebRTCKey.sessionId]) {
        return;
    }
    NSString *ID = (NSString *)IDObject;
    
    QBWebRTCSessionController *sessionController = self.cache[ID];
    if (!sessionController) {
        [NSError reject:reject
                message:[NSString
                         stringWithFormat:@"session with id: %@  is missing",
                         ID]];
    } else if (success) {
        success(sessionController.session);
    }
}

- (NSDictionary *)eventPayloadWithSession:(QBRTCSession *)session
                                   userId:(NSNumber *)userId
                                 userInfo:(NSDictionary *)userInfo {
    if (!session) { return nil; }
    NSError *error = nil;
    NSDictionary *result = [session toQBResultData:&error];
    if (error || !result) { return nil; }
    NSMutableDictionary *payload = @{ QBWebRTCKey.session: result }.mutableCopy;
    if (userId) {
        payload[QBWebRTCKey.userId] = userId;
    }
    if (userInfo) {
        payload[QBWebRTCKey.userInfo] = userInfo;
    }
    
    return payload.copy;
}

@end

@implementation QBWebRTCModule (Call)

- (void)didReceiveNewSession:(QBRTCSession *)session
                    userInfo:(NSDictionary *)userInfo {
    if (!self.cache[session.id]) {
        QBWebRTCSessionController *sessionController =
        [[QBWebRTCSessionController alloc] initWithSession:session];
        self.cache[session.id] = sessionController;
    }
    NSDictionary *payload = [self eventPayloadWithSession:session
                                                   userId:session.initiatorID
                                                 userInfo:userInfo];
    [self postQBEventWithName:QBCallEventKey.call body:payload];
}

- (void)session:(QBRTCSession *)session
userDidNotRespond:(NSNumber *)userID {
    NSDictionary *payload = [self eventPayloadWithSession:session
                                                   userId:userID
                                                 userInfo:nil];
    [self postQBEventWithName:QBCallEventKey.notAnswer body:payload];
}

- (void)session:(QBRTCSession *)session
 rejectedByUser:(NSNumber *)userID
       userInfo:(NSDictionary *)userInfo {
    NSDictionary *payload = [self eventPayloadWithSession:session
                                                   userId:userID
                                                 userInfo:userInfo];
    [self postQBEventWithName:QBCallEventKey.reject body:payload];
}

- (void)session:(QBRTCSession *)session
 acceptedByUser:(NSNumber *)userID
       userInfo:(NSDictionary *)userInfo {
    QBRTCAudioSession *audioSession = QBRTCAudioSession.instance;
    if (audioSession.isActive == false) { [audioSession setActive:YES]; }
    NSDictionary *payload = [self eventPayloadWithSession:session
                                                   userId:userID
                                                 userInfo:userInfo];
    [self postQBEventWithName:QBCallEventKey.accept body:payload];
}

- (void)session:(QBRTCSession *)session hungUpByUser:(NSNumber *)userID userInfo:(nullable NSDictionary <NSString *, NSString *> *)userInfo{
    NSDictionary *payload = [self eventPayloadWithSession:session
                                                   userId:userID
                                                 userInfo:userInfo];
    [self postQBEventWithName:QBCallEventKey.hangUp body:payload];
}

- (void)sessionDidClose:(QBRTCSession *)session {
    NSDictionary *payload = [self eventPayloadWithSession:session
                                                   userId:nil
                                                 userInfo:nil];
    QBWebRTCSessionController *sessionController = self.cache[session.id];
    if (sessionController) {
        if (sessionController.videoCapture.isRunning || sessionController.videoCapture.hasStarted) {
            [sessionController.videoCapture stopSession:nil];
        }
        [self.cache removeObjectForKey:session.id];
    }
    [self postQBEventWithName:QBCallEventKey.callEnd body:payload];
}

@end

@implementation QBWebRTCModule (Connection)

- (void)session:(__kindof QBRTCBaseSession *)session
didChangeConnectionState:(QBRTCConnectionState)state
        forUser:(NSNumber *)userID {
    NSMutableDictionary *payload = [self eventPayloadWithSession:session
                                                          userId:userID
                                                        userInfo:nil].mutableCopy;
    switch (state) {
        case QBRTCConnectionStateNew: {
            payload[QBWebRTCKey.state] = @(QBRTCPeerStateNew);
            break;
        }
        case QBRTCConnectionStateConnected: {
            QBRTCAudioSession *audioSession = [QBRTCAudioSession instance];
            if (audioSession.isActive == NO) { [audioSession setActive:YES]; }
            payload[QBWebRTCKey.state] = @(QBRTCPeerStateConnected);
            break;
        }
        case QBRTCConnectionStateDisconnectTimeout:
        case QBRTCConnectionStateDisconnected: {
            payload[QBWebRTCKey.state] = @(QBRTCPeerStateDisconnected);
            break;
        }
        case QBRTCConnectionStateFailed: {
            payload[QBWebRTCKey.state] = @(QBRTCPeerStateFailed);
            break;
        }
        case QBRTCConnectionStateClosed: {
            payload[QBWebRTCKey.state] = @(QBRTCPeerStateClosed);
            break;
        }
        default: return;
    }
    
    [self postQBEventWithName:QBCallEventKey.connectionState body:payload];
}

- (void)session:(__kindof QBRTCBaseSession *)session
didChangeRconnectionState:(QBRTCReconnectionState)state forUser:(NSNumber *)userID {
    NSMutableDictionary *payload = [self eventPayloadWithSession:session
                                                          userId:userID
                                                        userInfo:nil].mutableCopy;
    switch (state) {
        case QBRTCReconnectionStateFailed:
            payload[QBWebRTCKey.state] = @(QBRTCReconnectionStateFailed);
            break;
        case QBRTCReconnectionStateReconnecting:
            payload[QBWebRTCKey.state] = @(QBRTCReconnectionStateReconnecting);
            break;
        case QBRTCReconnectionStateReconnected:
            payload[QBWebRTCKey.state] = @(QBRTCReconnectionStateReconnected);
            break;
        default:
            payload[QBWebRTCKey.state] = @(-1);
            break;
    }
    
    [self postQBEventWithName:QBCallEventKey.reconnectionState body:payload];
}

@end

@implementation QBWebRTCModule (Video)

- (void)session:(__kindof QBRTCBaseSession *)session
receivedRemoteVideoTrack:(QBRTCVideoTrack *)videoTrack
       fromUser:(NSNumber *)userID {
    QBRTCSession *fullSession = (QBRTCSession *)session;
    if (!videoTrack || !userID || !fullSession.id.length) { return; }
    [self postQBEventWithName:QBCallEventKey.remoteVideoTrack
                         body:@{ QBWebRTCKey.userId : userID,
                                 QBWebRTCKey.enabled : @(videoTrack.isEnabled),
                                 QBWebRTCKey.sessionId: fullSession.id }];
}

@end
