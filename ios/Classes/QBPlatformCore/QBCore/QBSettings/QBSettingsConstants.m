//
//  QBSettingsConstants.m
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBSettingsConstants.h"

const struct SettingsKeysStruct SettingsKey = {
    .appId = @"appId",
    .authKey = @"authKey",
    .authSecret = @"authSecret",
    .accountKey = @"accountKey",
    .apiEndpoint = @"apiEndpoint",
    .chatEndpoint = @"chatEndpoint",
    .chatEndpointPort = @"chatEndpointPort",
    .sdkVersion = @"sdkVersion",
    .autoReconnect = @"autoReconnect",
    .messageTimeout = @"messageTimeout",
    .enable = @"enable",
};

@implementation QBSettingsConstants

@end
