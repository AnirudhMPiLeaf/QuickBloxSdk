//
//  QBChatModule.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBModule.h"
#import "QBChatConstants.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBChatModule : QBModule

@property (nonatomic, strong) NSMutableDictionary<NSString*, QBChatDialog*>*dialogsCache;
@property (nonatomic, assign, readonly) NSUInteger currentId;

- (void)dialogWithInfo:(NSDictionary *)info
               success:(void(^)(QBChatDialog *dialog))success
              rejecter:(QBRejectBlock)reject;

- (void)addDialogsToCache:(NSArray<QBChatDialog *>*)dialogs;
- (void)removeDialogsFromCache:(NSArray<QBChatDialog *>*)dialogs;

@end

NS_ASSUME_NONNULL_END
