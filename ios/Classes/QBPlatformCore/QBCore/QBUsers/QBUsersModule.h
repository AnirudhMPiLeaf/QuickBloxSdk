//
//  QMUsersModule.h
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBModule.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBUsersModule : QBModule

- (void)create:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject;

- (void)update:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject;

- (void)getUsers:(NSDictionary *)info
        resolver:(QBResolveBlock)resolve
        rejecter:(QBRejectBlock)reject;

- (void)getUsersByTag:(NSDictionary *)info
        resolver:(QBResolveBlock)resolve
        rejecter:(QBRejectBlock)reject;

@end

NS_ASSUME_NONNULL_END
