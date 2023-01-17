//
//  QBWebRTCSessionController.m
//  quickblox_sdk
//
//  Created by Injoit on 03.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import "QBWebRTCSessionController.h"
#import "QBRTCSession+QBSerializer.h"

@interface QBWebRTCSessionController ()

@property (nonatomic, strong) QBRTCCameraCapture *videoCapture;

@end


@implementation QBWebRTCSessionController

- (id)initWithSession:(QBRTCSession *)session {
    self = [super init];
    if (self) {
        _session = session;
        _session.localMediaStream.videoTrack.videoCapture = self.videoCapture;
    }
    return self;
}

- (id)initWithOpponents:(NSArray <NSNumber *> *)opponents
            sessionType:(QBRTCConferenceType)type {
    self = [super init];
    if (self) {
        _session = [[QBRTCClient instance] createNewSessionWithOpponents:opponents
                                                      withConferenceType:type];
        _session.localMediaStream.videoTrack.videoCapture = self.videoCapture;
    }
    return self;
}

- (QBRTCLocalVideoTrack *)localVideoTrack {
    return _session.localMediaStream.videoTrack;
}

- (QBRTCCameraCapture *)videoCapture {
    if (!_videoCapture) {
        _videoCapture = [[QBRTCCameraCapture alloc]
                         initWithVideoFormat:[QBRTCVideoFormat defaultFormat]
                         position:AVCaptureDevicePositionFront];
        return _videoCapture;
    }
    return _videoCapture;
}

- (NSNumber *)currentUserId {
    return _session.currentUserID;
}

- (void)sessionToRNQBObjectWithResolver:(QBResolveBlock)resolve
                               rejecter:(QBRejectBlock)reject {
    [_session toQBResultDataWithResolver:resolve rejecter:reject];
}

- (void)dealloc {
    if (self.videoCapture.isRunning || self.videoCapture.hasStarted) {
        [self.videoCapture stopSession:nil];
    }
}

@end
