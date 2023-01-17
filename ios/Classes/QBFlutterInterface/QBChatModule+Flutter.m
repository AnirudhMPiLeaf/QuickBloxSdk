//
//  QBChatModule+Flutter.m
//  quickblox_sdk
//
//  Created by Injoit on 27.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatModule+Flutter.h"

@implementation QBChatModule (Flutter)

+ (void)registerWithRegistrar:(nonnull NSObject<FlutterPluginRegistrar> *)registrar {
    QBChatModule* instance = [[QBChatModule alloc] init];
    [instance setupRegistrar:registrar];
}

@end
