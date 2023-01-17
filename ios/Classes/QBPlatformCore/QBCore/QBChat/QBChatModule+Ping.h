//
//  QBChatModule+Ping.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatModule.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBChatModule (Ping)

- (void)pingServer:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject;

- (void)pingUser:(NSDictionary *)info
        resolver:(QBResolveBlock)resolve
        rejecter:(QBRejectBlock)reject;

@end

NS_ASSUME_NONNULL_END
