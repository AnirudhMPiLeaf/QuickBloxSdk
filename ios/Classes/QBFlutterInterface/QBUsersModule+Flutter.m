//
//  QBUsersModule+Flutter.m
//  quickblox_sdk
//
//  Created by Injoit on 27.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBUsersModule+Flutter.h"

@implementation QBUsersModule (Flutter)

+ (void)registerWithRegistrar:(nonnull NSObject<FlutterPluginRegistrar> *)registrar {
    QBUsersModule* instance = [[QBUsersModule alloc] init];
    [instance setupRegistrar:registrar];
}

@end
