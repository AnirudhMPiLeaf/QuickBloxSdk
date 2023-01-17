//
//  QBConferenceModule.m
//  quickblox_sdk
//
//  Created by Injoit on 03.02.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import "QBConferenceModule.h"
#import "QBRTCConferenceSession+QBSerializer.h"
#import "QBConferenceConstants.h"
#import "QBConferenceCache.h"
#import "QBConferenceWrapper.h"

@interface QBConferenceModule () <QBRTCConferenceClientDelegate>

@property (nonatomic, strong) NSMutableDictionary <NSString *, QBResolveBlock> *createResolves;
@property (nonatomic, strong) NSMutableDictionary <NSString *, QBRejectBlock> *createRejects;
@property (nonatomic, strong) NSMutableDictionary <NSString *, QBResolveBlock> *joinResolves;

@end

@interface QBConferenceModule (Session)
@end

@interface QBConferenceModule (Event)
@end

@interface QBConferenceModule (Video)
@end

@implementation QBConferenceModule

- (instancetype)init {
    self = [super init];
    if (self) {
        _cache = [QBConferenceCache new];
        _createResolves = @{}.mutableCopy;
        _createRejects = @{}.mutableCopy;
        _joinResolves = @{}.mutableCopy;
        [QBRTCConferenceClient.instance addDelegate:self];
        
    }
    return self;
}

- (void)dealloc {
    [QBRTCConferenceClient.instance removeDelegate:self];
}

- (NSArray<NSString *> *)events {
    return @[ QBConferenceEventKey.participantReceived,
              QBConferenceEventKey.participantLeft,
              QBConferenceEventKey.error,
              QBConferenceEventKey.close,
              QBConferenceEventKey.sessionState,
              QBConferenceEventKey.videoTrack ];
}

- (void)init:(NSString *)endpoint
    resolver:(QBResolveBlock)resolve
    rejecter:(QBRejectBlock)reject {
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:endpoint
              objectKey:@"endpoint"]) {
        return;
    }
    //This method used in the Android platform
    QBRTCConfig.conferenceEndpoint = endpoint;
    if (resolve) { resolve(nil); }
}

- (void)release:(QBResolveBlock)resolve
       rejecter:(QBRejectBlock)reject {
    if (QBRTCAudioSession.instance.isActive) {
        [QBRTCAudioSession.instance setActive:NO];
    }
    if (resolve) { resolve(nil); }
}

- (void)create:(NSString *)roomId
              :(NSNumber *)type
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:roomId
              objectKey:QBRTCConferenceSessionKey.roomId]) {
        return;
    }
    
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:type
              objectKey:QBRTCConferenceSessionKey.type]) {
        return;
    }
    
    self.createResolves[roomId] = [resolve copy];
    self.createRejects[roomId] = [reject copy];
    [QBRTCConferenceClient.instance createSessionWithChatDialogID:roomId
                                                   conferenceType:type.integerValue];
}

- (void)joinAsPublisher:(NSString *)id
resolver:(QBResolveBlock)resolve
rejecter:(QBRejectBlock)reject {
    [self sessionWithInfo:id
                  success:^(QBRTCConferenceSession *session) {
        self.joinResolves[id] = [resolve copy];
        QBRTCAudioSession *audioSession = [QBRTCAudioSession instance];
        QBRTCAudioSessionConfiguration *config = [self audioSessionConfiguration:session];
        [audioSession setConfiguration:config];
        if (audioSession.isActive == NO) { [audioSession setActive:YES]; }
        [session joinAsPublisher];
    } rejecter:reject];
}

- (void)getOnlineParticipants:(NSString *)id
resolver:(QBResolveBlock)resolve
rejecter:(QBRejectBlock)reject {
    [self sessionWithInfo:id
                  success:^(QBRTCConferenceSession *session) {
        [session listOnlineParticipantsWithCompletionBlock:^(NSArray<NSNumber *> * _Nonnull publishers, NSArray<NSNumber *> * _Nonnull listeners) {
            NSMutableArray<NSNumber *>*participants = @[].mutableCopy;
            [participants addObjectsFromArray:publishers];
            [participants addObjectsFromArray:listeners];
            if (resolve) { resolve(participants.copy); }
        }];
    } rejecter:reject];
}

- (void)subscribeToParticipant:(NSNumber *)id
:(NSString *)sessionId
resolver:(QBResolveBlock)resolve
rejecter:(QBRejectBlock)reject {
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:id
              objectKey:@"participantId"]) {
        return;
    }
    
    [self sessionWithInfo:sessionId
                  success:^(QBRTCConferenceSession *session) {
        [session subscribeToUserWithID:id];
        if (resolve) { resolve(nil); }
    } rejecter:reject];
}

