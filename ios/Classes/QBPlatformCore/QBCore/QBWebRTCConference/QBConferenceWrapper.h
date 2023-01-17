//
//  QBConferenceWrapper.h
//  quickblox_sdk
//
//  Created by Injoit on 03.02.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "QBBridgeMethod.h"
#import <QuickbloxWebRTC/QuickbloxWebRTC.h>

NS_ASSUME_NONNULL_BEGIN

@interface QBConferenceWrapper : NSObject

@property (nonatomic, strong, readonly) NSString *id;
@property (nonatomic, strong, readonly) QBRTCConferenceSession *session;
@property (nonatomic, strong, readonly) id _Nullable sessionInfo;

@property (nonatomic, strong, readonly) QBRTCLocalVideoTrack *localVideoTrack;
@property (nonatomic, strong, readonly) QBRTCCameraCapture *videoCapture;
@property (nonatomic, strong, readonly) NSNumber *currentUserId;

- (id)initWithSession:(QBRTCConferenceSession *)session;

@end

NS_ASSUME_NONNULL_END
