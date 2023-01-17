//
//  NSDate+Helper.h
//  RNQbReactnative
//
//  Created by Injoit on 6/13/19.
//  Copyright © 2019 Quickblox. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSDate (Helper)

+ (NSString *)stringFromISO8601Date:(NSDate *)date;
+ (NSDate *)dateFromQBTokenHeader:(NSString *)str;
+ (NSDate *)numberConvert:(NSNumber *)number;

@end
