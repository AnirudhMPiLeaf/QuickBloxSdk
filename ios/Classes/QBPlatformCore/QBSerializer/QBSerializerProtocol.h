//
//  QBSerializerProtocol.h
//  quickblox_sdk
//
//  Created by Injoit on 24.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBBridgeMethod.h"
#import "NSObject+QBSerializer.h"
#import "NSDictionary+QBSerializer.h"
#import "NSArray+QBSerializer.h"

@protocol QBSerializerProtocol <NSObject>

- (id)toQBResultData:(NSError **)error;
- (void)toQBResultDataWithResolver:(QBResolveBlock)resolve
                          rejecter:(QBRejectBlock)reject;

@end
