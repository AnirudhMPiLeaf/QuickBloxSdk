//
//  QBChatModule+Typing.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatModule+Typing.h"

#import <objc/runtime.h>

static void *_qbtypingListeners;

@implementation QBChatModule (Typing)

- (NSMutableDictionary<NSString *,QBDialogListener *>*)typingListenersCache {
    NSMutableDictionary<NSString *,QBDialogListener *>*result = objc_getAssociatedObject(self,
                                                                        &_qbtypingListeners);
    if (result == nil) {
        result = @{}.mutableCopy;
        objc_setAssociatedObject(self,
                                 &_qbtypingListeners,
                                 result,
                                 OBJC_ASSOCIATION_RETAIN_NONATOMIC);
    }
    return result;
}

- (void)sendIsTyping:(NSDictionary *)info
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject {
    if (QBChat.instance.isConnected == NO) {
        NSString *errorMessage = @"Client is not, or no longer, connected.";
        [NSError reject:reject message:errorMessage];
        return;
    }
    [self dialogWithInfo:info
                 success:^(QBChatDialog *dialog) {
        [dialog sendUserIsTyping];
        resolve(nil);
    } rejecter:reject];
}

- (void)sendStoppedTyping:(NSDictionary *)info
                 resolver:(QBResolveBlock)resolve
                 rejecter:(QBRejectBlock)reject {
    if (QBChat.instance.isConnected == NO) {
        NSString *errorMessage = @"Client is not, or no longer, connected.";
        [NSError reject:reject message:errorMessage];
        return;
    }
    [self dialogWithInfo:info
                 success:^(QBChatDialog *dialog) {
        [dialog sendUserStoppedTyping];
        resolve(nil);
    } rejecter:reject];
}

- (void)subscribeTyping:(QBChatDialog*)dialog {
    QBDialogListener *listner = self.typingListenersCache[dialog.ID];
    if (!listner) {
        listner = [[QBDialogListener alloc] init];
        listner.delegate = self;
        self.typingListenersCache[dialog.ID] = listner;
    } else {
        [listner unsubscribe];
    }
    [listner subscribeWithDialog:dialog];
}

- (void)unsubscribeTyping:(QBChatDialog *)dialog {
    QBDialogListener *listner = self.typingListenersCache[dialog.ID];
    [listner unsubscribe];
    [self.typingListenersCache removeObjectForKey:dialog.ID];
}

@end

//MARK: DialogListnerProtocol
@implementation QBChatModule(DialogListnerTyping)

- (void)chatDidReciveIsTypingWithUserID:(NSNumber *)userID dialogID:(NSString *)dialogID {
    [self postQBEventWithName:QBUserTypingEvent.isTyping
                         body:@{ QBChatKey.userId: userID,
                                 QBChatKey.dialogId: dialogID }];
}

- (void)chatDidReciveStopTypingWithUserID:(NSNumber *)userID dialogID:(NSString *)dialogID {
    [self postQBEventWithName:QBUserTypingEvent.stopTyping
                         body:@{ QBChatKey.userId: userID,
                                 QBChatKey.dialogId: dialogID }];
}

@end
