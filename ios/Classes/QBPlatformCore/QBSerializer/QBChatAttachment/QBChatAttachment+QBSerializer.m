//
//  QBChatAttachment+QBSerializer.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatAttachment+QBSerializer.h"

const struct QBChatAttachmentKeysStruct QBAttachmentKey = {
    .id = @"id",
    .name = @"name",
    .contentType = @"contentType",
    .type = @"type",
    .url = @"url",
    .data = @"data",
    .size = @"size",
    .height = @"height",
    .width = @"width",
    .duration = @"duration",
};

@implementation QBChatAttachment (QBSerializer)

+ (NSArray<QBChatAttachment *> *)attachmentsWithInfo:(NSArray<NSDictionary<NSString *,id> *> *)info {
    NSMutableArray<QBChatAttachment *> *attachments = @[].mutableCopy;
    for (NSDictionary<NSString *,NSObject *> *attachmentInfo in info) {
        
        QBChatAttachment *attachment = [[QBChatAttachment alloc] init];
        id ID = attachmentInfo[QBAttachmentKey.id];
        if (ID) {
            attachment.ID = [NSString stringWithFormat:@"%@", ID];
        }
        
        id name = attachmentInfo[QBAttachmentKey.name];
        if (name) {
            attachment.name = [NSString stringWithFormat:@"%@", name];
        }
        
        id contentType = attachmentInfo[QBAttachmentKey.contentType];
        if (contentType &&
            [NSString stringWithFormat:@"%@", contentType].length) {
            [attachment setObject:[NSString stringWithFormat:@"%@", contentType] forKeyedSubscript:@"content-type"];
        }
        
        id type = attachmentInfo[QBAttachmentKey.type];
        if (type) {
            attachment.type = [NSString stringWithFormat:@"%@", type];
        }
        
        id url = attachmentInfo[QBAttachmentKey.url];
        if (url) {
            attachment.url = [NSString stringWithFormat:@"%@", url];
        }
        
        id data = attachmentInfo[QBAttachmentKey.data];
        if (data &&
            [NSString stringWithFormat:@"%@", data].length) {
            [attachment setObject:[NSString stringWithFormat:@"%@", data] forKeyedSubscript:QBAttachmentKey.data];
        }
        
        id size = attachmentInfo[QBAttachmentKey.size];
        if (size &&
            [NSString stringWithFormat:@"%@", size].length) {
            [attachment setObject:[NSString stringWithFormat:@"%@", size] forKeyedSubscript:QBAttachmentKey.size];
        }
        
        id height = attachmentInfo[QBAttachmentKey.height];
        if (height &&
            [NSString stringWithFormat:@"%@", height].length) {
            [attachment setObject:[NSString stringWithFormat:@"%@", height] forKeyedSubscript:QBAttachmentKey.height];
        }
        
        id width = attachmentInfo[QBAttachmentKey.width];
        if (width &&
            [NSString stringWithFormat:@"%@", width].length) {
            [attachment setObject:[NSString stringWithFormat:@"%@", width] forKeyedSubscript:QBAttachmentKey.width];
        }
        
        id duration = attachmentInfo[QBAttachmentKey.duration];
        if (duration &&
            [NSString stringWithFormat:@"%@", duration].length) {
            [attachment setObject:[NSString stringWithFormat:@"%@", duration] forKeyedSubscript:QBAttachmentKey.duration];
        }
        
        [attachments addObject:attachment];
    }
    return attachments.copy;
}

- (id)toQBResultData:(NSError *__autoreleasing *)error {
    NSMutableDictionary *info = @{}.mutableCopy;
    
    if (self.ID.length) {
        info[QBAttachmentKey.id] = self.ID;
    }
    
    if (self.name.length) {
        info[QBAttachmentKey.name] = self.name;
    }
    
    NSString *contentType = self.customParameters[@"content-type"];
    if (contentType.length) {
        info[QBAttachmentKey.contentType] = contentType;
    }
    
    if (self.type.length) {
        info[QBAttachmentKey.type] = self.type;
    }
    
    if (self.url.length) {
        info[QBAttachmentKey.url] = self.url;
    }
    
    NSString *data = self.customParameters[QBAttachmentKey.data];
    if (data.length) {
        info[QBAttachmentKey.data] = data;
    }
    
    NSString *size = self.customParameters[QBAttachmentKey.size];
    if (size.length) {
        info[QBAttachmentKey.size] = @(size.doubleValue);
    }
    
    NSString *height = self.customParameters[QBAttachmentKey.height];
    if (height.length) {
        info[QBAttachmentKey.height] = @(height.integerValue);
    }
    
    NSString *width = self.customParameters[QBAttachmentKey.width];
    if (width.length) {
        info[QBAttachmentKey.width] = @(width.integerValue);
    }
    
    NSString *duration = self.customParameters[QBAttachmentKey.duration];
    if (width.length) {
        info[QBAttachmentKey.duration] = @(duration.integerValue);
    }
    
    return [info.copy toQBResultWithType:QBResultTypeDefault error:error];
}

@end
