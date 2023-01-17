//
//  QBChatDialog+QBSerializer.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatDialog+QBSerializer.h"
#import "NSDate+Helper.h"

const struct QBChatDialogKeysStruct QBDialogKey = {
    .id  = @"id",
    .createdAt = @"createdAt",
    .updatedAt = @"updatedAt",
    .roomJid = @"roomJid",
    .type = @"type",
    .name = @"name",
    .photo = @"photo",
    .lastMessage = @"lastMessage",
    .lastMessageDateSent = @"lastMessageDateSent",
    .lastMessageUserId = @"lastMessageUserId",
    .unreadMessagesCount = @"unreadMessagesCount",
    .occupantsIds = @"occupantsIds",
    .userId = @"userId",
    .customData = @"customData",
    .isJoined = @"isJoined",
    .force = @"force",
};

@implementation QBChatDialog (QBSerializer)

- (id)toQBResultData:(NSError *__autoreleasing *)error {
    NSMutableDictionary* info = [NSMutableDictionary dictionary];
    
    if (self.ID.length) {
        info[QBDialogKey.id] = self.ID;
    }
    
    NSString *created_at = [NSDate stringFromISO8601Date:self.createdAt];
    if (created_at != nil) {
        info[QBDialogKey.createdAt] = created_at;
    }
    
    NSString *updated_at = [NSDate stringFromISO8601Date:self.updatedAt];
    if (updated_at != nil) {
        info[QBDialogKey.updatedAt] = updated_at;
    }
    
    if (self.roomJID.length) {
        info[QBDialogKey.roomJid] = self.roomJID;
    }
    
    if (self.type != 0) {
        info[QBDialogKey.type] = @(self.type);
    }
    
    if (self.name.length) {
        info[QBDialogKey.name] = self.name;
    }
    
    if (self.photo.length) {
        info[QBDialogKey.photo] = self.photo;
    }
    
    if (self.lastMessageText.length) {
        info[QBDialogKey.lastMessage] = self.lastMessageText;
    }
    
    if (self.lastMessageDate != nil) {
        NSUInteger lastMessageDateInterval = [self.lastMessageDate timeIntervalSince1970] * 1000;
        info[QBDialogKey.lastMessageDateSent] = @(lastMessageDateInterval);
    }
    
    if (self.lastMessageUserID != 0) {
        info[QBDialogKey.lastMessageUserId] = @(self.lastMessageUserID);
    }
    
    info[QBDialogKey.unreadMessagesCount] = @(self.unreadMessagesCount);
    
    if (self.occupantIDs.count > 0) {
        info[QBDialogKey.occupantsIds] = self.occupantIDs;
    }
    
    if (self.userID != 0) {
        info[QBDialogKey.userId] = @(self.userID);
    }
    
    if (self.data != nil) {
        info[QBDialogKey.customData] = self.data;
    }
    
    if (self.type == QBChatDialogTypePrivate) {
        info[QBDialogKey.isJoined] = @(YES);
    } else {
        info[QBDialogKey.isJoined] = @(self.isJoined);
    }
    
    return [info.copy toQBResultWithType:QBResultTypeDefault error:error];
}

@end
