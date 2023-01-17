//
//  NSArray+QBSerializer.h
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBBridgeMethod.h"

@interface NSArray (QBSerializer)

- (NSArray *)toQBResultArray:(NSError **)error;
- (void)toQBResultArrayWithResolver:(QBResolveBlock)resolve
                           rejecter:(QBRejectBlock)reject;

@end
