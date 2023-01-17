//
//  QBRTCSession+QBSerializer.m
//  quickblox_sdk
//
//  Created by Injoit on 03.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import "QBRTCSession+QBSerializer.h"

const struct QBRTCSessionKeysStruct QBRTCSessionKey = {
    .id = @"id",
    .type = @"type",
    .state = @"state",
    .initiatorId = @"initiatorId",
    .opponentsIds = @"opponentsIds"
};

@implementation QBRTCSession (QBSerializer)

- (NSString *)id {
    return [self.ID lowercaseString];
}


- (id)toQBResultData:(NSError *__autoreleasing *)error {
    NSMutableDictionary *info = @{}.mutableCopy;
    
    if (self.ID.length) {
        info[QBRTCSessionKey.id] = [self.ID lowercaseString];
    }
    
    info[QBRTCSessionKey.type] = @(self.conferenceType);
    info[QBRTCSessionKey.state] = @(self.state);
    
    if (self.opponentsIDs.count) {
        info[QBRTCSessionKey.opponentsIds] = self.opponentsIDs;
    }
    
    if (self.initiatorID) {
        info[QBRTCSessionKey.initiatorId] = self.initiatorID;
    }
    
    return [info.copy toQBResultWithType:QBResultTypeDefault error:error];
}

@end
