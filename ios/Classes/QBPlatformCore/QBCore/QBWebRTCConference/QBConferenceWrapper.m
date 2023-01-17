//
//  QBConferenceWrapper.m
//  quickblox_sdk
//
//  Created by Injoit on 03.02.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import "QBConferenceWrapper.h"
#import "QBRTCConferenceSession+QBSerializer.h"

@interface QBConferenceWrapper ()

@property (nonatomic, strong) QBRTCCameraCapture *videoCapture;

@end

@implementation QBConferenceWrapper

- (id)initWithSession:(QBRTCConferenceSession *)session {
    self = [super init];
    if (self) {
        _session = session;
        _session.localMediaStream.videoTrack.videoCapture = self.videoCapture;
        _id = NSUUID.UUID.UUIDString.lowercaseString;
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

- (id)sessionInfo {
    NSDictionary *result = [_session toQBResultData:nil];
    NSMutableDictionary *info = result.mutableCopy;
    info[@"id"] = _id;
    return info.copy;
}

- (void)dealloc {
    if (self.videoCapture.isRunning || self.videoCapture.hasStarted) {
        [self.videoCapture stopSession:nil];
    }
}

@end