- (void)unsubscribeFromParticipant:(NSNumber *)id
:(NSString *)sessionId
resolver:(QBResolveBlock)resolve
rejecter:(QBRejectBlock)reject {
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:id
              objectKey:@"participantId"]) {
        return;
    }
    
    [self sessionWithInfo:sessionId
                  success:^(QBRTCConferenceSession *session) {
        [session unsubscribeFromUserWithID:id];
        if (resolve) { resolve(nil); }
    } rejecter:reject];
}

- (void)leave:(NSString *)id resolver:(QBResolveBlock)resolve rejecter:(QBRejectBlock)reject {
    [self sessionWithInfo:id
                  success:^(QBRTCConferenceSession *session) {
        [session leave];
        if (resolve) { resolve(nil); }
    } rejecter:reject];
}

- (void)enableVideo:(NSDictionary *)info
           resolver:(QBResolveBlock)resolve
           rejecter:(QBRejectBlock)reject {
    NSObject *userIdObject = info[@"userId"];
    NSNumber *userId = nil;
    if ([userIdObject isKindOfClass:NSString.class]) {
        NSString *userIdString = (NSString *)userIdObject;
        userId = @(userIdString.integerValue);
    } else if ([userIdObject isKindOfClass:NSNumber.class]) {
        userId = (NSNumber *)userIdObject;
    }
    
    BOOL enable = YES;
    NSObject *enableObject = info[@"enable"];
    if ([enableObject isKindOfClass:NSString.class] ||
        [enableObject isKindOfClass:NSNumber.class]) {
        NSString *enableString = (NSString *)enableObject;
        enable = enableString.boolValue;
    }
    
    NSObject *IDObject = info[@"sessionId"];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:IDObject
              objectKey:@"sessionId"]) {
        return;
    }
    NSString *uuid = (NSString *)IDObject;
    [self sessionWithInfo:uuid success:^(QBRTCConferenceSession *session) {
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
    NSObject *userIdObject = info[@"userId"];
    NSNumber *userId = nil;
    if ([userIdObject isKindOfClass:NSString.class]) {
        NSString *userIdString = (NSString *)userIdObject;
        userId = @(userIdString.integerValue);
    } else if ([userIdObject isKindOfClass:NSNumber.class]) {
        userId = (NSNumber *)userIdObject;
    }
    
    BOOL enable = YES;
    NSObject *enableObject = info[@"enable"];
    if ([enableObject isKindOfClass:NSString.class] ||
        [enableObject isKindOfClass:NSNumber.class]) {
        NSString *enableString = (NSString *)enableObject;
        enable = enableString.boolValue;
    }
    
    NSObject *IDObject = info[@"sessionId"];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:IDObject
              objectKey:@"sessionId"]) {
        return;
    }
    NSString *uuid = (NSString *)IDObject;
    
    [self sessionWithInfo:uuid success:^(QBRTCConferenceSession *session) {
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
    NSObject *IDObject = info[@"sessionId"];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:IDObject
              objectKey:@"sessionId"]) {
        return;
    }
    NSString *uuid = (NSString *)IDObject;
    [self sessionWithInfo:uuid success:^(QBRTCConferenceSession *session) {
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
    NSObject *outputObject = info[@"output"];
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:outputObject
              objectKey:@"output"]) {
        return;
    }
    NSNumber *outputNumber = (NSNumber *)outputObject;
    QB_CONFERENCE_AUDIO_OUTPUT output = (QB_CONFERENCE_AUDIO_OUTPUT)outputNumber.integerValue;
    QBRTCAudioSession *audioSession = [QBRTCAudioSession instance];
    switch (output) {
        case QB_CONFERENCE_EARSPEAKER:
        case QB_CONFERENCE_BLUETOOTH:
        case QB_CONFERENCE_HEADPHONES:
            [audioSession overrideOutputAudioPort:AVAudioSessionPortOverrideNone];
            break;
        case QB_CONFERENCE_LOUDSPEAKER:
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

- (QBRTCAudioSessionConfiguration *)audioSessionConfiguration:(QBRTCConferenceSession *)session {
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

- (void)sessionWithInfo:(NSString *)id
success:(void(^)(QBRTCConferenceSession *session))success
rejecter:(QBRejectBlock)reject {
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:id
              objectKey:QBRTCConferenceSessionKey.id]) {
        return;
    }
    
    QBRTCConferenceSession *session = [self.cache sessionWithUUID:id];
    if (!session) {
        [NSError reject:reject
                message:[NSString
                         stringWithFormat:@"session with id: %@  is missing",
                         id]];
    } else if (success) {
        success(session);
    }
}

@end

@implementation QBConferenceModule (Session)

- (void)didCreateNewSession:(QBRTCConferenceSession *)session {
    __weak __typeof(self)weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^(void){
        id info = [weakSelf.cache add:session];
        QBResolveBlock resolve = [weakSelf.createResolves[session.chatDialogID] copy];
        [weakSelf.createResolves removeObjectForKey:session.chatDialogID];
        [weakSelf.createRejects removeObjectForKey:session.chatDialogID];
        if (resolve) { resolve(info); }
        [weakSelf postQBEventWithName:QBConferenceEventKey.videoTrack
                                 body:@{ @"userId" : session.currentUserID,
                                         @"enabled" : @(session.localMediaStream.videoTrack.isEnabled),
                                         @"sessionId": info[@"id"] }];
    });
}

