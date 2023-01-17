//
//  QBResponse+Helper.m
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBResponse+Helper.h"

@implementation QBResponse (Helper)

- (NSString *)statusCode {
    return [NSString stringWithFormat:@"%@", @(self.status)];
}

- (NSString *)errorMessage {
    if (self.error.reasons) {
        NSObject *reasons = self.error.reasons[@"errors"];
        NSString *errorMessage = [self errorsMessageWithReasons:reasons];
        if (errorMessage.length) {
            return errorMessage;
        }
    }
    NSString *errorMessage = self.nsError.localizedRecoverySuggestion
    ?: self.nsError.localizedDescription;
    if (errorMessage.length) {
        return errorMessage;
    }
    if (self.error.reasons) {
        errorMessage = [NSString stringWithFormat:@"%@", self.error.reasons];
        return errorMessage.length ? errorMessage : @"Unknown error";
    }
    return @"Unknown error";
}

- (NSError *)nsError {
    return self.error.error;
}

- (void)reject:(QBRejectBlock)reject {
    if (reject) {
        reject(self.statusCode,self.errorMessage,self.nsError);
    }
}

- (NSString *)errorsMessageWithReasons:(NSObject *)reasons {
    NSArray *errors = @[];
    if ([reasons isKindOfClass:[NSDictionary class]]) {
        NSMutableArray *allValues = @[].mutableCopy;
        NSDictionary *reasonsObj = (NSDictionary *)reasons;
        for (NSString *key in reasonsObj.allKeys) {
            NSObject *reasonObject = reasonsObj[key];
            NSString *stringValue = @"";
            NSString *keyString = [NSString stringWithFormat:@"\"%@\"", key];
            if ([reasonObject isKindOfClass:NSString.class]) {
                stringValue =
                [keyString stringByAppendingString:[NSString stringWithFormat:@" %@", reasonObject]];
            } else if ([reasonObject isKindOfClass:NSArray.class]) {
                NSArray *reasonArray = (NSArray *)reasonObject;
                for (NSObject *object in reasonArray) {
                    if ([object isKindOfClass:NSString.class]) {
                        NSString *subReason = [keyString stringByAppendingString:[NSString stringWithFormat:@" %@", object]];
                        stringValue = [stringValue stringByAppendingString:[NSString stringWithFormat:@"%@\n", subReason]];
                    }
                }
            }
            [allValues addObject:stringValue.length ? stringValue : reasonObject];
        }
        errors = allValues;
    } else if ([reasons isKindOfClass:[NSArray class]]) {
        errors = (NSArray *)reasons;
    } else if ([reasons isKindOfClass:[NSString class]]) {
        return (NSString *)reasons;
    }
    
    NSString *errorMessage = @"";
    for (NSObject *value in errors) {
        NSString *subError = [self errorsMessageWithReasons:value];
        if (subError.length) {
            if (errorMessage.length) {
                subError = [@"\n" stringByAppendingString:subError];
            }
            errorMessage = [errorMessage stringByAppendingString:subError];
        }
    }
    return errorMessage;
}

@end
