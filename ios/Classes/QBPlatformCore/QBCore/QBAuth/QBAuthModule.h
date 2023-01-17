//
//  QBAuthModule.h
//  quickblox_sdk
//
//  Created by Injoit on 23.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBModule.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBAuthModule : QBModule

- (void)login:(NSDictionary *)info
     resolver:(QBResolveBlock)resolve
     rejecter:(QBRejectBlock)reject;

- (void)loginWithEmail:(NSString *)email
                      :(NSString *)password
              resolver:(QBResolveBlock)resolve
              rejecter:(QBRejectBlock)reject;

- (void)loginWithFacebook:(NSString *)token
                 resolver:(QBResolveBlock)resolve
                 rejecter:(QBRejectBlock)reject;

- (void)loginWithFirebase:(NSString *)firebaseProjectId
                         :(NSString *)token
                 resolver:(QBResolveBlock)resolve
                 rejecter:(QBRejectBlock)reject;

- (void)logout:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject;

- (void)setSession:(NSDictionary *)info
          resolver:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject;

- (void)getSession:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject;

- (void)startSessionWithToken:(NSString *)token
                     resolver:(QBResolveBlock)resolve
                     rejecter:(QBRejectBlock)reject;

@end

NS_ASSUME_NONNULL_END
