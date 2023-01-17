//
//  QBSettingsModule.h
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBModule.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBSettingsModule : QBModule

- (void)init:(NSDictionary *)info
    resolver:(QBResolveBlock)resolve
    rejecter:(QBRejectBlock)reject;

- (void)initWithoutAuthKeyAndSecret:(NSDictionary *)info
                           resolver:(QBResolveBlock)resolve
                           rejecter:(QBRejectBlock)reject;

- (void)get:(QBResolveBlock)resolve
   rejecter:(QBRejectBlock)reject;

- (void)initStreamManagement:(NSDictionary *)info
                    resolver:(QBResolveBlock)resolve
                    rejecter:(QBRejectBlock)reject;

- (void)enableAutoReconnect:(NSDictionary *)info
                   resolver:(QBResolveBlock)resolve
                   rejecter:(QBRejectBlock)reject;

- (void)enableCarbons:(QBResolveBlock)resolve
             rejecter:(QBRejectBlock)reject;

- (void)disableCarbons:(QBResolveBlock)resolve
              rejecter:(QBRejectBlock)reject;

- (void)enableLogging:(QBResolveBlock)resolve
             rejecter:(QBRejectBlock)reject;

- (void)disableLogging:(QBResolveBlock)resolve
              rejecter:(QBRejectBlock)reject;

- (void)enableXMPPLogging:(QBResolveBlock)resolve
             rejecter:(QBRejectBlock)reject;

- (void)disableXMPPLogging:(QBResolveBlock)resolve
                  rejecter:(QBRejectBlock)reject;

@end

NS_ASSUME_NONNULL_END
