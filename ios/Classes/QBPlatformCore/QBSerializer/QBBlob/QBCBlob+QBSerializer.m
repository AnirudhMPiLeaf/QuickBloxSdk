//
//  QBCBlob+QBSerializer.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBCBlob+QBSerializer.h"
#import "NSDate+Helper.h"

const struct QBCBlobKeysStruct QBCBlobKey = {
    .id = @"id",
    .uid = @"uid",
    .contentType = @"contentType",
    .name = @"name",
    .size = @"size",
    .completedAt = @"completedAt",
    .isPublic = @"isPublic",
    .lastReadAccessTime = @"lastReadAccessTime",
    .tags = @"tags",
};

@implementation QBCBlob (QBSerializer)

- (id)toQBResultData:(NSError *__autoreleasing *)error {
    NSMutableDictionary* info = [NSMutableDictionary dictionary];
    
    if (self.ID != 0) {
        info[QBCBlobKey.id] = @(self.ID);
    }
    
    if (self.UID.length) {
        info[QBCBlobKey.uid] = self.UID;
    }
    
    if (self.contentType.length) {
        info[QBCBlobKey.contentType] = self.contentType;
    }
    
    if (self.name.length) {
        info[QBCBlobKey.name] = self.name;
    }
    
    info[QBCBlobKey.size] = @(self.size);
    
    NSString *completedAt = [NSDate stringFromISO8601Date:self.completedAt];
    if (completedAt.length) {
        info[QBCBlobKey.completedAt] = completedAt;
    }
    
    info[QBCBlobKey.isPublic] = @(self.isPublic);
    
    NSString *lastReadAccessTime = [NSDate stringFromISO8601Date:self.lastReadAccessTs];
    if (lastReadAccessTime.length) {
        info[QBCBlobKey.lastReadAccessTime] = lastReadAccessTime;
    }
    
    if (self.tags.length) {
        info[QBCBlobKey.tags] = self.tags;
    }
    
    return [info.copy toQBResultWithType:QBResultTypeDefault error:error];
}

@end
