//
//  QBChatModule.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatModule.h"
//#import "QBChatDialog+QBSerializer.h"
//#import "QBChatModule+Connection.h"
//#import "QBChatModule+Ping.h"
//#import "QBChatModule+Dialogs.h"
#import "QBChatModule+Messages.h"
#import "QBChatModule+Typing.h"


@interface QBChatModule()<QBChatDelegate>

@end

@implementation QBChatModule

- (instancetype)init {
    self = [super init];
    if (self) {
        [[QBChat instance] addDelegate:self];
    }
    return self;
}

- (void)dealloc {
    [[QBChat instance] removeDelegate:self];
}

- (NSArray<NSString *> *)methodsWithArray {
  return @[
      NSStringFromSelector(@selector(getTotalUnreadMessagesCount:resolver:rejecter:))
  ];
}

- (NSArray<NSString *> *)events {
    return @[ QBChatConnectEvent.connected,
              QBChatConnectEvent.connectionClosedOnError,
              QBChatConnectEvent.connectionClosed,
              QBChatConnectEvent.reconnectionFailed,
              QBChatConnectEvent.reconnectionSuccessful,
              QBMessageEvent.receivedNewMessage,
              QBMessageEvent.receivedSystemMessage,
              QBMessageStatusEvent.delivered,
              QBMessageStatusEvent.read,
              QBUserTypingEvent.isTyping,
              QBUserTypingEvent.stopTyping];
}

- (NSUInteger)currentId {
    return QBSession.currentSession.currentUserID;
}

- (NSMutableDictionary<NSString*, QBChatDialog*>*)dialogsCache {
    if (!_dialogsCache) {
        _dialogsCache = @{}.mutableCopy;
    }
    return _dialogsCache;
}

- (void)dialogWithInfo:(NSDictionary *)info
               success:(void(^)(QBChatDialog *dialog))success
              rejecter:(QBRejectBlock)reject {
    NSObject *dialogIdObject = info[QBChatKey.dialogId];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:dialogIdObject
              objectKey:QBChatKey.dialogId]) {
        return;
    }
    NSString *dialogId = (NSString *)dialogIdObject;
    
    QBChatDialog *dialog = self.dialogsCache[dialogId];
    if (success && dialog) {
        success(dialog);
        return;
    }
    
    QBResponsePage *responsePage = [QBResponsePage responsePageWithLimit:1 skip:0];
    NSDictionary *extendedRequest = @{@"_id": dialogId};
    
    __weak __typeof(self)weakSelf = self;
    
    [QBRequest dialogsForPage:responsePage
              extendedRequest:extendedRequest
                 successBlock:^(QBResponse * _Nonnull response,
                                NSArray<QBChatDialog *> * _Nonnull dialogs,
                                NSSet<NSNumber *> * _Nonnull dialogsUsersIDs,
                                QBResponsePage * _Nonnull page) {
        if (dialogs.count == 0) {
            [NSError reject:reject
                    message:[NSString
                             stringWithFormat:@"dialog with id: %@  is missing",
                             dialogId]];
        } else if (success) {
            [weakSelf addDialogsToCache:dialogs];
            success(dialogs.firstObject);
        }
    } errorBlock:^(QBResponse * _Nonnull response) {
        reject(response.statusCode,response.errorMessage,response.nsError);
    }];
}

- (void)addDialogsToCache:(NSArray<QBChatDialog *> *)dialogs {
  if (!dialogs.count) { return; }
  for (QBChatDialog *dialog in dialogs) {
      self.dialogsCache[dialog.ID] = dialog;
      [self subscribeTyping:dialog];
  }
}

- (void)removeDialogsFromCache:(NSArray<QBChatDialog *> *)dialogs {
  if (!dialogs.count) { return; }
  for (QBChatDialog *dialog in dialogs) {
      [self unsubscribeTyping:dialog];
      [self.dialogsCache removeObjectForKey:dialog.ID];
  }
}


@end
