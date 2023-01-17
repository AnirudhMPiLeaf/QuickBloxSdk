//
//  QBConferenceCache.h
//  quickblox_sdk
//
//  Created by Injoit on 12.02.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <QuickbloxWebRTC/QBRTCConferenceSession.h>
#import "QBBridgeMethod.h"

@class QBConferenceWrapper;

NS_ASSUME_NONNULL_BEGIN

@interface QBConferenceCache : NSObject

- (id _Nullable)add:(QBRTCConferenceSession *)session;
- (NSString *_Nullable)sessionUUIDString:(QBRTCConferenceSession *)session;
- (QBRTCConferenceSession *_Nullable)sessionWithUUID:(NSString *)uuidString;
- (QBConferenceWrapper *_Nullable)wrapperWithUUID:(NSString *)uuidString;
- (id _Nullable)remove:(QBRTCConferenceSession *)session;


@end

NS_ASSUME_NONNULL_END
