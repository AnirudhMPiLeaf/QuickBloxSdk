//
//  QBWebRTCModule+Flutter.m
//  quickblox_sdk
//
//  Created by Injoit on 09.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import "QBWebRTCModule+Flutter.h"
#import "QBWebRTCViewFactory.h"

@implementation QBWebRTCModule (Flutter)

+ (void)registerWithRegistrar:(nonnull NSObject<FlutterPluginRegistrar> *)registrar {
    QBWebRTCModule *instance = [[QBWebRTCModule alloc] init];
    [instance setupRegistrar:registrar];
    QBWebRTCViewFactory *factory = [QBWebRTCViewFactory factoryWithMessenger:[registrar messenger]];
    factory.delegate = instance;
    [registrar registerViewFactory:factory withId:@"QBWebRTCViewFactory"];
}

@end
