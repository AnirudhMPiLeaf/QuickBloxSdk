//
//  QBModule.m
//  quickblox_sdk
//
//  Created by Injoit on 24.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBModule.h"

@implementation QBModule

- (void)postQBEventWithName:(NSString *)eventName body:(id)body {
  NSString *event = sdkEvent(eventName);
  NSMutableDictionary *postObject = @{ QBBridgeEventKey.type : event(eventName) }.mutableCopy;
  if (body) {
    [postObject setObject:body forKey:QBBridgeEventKey.payload];
  }
  NSNotificationCenter *center = NSNotificationCenter.defaultCenter;
  [center postNotificationName:event
                        object:postObject.copy];
}

- (NSArray<NSString *> *)events {
  return @[];
}

- (NSArray<NSString *> *)methodsWithArray {
  return @[];
}

@end
