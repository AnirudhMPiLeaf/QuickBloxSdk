//
//  QBFileModule.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBModule.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBFileModule : QBModule

- (void)upload:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject;

- (void)subscribeUploadProgress:(NSDictionary *)info
                       resolver:(QBResolveBlock)resolve
                       rejecter:(QBRejectBlock)reject;

- (void)unsubscribeUploadProgress:(NSDictionary *)info
                         resolver:(QBResolveBlock)resolve
                         rejecter:(QBRejectBlock)reject;

- (void)getInfo:(NSDictionary *)info
       resolver:(QBResolveBlock)resolve
       rejecter:(QBRejectBlock)reject;

- (void)getPublicURL:(NSDictionary *)info
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject;

- (void)getPrivateURL:(NSDictionary *)info
             resolver:(QBResolveBlock)resolve
             rejecter:(QBRejectBlock)reject;

@end

NS_ASSUME_NONNULL_END
