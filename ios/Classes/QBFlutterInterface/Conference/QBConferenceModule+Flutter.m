//
//  QBConferenceModule+Flutter.m
//  quickblox_sdk
//
//  Created by Injoit on 12.02.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import "QBConferenceModule+Flutter.h"
#import "QBConferenceViewFactory.h"

@implementation QBConferenceModule (Flutter)

+ (void)registerWithRegistrar:(nonnull NSObject<FlutterPluginRegistrar> *)registrar {
    QBConferenceModule *instance = [[QBConferenceModule alloc] init];
    [instance setupRegistrar:registrar];
    QBConferenceViewFactory *factory = [QBConferenceViewFactory factoryWithMessenger:[registrar messenger]];
    factory.delegate = instance;
    [registrar registerViewFactory:factory withId:@"QBConferenceViewFactory"];
}

@end
