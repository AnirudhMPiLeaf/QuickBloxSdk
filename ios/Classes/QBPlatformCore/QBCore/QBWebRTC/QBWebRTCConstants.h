//
//  QBWebRTCConstants.h
//  quickblox_sdk
//
//  Created by Injoit on 03.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

struct QBWebRTCKeysStruct {
    __unsafe_unretained NSString * const userInfo;
    __unsafe_unretained NSString * const userId;
    __unsafe_unretained NSString * const enabled;
    __unsafe_unretained NSString * const sessionId;
    __unsafe_unretained NSString * const session;
    __unsafe_unretained NSString * const enable;
    __unsafe_unretained NSString * const output;
    __unsafe_unretained NSString * const state;
};
extern const struct QBWebRTCKeysStruct QBWebRTCKey;

struct QBWebRTCCallEventKeysStruct {
    __unsafe_unretained NSString * const call;
    __unsafe_unretained NSString * const notAnswer;
    __unsafe_unretained NSString * const reject;
    __unsafe_unretained NSString * const accept;
    __unsafe_unretained NSString * const hangUp;
    __unsafe_unretained NSString * const callEnd;
    __unsafe_unretained NSString * const connectionState;
    __unsafe_unretained NSString * const reconnectionState;
    __unsafe_unretained NSString * const remoteVideoTrack;
};
extern const struct QBWebRTCCallEventKeysStruct QBCallEventKey;

typedef NS_ENUM(NSInteger, QB_AUDIO_OUTPUT) {
    QB_AUDIO_OUTPUT_EARSPEAKER = 0,
    QB_AUDIO_OUTPUT_LOUDSPEAKER = 1,
    QB_AUDIO_OUTPUT_HEADPHONES = 2,
    QB_AUDIO_OUTPUT_BLUETOOTH = 3
};

typedef NS_ENUM(NSInteger, QBRTCPeerState) {
    QBRTCPeerStateNew,
    QBRTCPeerStateConnected,
    QBRTCPeerStateDisconnected,
    QBRTCPeerStateFailed,
    QBRTCPeerStateClosed
};

@interface QBWebRTCConstants : NSObject

+ (NSDictionary *)sessionType;
+ (NSDictionary *)sessionState;
+ (NSDictionary *)connectionState;
+ (NSDictionary *)reconnectionState;
+ (NSDictionary *)audioOutputs;
+ (NSDictionary *)scaleTypes;

@end

NS_ASSUME_NONNULL_END
