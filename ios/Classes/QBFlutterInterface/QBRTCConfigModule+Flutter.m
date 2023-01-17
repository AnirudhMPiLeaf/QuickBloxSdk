//
//  QBRTCConfigModule+Flutter.m
//  quickblox_sdk
//
//  Created by Illia Chemolosov on 14.01.2021.
//

#import "QBRTCConfigModule+Flutter.h"

@implementation QBRTCConfigModule (Flutter)

+ (void)registerWithRegistrar:(nonnull NSObject<FlutterPluginRegistrar> *)registrar {
    QBRTCConfigModule* instance = [[QBRTCConfigModule alloc] init];
    [instance setupRegistrar:registrar];
}

@end
