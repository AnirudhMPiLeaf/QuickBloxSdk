//
//  QBRTCSession+QBSerializer.h
//  quickblox_sdk
//
//  Created by Injoit on 03.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import <QuickbloxWebRTC/QuickbloxWebRTC.h>
#import "QBSerializerProtocol.h"

NS_ASSUME_NONNULL_BEGIN

struct QBRTCSessionKeysStruct {
    __unsafe_unretained NSString * const id;
    __unsafe_unretained NSString * const type;
    __unsafe_unretained NSString * const state;
    __unsafe_unretained NSString * const initiatorId;
    __unsafe_unretained NSString * const opponentsIds;
};
extern const struct QBRTCSessionKeysStruct QBRTCSessionKey;

@interface QBRTCSession (QBSerializer) <QBSerializerProtocol>

- (NSString *)id;

@end

NS_ASSUME_NONNULL_END
