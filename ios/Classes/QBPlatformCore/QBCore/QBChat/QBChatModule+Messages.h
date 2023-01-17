//
//  QBChatModule+Messages.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatModule.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBChatModule (Messages)

- (void)getTotalUnreadMessagesCount:(NSArray *)dialogIds
                           resolver:(QBResolveBlock)resolve
                           rejecter:(QBRejectBlock)reject;

- (void)getDialogMessages:(NSDictionary *)info
                 resolver:(QBResolveBlock)resolve
                 rejecter:(QBRejectBlock)reject;

- (void)sendMessage:(NSDictionary *)info
           resolver:(QBResolveBlock)resolve
           rejecter:(QBRejectBlock)reject;

- (void)sendSystemMessage:(NSDictionary *)info
                 resolver:(QBResolveBlock)resolve
                 rejecter:(QBRejectBlock)reject;

- (void)markMessageRead:(NSDictionary *)info
               resolver:(QBResolveBlock)resolve
               rejecter:(QBRejectBlock)reject;

- (void)markMessageDelivered:(NSDictionary *)info
                    resolver:(QBResolveBlock)resolve
                    rejecter:(QBRejectBlock)reject;

@end

@interface QBChatModule (MessageProtocol) <QBChatReceiveMessageProtocol>
@end

NS_ASSUME_NONNULL_END
