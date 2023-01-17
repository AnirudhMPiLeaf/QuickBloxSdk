//
//  QBResponsePage+QBSerializer.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBResponsePage+QBSerializer.h"

const struct QBResponsePageKeysStruct QBPageKey = {
    .skip = @"skip",
    .limit = @"limit",
    .total = @"total",
};

@implementation QBResponsePage (QBSerializer)

- (id)toQBResultData:(NSError *__autoreleasing *)error {
    NSMutableDictionary* info = [NSMutableDictionary dictionary];
    
    info[QBPageKey.skip] = @(self.skip);
    info[QBPageKey.limit] = @(self.limit);
    info[QBPageKey.total] = @(self.totalEntries);
    
    return [info.copy toQBResultWithType:QBResultTypeDefault error:error];
}

@end
