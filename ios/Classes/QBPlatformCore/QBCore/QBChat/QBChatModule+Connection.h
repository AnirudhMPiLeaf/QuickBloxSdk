//
//  QBChatModule+Connection.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatModule.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBChatModule (Connection)

- (void)connect:(NSDictionary *)info
       resolver:(QBResolveBlock)resolve
       rejecter:(QBRejectBlock)reject;

- (void)isConnected:(QBResolveBlock)resolve
           rejecter:(QBRejectBlock)reject;

- (void)disconnect:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject;

@end

@interface QBChatModule(ConnectionProtocol) <QBChatConnectionProtocol>
@end

NS_ASSUME_NONNULL_END
