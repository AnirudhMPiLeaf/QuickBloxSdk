//
//  QBConferenceConstants.h
//  quickblox_sdk
//
//  Created by Injoit on 11.02.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

struct QBWebRTCConferenceEventKeysStruct {
    __unsafe_unretained NSString * const participantReceived;
    __unsafe_unretained NSString * const participantLeft;
    __unsafe_unretained NSString * const error;
    __unsafe_unretained NSString * const close;
    __unsafe_unretained NSString * const sessionState;
    __unsafe_unretained NSString * const videoTrack;
};
extern const struct QBWebRTCConferenceEventKeysStruct QBConferenceEventKey;

typedef NS_ENUM(NSInteger, QB_CONFERENCE_AUDIO_OUTPUT) {
    QB_CONFERENCE_EARSPEAKER = 0,
    QB_CONFERENCE_LOUDSPEAKER = 1,
    QB_CONFERENCE_HEADPHONES = 2,
    QB_CONFERENCE_BLUETOOTH = 3
};

typedef NS_ENUM(NSInteger, QBRTCConfereceState) {
    QBRTCConfereceStateNew,
    QBRTCConfereceStatePending,
    QBRTCConfereceStateConnecting,
    QBRTCConfereceStateConnected,
    QBRTCConfereceStateClosed
};

@interface QBConferenceConstants : NSObject

+ (NSDictionary *)sessionType;
+ (NSDictionary *)sessionState;
+ (NSDictionary *)audioOutputs;
+ (NSDictionary *)scaleTypes;

@end

NS_ASSUME_NONNULL_END
