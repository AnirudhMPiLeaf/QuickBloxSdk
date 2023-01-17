//
//  NSObject+QBSerializer.m
//  quickblox_sdk
//
//  Created by Injoit on 24.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "NSObject+QBSerializer.h"
#import "NSError+Helper.h"
#import "QBSerializerProtocol.h"

@implementation NSObject (QBSerializer)

- (void)toQBResultDataWithResolver:(QBResolveBlock)resolve rejecter:(QBRejectBlock)reject {
    NSError *error = nil;
    if (![self conformsToProtocol:@protocol(QBSerializerProtocol)]) {
        [NSError reject:reject message:@"Wrong data"];
        return;
    }
    
    NSObject<QBSerializerProtocol> *serializer = (NSObject<QBSerializerProtocol> *)self;
    
    id qbResultData = [serializer toQBResultData:&error];
    if (![error reject:reject] && resolve) {
        resolve(qbResultData);
    }
}

@end
