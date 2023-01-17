//
//  QBAuthModule+Flutter.m
//  quickblox_sdk
//
//  Created by Injoit on 27.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBAuthModule+Flutter.h"

@implementation QBAuthModule (Flutter)

+ (void)registerWithRegistrar:(nonnull NSObject<FlutterPluginRegistrar> *)registrar {
    QBAuthModule* instance = [[QBAuthModule alloc] init];
    [instance setupRegistrar:registrar];
}

@end
