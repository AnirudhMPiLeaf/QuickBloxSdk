//
//  QBFileModule+Flutter.m
//  quickblox_sdk
//
//  Created by Injoit on 27.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBFileModule+Flutter.h"

@implementation QBFileModule (Flutter)

+ (void)registerWithRegistrar:(nonnull NSObject<FlutterPluginRegistrar> *)registrar {
    QBFileModule* instance = [[QBFileModule alloc] init];
    [instance setupRegistrar:registrar];
}

@end
