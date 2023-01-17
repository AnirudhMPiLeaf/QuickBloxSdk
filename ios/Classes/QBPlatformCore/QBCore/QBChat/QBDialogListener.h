//
//  QBDialogListener.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Quickblox/Quickblox.h>

@protocol QBDialogListnerDelegate;

@interface QBDialogListener : NSObject <NSCoding, NSCopying>

@property (nonatomic, strong) QBChatDialog *dialog;
@property (nonatomic, weak) id <QBDialogListnerDelegate> delegate;

- (void)subscribeWithDialog:(QBChatDialog *)dialog;
- (void)unsubscribe;

@end

@protocol QBDialogListnerDelegate <NSObject>

- (void)chatDidReciveIsTypingWithUserID:(NSNumber *)userID
                               dialogID:(NSString *)dialogID;
- (void)chatDidReciveStopTypingWithUserID:(NSNumber *)userID
                                 dialogID:(NSString *)dialogID;

@end
