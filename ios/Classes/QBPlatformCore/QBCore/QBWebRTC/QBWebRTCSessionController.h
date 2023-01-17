//
//  QBWebRTCSessionController.h
//  quickblox_sdk
//
//  Created by Injoit on 03.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "QBBridgeMethod.h"
#import <QuickbloxWebRTC/QuickbloxWebRTC.h>

NS_ASSUME_NONNULL_BEGIN

@interface QBWebRTCSessionController : NSObject

@property (nonatomic, strong, readonly) QBRTCSession *session;
@property (nonatomic, strong, readonly) QBRTCLocalVideoTrack *localVideoTrack;
@property (nonatomic, strong, readonly) QBRTCCameraCapture *videoCapture;
@property (nonatomic, strong, readonly) NSNumber *currentUserId;

- (id)initWithSession:(QBRTCSession *)session;
- (id)initWithOpponents:(NSArray <NSNumber *> *)opponents
            sessionType:(QBRTCConferenceType)type;

- (void)sessionToRNQBObjectWithResolver:(QBResolveBlock)resolve
                               rejecter:(QBRejectBlock)reject;

@end

NS_ASSUME_NONNULL_END
