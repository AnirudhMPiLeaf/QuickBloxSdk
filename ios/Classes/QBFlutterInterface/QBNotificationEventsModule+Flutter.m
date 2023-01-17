//
//  QBNotificationEventsModule+Flutter.m
//  quickblox_sdk
//
//  Created by Injoit on 27.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBNotificationEventsModule+Flutter.h"

@implementation QBNotificationEventsModule (Flutter)

+ (void)registerWithRegistrar:(nonnull NSObject<FlutterPluginRegistrar> *)registrar {
    QBNotificationEventsModule* instance = [[QBNotificationEventsModule alloc] init];
    [instance setupRegistrar:registrar];
}

@end
