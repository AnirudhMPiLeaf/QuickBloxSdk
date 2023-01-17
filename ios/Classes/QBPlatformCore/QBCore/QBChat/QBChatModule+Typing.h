//
//  QBChatModule+Typing.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatModule.h"
#import "QBDialogListener.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBChatModule (Typing)

@property (nonatomic, strong, readonly) NSMutableDictionary<NSString *,QBDialogListener *>*typingListenersCache;

- (void)sendIsTyping:(NSDictionary *)info
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject;

- (void)sendStoppedTyping:(NSDictionary *)info
                 resolver:(QBResolveBlock)resolve
                 rejecter:(QBRejectBlock)reject;

- (void)subscribeTyping:(QBChatDialog*)dialog;

- (void)unsubscribeTyping:(QBChatDialog*)dialog;

@end

@interface QBChatModule(DialogListnerTyping) <QBDialogListnerDelegate>
@end

NS_ASSUME_NONNULL_END
