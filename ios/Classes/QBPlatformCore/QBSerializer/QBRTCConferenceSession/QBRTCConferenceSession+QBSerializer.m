//
//  QBRTCConferenceSession+QBSerializer.m
//  quickblox_sdk
//
//  Created by Injoit on 03.02.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import "QBRTCConferenceSession+QBSerializer.h"

const struct QBRTCConferenceSessionKeysStruct QBRTCConferenceSessionKey = {
    .id = @"id",
    .roomId = @"roomId",
    .type = @"type",
    .state = @"state",
    .publishers = @"publishers"
};

@implementation QBRTCConferenceSession (QBSerializer)

- (id)toQBResultData:(NSError *__autoreleasing *)error {
    NSMutableDictionary *info = @{}.mutableCopy;
    
    if (self.chatDialogID.length) {
        info[QBRTCConferenceSessionKey.roomId] = self.chatDialogID;
    }
    
    info[QBRTCConferenceSessionKey.type] = @(self.conferenceType);
    info[QBRTCConferenceSessionKey.state] = @(self.state);
    
    if (self.publishersList
        .count) {
        info[QBRTCConferenceSessionKey.publishers] = self.publishersList;
    }
    
    return [info.copy toQBResultWithType:QBResultTypeDefault error:error];
}

@end
