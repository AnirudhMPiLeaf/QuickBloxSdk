//
//  QBSettingsConstants.h
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>

struct SettingsKeysStruct {
    __unsafe_unretained NSString * _Nonnull const appId;
    __unsafe_unretained NSString * _Nonnull const authKey;
    __unsafe_unretained NSString * _Nonnull const authSecret;
    __unsafe_unretained NSString * _Nonnull const accountKey;
    __unsafe_unretained NSString * _Nonnull const apiEndpoint;
    __unsafe_unretained NSString * _Nonnull const chatEndpoint;
    __unsafe_unretained NSString * _Nonnull const chatEndpointPort;
    __unsafe_unretained NSString * _Nonnull const sdkVersion;
    __unsafe_unretained NSString * _Nonnull const autoReconnect;
    __unsafe_unretained NSString * _Nonnull const messageTimeout;
    __unsafe_unretained NSString * _Nonnull const enable;
};

extern const struct SettingsKeysStruct SettingsKey;

NS_ASSUME_NONNULL_BEGIN

@interface QBSettingsConstants : NSObject

@end

NS_ASSUME_NONNULL_END
