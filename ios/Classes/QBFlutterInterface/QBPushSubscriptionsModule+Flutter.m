//
//  QBPushSubscriptionsModule+Flutter.m
//  quickblox_sdk
//
//  Created by Injoit on 27.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBPushSubscriptionsModule+Flutter.h"

@implementation QBPushSubscriptionsModule (Flutter)

+ (void)registerWithRegistrar:(nonnull NSObject<FlutterPluginRegistrar> *)registrar {
    QBPushSubscriptionsModule* instance = [[QBPushSubscriptionsModule alloc] init];
    [instance setupRegistrar:registrar];
}

@end
