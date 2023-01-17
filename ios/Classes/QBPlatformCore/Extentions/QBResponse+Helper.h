//
//  QBResponse+Helper.h
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Quickblox/Quickblox.h>
#import "QBBridgeMethod.h"

@interface QBResponse (Helper)

- (NSString *)statusCode;
- (NSString *)errorMessage;
- (NSError *)nsError;

- (void)reject:(QBRejectBlock)reject;

@end
