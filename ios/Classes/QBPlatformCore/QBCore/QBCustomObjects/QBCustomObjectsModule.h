//
//  QBCustomObjectsModule.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBModule.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBCustomObjectsModule : QBModule

- (void)create:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject;

- (void)getByIds:(NSDictionary *)info
        resolver:(QBResolveBlock)resolve
        rejecter:(QBRejectBlock)reject;

- (void)get:(NSDictionary *)info
   resolver:(QBResolveBlock)resolve
   rejecter:(QBRejectBlock)reject;

- (void)update:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject;

- (void)remove:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject;

@end

NS_ASSUME_NONNULL_END
