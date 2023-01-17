//
//  QBCustomObjectsModule+Flutter.m
//  quickblox_sdk
//
//  Created by Injoit on 27.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBCustomObjectsModule+Flutter.h"

@implementation QBCustomObjectsModule (Flutter)

+ (void)registerWithRegistrar:(nonnull NSObject<FlutterPluginRegistrar> *)registrar {
    QBCustomObjectsModule* instance = [[QBCustomObjectsModule alloc] init];
    [instance setupRegistrar:registrar];
}

@end
