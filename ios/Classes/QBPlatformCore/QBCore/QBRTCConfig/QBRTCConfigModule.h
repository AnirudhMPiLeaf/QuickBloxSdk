//
//  QBRTCConfigModule.h
//  quickblox_sdk
//
//  Created by Injoit on 14.01.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import "QBModule.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBRTCConfigModule : QBModule

- (void)setAnswerTimeInterval:(NSNumber *)interval
                   resolver:(QBResolveBlock)resolve
                   rejecter:(QBRejectBlock)reject;

- (void)getAnswerTimeInterval:(QBResolveBlock)resolve
   rejecter:(QBRejectBlock)reject;

- (void)setDialingTimeInterval:(NSNumber *)interval
                   resolver:(QBResolveBlock)resolve
                   rejecter:(QBRejectBlock)reject;

- (void)getDialingTimeInterval:(QBResolveBlock)resolve
                      rejecter:(QBRejectBlock)reject;

- (void)setReconnectionTimeInterval:(NSNumber *)interval
                           resolver:(QBResolveBlock)resolve
                           rejecter:(QBRejectBlock)reject;

- (void)getReconnectionTimeInterval:(QBResolveBlock)resolve
                           rejecter:(QBRejectBlock)reject;

- (void)setICEServers:(NSArray *)servers
             resolver:(QBResolveBlock)resolve
             rejecter:(QBRejectBlock)reject;

- (void)getICEServers:(QBResolveBlock)resolve
             rejecter:(QBRejectBlock)reject;

@end

NS_ASSUME_NONNULL_END
