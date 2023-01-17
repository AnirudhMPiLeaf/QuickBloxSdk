//
//  NSArray+QBSerializer.m
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "NSArray+QBSerializer.h"
#import "NSError+Helper.h"
#import "QBSerializerProtocol.h"

@implementation NSArray (QBSerializer)

- (NSArray *)toQBResultArray:(NSError * _Nullable __autoreleasing *)error {
    NSMutableArray *resultArray = @[].mutableCopy;
       for (NSObject *object in self) {
           if ([object conformsToProtocol:@protocol(QBSerializerProtocol)]) {
               NSObject<QBSerializerProtocol> *serializer = (NSObject<QBSerializerProtocol> *)object;
               id result = [serializer toQBResultData:error];
               if (*error) {
                   return @[];
               }
               [resultArray addObject:result];
           }
       }
       if (*error) {
           return @[];
       }
       return resultArray.copy;
}

- (void)toQBResultArrayWithResolver:(QBResolveBlock)resolve
                           rejecter:(QBRejectBlock)reject {
    NSError *error = nil;
    NSArray *resultArray = [self toQBResultArray:&error];
    if (![error reject:reject] && resolve) {
        resolve(resultArray);
    }
}

@end
