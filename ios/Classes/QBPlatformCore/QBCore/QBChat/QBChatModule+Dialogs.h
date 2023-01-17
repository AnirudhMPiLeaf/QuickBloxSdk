//
//  QBChatModule+Dialogs.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatModule.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBChatModule (Dialogs)

- (void)getDialogs:(NSDictionary *)info
          resolver:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject;

- (void)getDialogsCount:(NSDictionary *)info
               resolver:(QBResolveBlock)resolve
               rejecter:(QBRejectBlock)reject;

- (void)createDialog:(NSDictionary *)info
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject;

- (void)updateDialog:(NSDictionary *)info
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject;

- (void)deleteDialog:(NSDictionary *)info
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject;

- (void)joinDialog:(NSDictionary *)info
          resolver:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject;

- (void)getOnlineUsers:(NSDictionary *)info
              resolver:(QBResolveBlock)resolve
              rejecter:(QBRejectBlock)reject;

- (void)leaveDialog:(NSDictionary *)info
           resolver:(QBResolveBlock)resolve
           rejecter:(QBRejectBlock)reject;

- (void)isJoinedDialog:(NSDictionary *)info
              resolver:(QBResolveBlock)resolve
              rejecter:(QBRejectBlock)reject;

@end

NS_ASSUME_NONNULL_END
