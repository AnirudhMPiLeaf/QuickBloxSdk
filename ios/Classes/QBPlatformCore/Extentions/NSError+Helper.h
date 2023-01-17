//
//  NSError+Helper.h
//  quickblox_sdk
//
//  Created by Injoit on 24.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBBridgeMethod.h"

NS_ASSUME_NONNULL_BEGIN

@interface Requirement : NSObject

@property (readonly, assign, nonatomic) Class requirementClass;
@property (readonly, strong, nonatomic) NSString *type;
@property (readonly, strong, nonatomic) NSString *key;

+ (id)class:(Class)requirementClass key:(NSString *)key;

// Unavailable initializers
- (id)init NS_UNAVAILABLE;
+ (id)new NS_UNAVAILABLE;

- (id)initWithClass:(Class)requirementClass key:(NSString *)key;

@end

@interface NSError (Helper)

+ (NSError *)errrorWithRNQBMessage:(NSString *)message;

+ (void)reject:(QBRejectBlock)reject
       message:(NSString *)message;

+ (BOOL)reject:(QBRejectBlock)reject
  checkerClass:(Class)checkerClass
        object:(NSObject *)object
     objectKey:(NSString *)key;

+ (BOOL)reject:(QBRejectBlock)reject
          info:(NSDictionary<NSString *, id>*)info
requirements:(NSArray<Requirement *> *)requirements;

- (BOOL)reject:(QBRejectBlock)reject;

@end

NS_ASSUME_NONNULL_END
