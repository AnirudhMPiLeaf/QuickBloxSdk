//
//  NSDate+Helper.m
//  RNQbReactnative
//
//  Created by Injoit on 6/13/19.
//  Copyright © 2019 Quickblox. All rights reserved.
//

#import "NSDate+Helper.h"

@implementation NSDate (Helper)

+ (NSString *)stringFromISO8601Date:(NSDate *)date {
    if (date == nil) {
        return nil;
    }
    NSTimeZone *GMT = [NSTimeZone timeZoneWithAbbreviation: @"GMT"];
    NSISO8601DateFormatOptions options = NSISO8601DateFormatWithInternetDateTime |
    NSISO8601DateFormatWithDashSeparatorInDate |
    NSISO8601DateFormatWithColonSeparatorInTime |
    NSISO8601DateFormatWithTimeZone;
    return [NSISO8601DateFormatter stringFromDate:date timeZone:GMT formatOptions:options];
}

static NSDateFormatter *_qbTokenDateFormatter = nil;

+ (NSDate *)dateFromQBTokenHeader:(NSString *)str{
    
    if (_qbTokenDateFormatter == nil) {
        _qbTokenDateFormatter = [[NSDateFormatter alloc] init];
        [_qbTokenDateFormatter setDateFormat:@"yyyy-MM-dd'T'HH:mm:ss.SSSZ"];

        NSLocale *posix = [[NSLocale alloc] initWithLocaleIdentifier:@"en_US_POSIX"];
        [_qbTokenDateFormatter setLocale:posix];
    }
    
    NSDate *qbTokenExpirationDate = [_qbTokenDateFormatter dateFromString:str];
    
    return qbTokenExpirationDate;
}

+ (NSDate *)numberConvert:(NSNumber *)number
{
  return [NSDate dateWithTimeIntervalSince1970:(number.doubleValue / 1000.0)];
}

@end
