//
//  QBChatMessage+QBSerializer.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatMessage+QBSerializer.h"

const struct QBChatMessageKeysStruct QBMessageKey = {
    .id = @"id",
    .dialogId = @"dialogId",
    .body = @"body",
    .dateSent = @"dateSent",
    .senderId = @"senderId",
    .recipientId = @"recipientId",
    .readIds = @"readIds",
    .deliveredIds = @"deliveredIds",
    .attachments = @"attachments",
    .markable = @"markable",
    .delayed = @"delayed",
    .properties = @"properties",
    .saveToHistory = @"saveToHistory",
};

@implementation QBChatMessage (QBSerializer)

- (id)toQBResultData:(NSError *__autoreleasing *)error {
    NSMutableDictionary *info = @{}.mutableCopy;
    if (self.ID.length) {
        info[QBMessageKey.id] = self.ID;
    }
    
    //    NSString *created_at = [NSDate stringFromISO8601Date:self.createdAt];
    //    if (created_at != nil) {
    //        info[QBMessageKey.created_at] = created_at;
    //    }
    //
    //    NSString *updated_at = [NSDate stringFromISO8601Date:self.updatedAt];
    //    if (updated_at != nil) {
    //        info[QBMessageKey.updated_at] = updated_at;
    //    }
    
    if (self.dialogID.length) {
        info[QBMessageKey.dialogId] = self.dialogID;
    }
    
    if (self.text.length) {
        info[QBMessageKey.body] = self.text;
    }
    
    if (self.dateSent) {
        NSUInteger dateSentInterval = [self.dateSent timeIntervalSince1970] * 1000;
        info[QBMessageKey.dateSent] = @(dateSentInterval);
    }
    
    if (self.senderID != 0) {
        info[QBMessageKey.senderId] = @(self.senderID);
    }
    
    if (self.recipientID != 0) {
        info[QBMessageKey.recipientId] = @(self.recipientID);
    }
    
    if (self.readIDs.count) {
        info[QBMessageKey.readIds] = self.readIDs;
    }
    
    if (self.deliveredIDs.count) {
        info[QBMessageKey.deliveredIds] = self.deliveredIDs;
    }
    
    NSArray *attachments = [self.attachments toQBResultArray:error];
    if (attachments.count) {
        info[QBMessageKey.attachments] = attachments;
    }
    
    info[QBMessageKey.markable] = @(self.markable);
    info[QBMessageKey.delayed] = @(self.delayed);
    
    if (self.customParameters.count) {
        info[QBMessageKey.properties] = self.customParameters;
    }
    
    BOOL saveToHistory = self.customParameters[@"save_to_history"].integerValue;
    info[QBMessageKey.saveToHistory] = @(saveToHistory);
    
    return [info.copy toQBResultWithType:QBResultTypeDefault error:error];
}

@end
