//
//  QBPushSubscriptionsModule.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBModule.h"
#import "QBMSubscription+QBSerializer.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBPushSubscriptionsModule : QBModule

- (void)create:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject;

- (void)get:(QBResolveBlock)resolve
   rejecter:(QBRejectBlock)reject;

- (void)remove:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject;

@end

NS_ASSUME_NONNULL_END
