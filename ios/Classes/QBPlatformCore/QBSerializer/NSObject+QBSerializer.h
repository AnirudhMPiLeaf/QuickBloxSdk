//
//  NSObject+QBSerializer.h
//  quickblox_sdk
//
//  Created by Injoit on 24.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBBridgeMethod.h"

@interface NSObject (QBSerializer)

- (void)toQBResultDataWithResolver:(QBResolveBlock)resolve
                          rejecter:(QBRejectBlock)reject;

@end
