//
//  QBRTCConferenceSession+QBSerializer.h
//  quickblox_sdk
//
//  Created by Injoit on 03.02.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import <QuickbloxWebRTC/QuickbloxWebRTC.h>
#import "QBSerializerProtocol.h"

NS_ASSUME_NONNULL_BEGIN

struct QBRTCConferenceSessionKeysStruct {
    __unsafe_unretained NSString * const id;
    __unsafe_unretained NSString * const roomId;
    __unsafe_unretained NSString * const type;
    __unsafe_unretained NSString * const state;
    __unsafe_unretained NSString * const publishers;
};
extern const struct QBRTCConferenceSessionKeysStruct QBRTCConferenceSessionKey;

@interface QBRTCConferenceSession (QBSerializer) <QBSerializerProtocol>

@end

NS_ASSUME_NONNULL_END
