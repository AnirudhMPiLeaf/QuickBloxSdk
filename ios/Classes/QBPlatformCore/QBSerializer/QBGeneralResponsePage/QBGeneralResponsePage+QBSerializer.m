//
//  QBGeneralResponsePage+QBSerializer.m
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBGeneralResponsePage+QBSerializer.h"

const struct QBGeneralResponsePageKeysStruct QBGeneralPageKey = {
    .page = @"page",
    .perPage = @"perPage",
    .total = @"total",
};

@implementation QBGeneralResponsePage (QBSerializer)

- (id)toQBResultData:(NSError *__autoreleasing *)error {
    NSMutableDictionary* info = [NSMutableDictionary dictionary];
    
    info[QBGeneralPageKey.page] = @(self.currentPage);
    info[QBGeneralPageKey.perPage] = @(self.perPage);
    info[QBGeneralPageKey.total] = @(self.totalEntries);
    
    return [info.copy toQBResultWithType:QBResultTypeDefault error:error];
}

@end
