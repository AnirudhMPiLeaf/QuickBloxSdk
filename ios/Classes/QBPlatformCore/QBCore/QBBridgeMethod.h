//
//  QBBridgeMethod.h
//  quickblox_sdk
//
//  Created by Injoit on 24.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (^QBRejectBlock)(NSString * _Nonnull code,
                              NSString * _Nullable message,
                              NSError * _Nullable error);

typedef void (^QBResolveBlock)(id _Nullable result);
