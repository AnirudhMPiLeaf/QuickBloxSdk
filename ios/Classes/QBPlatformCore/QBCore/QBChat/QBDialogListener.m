//
//  QBDialogListener.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBDialogListener.h"

@implementation QBDialogListener

- (void)encodeWithCoder:(nonnull NSCoder *)aCoder {
    [aCoder encodeObject:self.dialog forKey:@"dialog"];
}

- (nullable instancetype)initWithCoder:(nonnull NSCoder *)aDecoder {
    if (self = [super init]) {
        _dialog = [aDecoder decodeObjectForKey:@"dialog"];
    }
    
    return self;
}

- (nonnull id)copyWithZone:(nullable NSZone *)zone {
    QBDialogListener *copy = [[QBDialogListener allocWithZone:zone] init];
    copy.dialog = [self.dialog copyWithZone:zone];
    return copy;
}

- (BOOL)isEqual:(id)dialogListner {
    if (self == dialogListner) {
        return YES;
    }
    
    if (![dialogListner isKindOfClass:[QBDialogListener class]]) {
        return NO;
    }
    
    QBDialogListener *other = (QBDialogListener *)dialogListner;
    
    return [self.dialog.ID isEqualToString:other.dialog.ID];
}

- (void)subscribeWithDialog:(QBChatDialog *)dialog {
    self.dialog = dialog;
    __weak __typeof(self)weakSelf = self;
    [dialog setOnUserIsTyping:^(NSUInteger userID) {
        if ([weakSelf.delegate respondsToSelector:@selector(chatDidReciveIsTypingWithUserID:dialogID:)]) {
            [weakSelf.delegate chatDidReciveIsTypingWithUserID:@(userID)
                                                      dialogID:weakSelf.dialog.ID];
        }
    }];
    
    [dialog setOnUserStoppedTyping:^(NSUInteger userID) {
        if ([weakSelf.delegate respondsToSelector:@selector(chatDidReciveStopTypingWithUserID:dialogID:)]) {
            [weakSelf.delegate chatDidReciveStopTypingWithUserID:@(userID)
                                                        dialogID:weakSelf.dialog.ID];
        }
    }];
}

- (void)unsubscribe {
    [self.dialog clearTypingStatusBlocks];
    self.dialog = nil;
}

@end