- (void)session:(QBRTCConferenceSession *)session didChangeState:(QBRTCSessionState)state {
    __weak __typeof(self)weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^(void){
        NSString *uuid = [self.cache sessionUUIDString:session];
        if (!uuid.length) { return; }
        [weakSelf postQBEventWithName:QBConferenceEventKey.sessionState
                                 body:@{ @"sessionId" : uuid,
                                         QBRTCConferenceSessionKey.state : @(state)}];
    });
}

- (void)session:(QBRTCConferenceSession *)session didJoinChatDialogWithID:(NSString *)chatDialogID publishersList:(NSArray<NSNumber *> *)publishersList {
    __weak __typeof(self)weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^(void){
        NSString *uuid = [self.cache sessionUUIDString:session];
        if (!uuid.length) { return; }
        QBResolveBlock resolve = [weakSelf.joinResolves[uuid] copy];
        [weakSelf.joinResolves removeObjectForKey:uuid];
        if (resolve) { resolve(publishersList); }
    });
}

- (void)sessionDidClose:(QBRTCConferenceSession *)session withTimeout:(BOOL)timeout {
    __weak __typeof(self)weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^(void) {
        NSString *uuid = [weakSelf.cache sessionUUIDString:session];
        QBConferenceWrapper *wrapper = [weakSelf.cache wrapperWithUUID:uuid];
        QBRTCCameraCapture *capture = wrapper.videoCapture;
        if (capture.hasStarted || capture.isRunning) {
            [capture stopSession:nil];
        }
        [weakSelf.cache remove:session];
        if (!uuid.length) {
            if (timeout) {
                QBRejectBlock reject = [weakSelf.createRejects[session.chatDialogID] copy];
                [weakSelf.createRejects removeObjectForKey:session.chatDialogID];
                [weakSelf.createResolves removeObjectForKey:session.chatDialogID];
                NSString *message = [NSString stringWithFormat:@"Failed to connect to `%@`", QBRTCConfig.conferenceEndpoint];
                [NSError reject:reject message:message];
            }
            return;
        }
        [weakSelf postQBEventWithName:QBConferenceEventKey.close
                                 body:@{ @"sessionId" : uuid }];
    });
}

@end

@implementation QBConferenceModule (Event)

- (void)session:(QBRTCConferenceSession *)session
didReceiveNewPublisherWithUserID:(NSNumber *)userID {
    __weak __typeof(self)weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^(void){
        NSString *uuid = [self.cache sessionUUIDString:session];
        if (!uuid.length) { return; }
        [weakSelf postQBEventWithName:QBConferenceEventKey.participantReceived
                                 body:@{ @"sessionId" : uuid,
                                         @"userId" : userID }];
    });
}

- (void)session:(QBRTCConferenceSession *)session
publisherDidLeaveWithUserID:(NSNumber *)userID {
    __weak __typeof(self)weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^(void){
        NSString *uuid = [self.cache sessionUUIDString:session];
        if (!uuid.length) { return; }
        [weakSelf postQBEventWithName:QBConferenceEventKey.participantLeft
                                 body:@{ @"sessionId" : uuid,
                                         @"userId" : userID }];
    });
}

- (void)session:(QBRTCConferenceSession *)session
didReceiveError:(NSError *)error {
    __weak __typeof(self)weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^(void) {
        NSString *uuid = [self.cache sessionUUIDString:session];
        NSString *errorMessage = error.localizedDescription ?: @"error";
        if (!uuid.length) {
            QBRejectBlock reject = [weakSelf.createRejects[session.chatDialogID] copy];
            [weakSelf.createRejects removeObjectForKey:session.chatDialogID];
            [weakSelf.createResolves removeObjectForKey:session.chatDialogID];
            [NSError reject:reject message:errorMessage];
            return;
        }
        [weakSelf postQBEventWithName:QBConferenceEventKey.error
                                 body:@{ @"sessionId" : uuid,
                                         @"errorMessage" : errorMessage }];
    });
}

@end

@implementation QBConferenceModule (Video)

- (void)session:(QBRTCConferenceSession *)session
receivedRemoteVideoTrack:(QBRTCVideoTrack *)videoTrack
       fromUser:(NSNumber *)userID {
    __weak __typeof(self)weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^(void) {
        NSString *uuid = [self.cache sessionUUIDString:session];
        if (!videoTrack || !userID || !uuid.length) { return; }
        [weakSelf postQBEventWithName:QBConferenceEventKey.videoTrack
                                 body:@{ @"userId" : userID,
                                         @"enabled" : @(videoTrack.isEnabled),
                                         @"sessionId": uuid }];
    });
}

@end
