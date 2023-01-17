//
//  QBAuthModule.m
//  quickblox_sdk
//
//  Created by Injoit on 23.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBAuthModule.h"
#import "QBAuthConstants.h"
#import "QBSession+QBSerializer.h"
#import "QBUUser+QBSerializer.h"
#import "NSDate+Helper.h"

@interface QBAuthModule()

@property (nonatomic, copy) QBResolveBlock startTokenResolve;
@property (nonatomic, copy) QBRejectBlock startTokenReject;

@end

@interface QBAuthModule (SessionManager)<QBSessionManagerDelegate>
@end

@implementation QBAuthModule

- (void)dealloc {
    if ([QBSessionManager.instance.delegates containsObject:self]) {
      [QBSessionManager.instance removeDelegate:self];
    }
}

- (NSArray<NSString *> *)events {
    return @[ QBAuthSessionEvent.expireSession ];
}

- (void)login:(NSDictionary *)info
     resolver:(QBResolveBlock)resolve
     rejecter:(QBRejectBlock)reject {
    
    NSObject *loginObject = info[AuthKey.login];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:loginObject
              objectKey:AuthKey.login]) {
        return;
    }
    NSString *login = (NSString *)loginObject;
    
    NSObject *passwordObject = info[AuthKey.password];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:passwordObject
              objectKey:AuthKey.password]) {
        return;
    }
    NSString *password = (NSString *)passwordObject;
    [QBRequest logInWithUserLogin:login
                         password:password
                     successBlock:^(QBResponse *response,
                                    QBUUser *user) {
        NSError *error = nil;
        NSDictionary *sessionData = [[QBSession currentSession] toQBResultData:&error];
        NSDictionary *userData = [user toQBResultData:&error];
        if ([error reject:reject]) {
            return;
        }
        if (resolve) {
            resolve(@{ AuthKey.user: userData,
                       AuthKey.session: sessionData });
        }
    } errorBlock:^(QBResponse *response) {
        [response reject:reject];
    }];
}

- (void)loginWithEmail:(NSString *)email
                      :(NSString *)password
              resolver:(QBResolveBlock)resolve
              rejecter:(QBRejectBlock)reject {
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:email
              objectKey:AuthKey.email]) {
        return;
    }
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:password
              objectKey:AuthKey.password]) {
        return;
    }
    [QBRequest logInWithUserEmail:email
                         password:password
                     successBlock:^(QBResponse * _Nonnull response,
                                    QBUUser * _Nonnull tUser) {
        NSError *error = nil;
        NSDictionary *sessionData = [[QBSession currentSession] toQBResultData:&error];
        NSDictionary *userData = [tUser toQBResultData:&error];
        if ([error reject:reject]) {
            return;
        }
        if (resolve) {
            resolve(@{ AuthKey.user: userData,
                       AuthKey.session: sessionData });
        }
    } errorBlock:^(QBResponse * _Nonnull response) {
        [response reject:reject];
    }];
}

- (void)loginWithFacebook:(NSString *)token
                 resolver:(QBResolveBlock)resolve
                 rejecter:(QBRejectBlock)reject {
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:token
              objectKey:AuthKey.token]) {
        return;
    }
    [QBRequest logInWithSocialProvider:@"facebook"
                           accessToken:token
                     accessTokenSecret:nil
                          successBlock:^(QBResponse * _Nonnull response,
                                         QBUUser * _Nonnull tUser) {
        NSError *error = nil;
        NSDictionary *sessionData = [[QBSession currentSession] toQBResultData:&error];
        NSDictionary *userData = [tUser toQBResultData:&error];
        if ([error reject:reject]) {
            return;
        }
        if (resolve) {
            resolve(@{ AuthKey.user: userData,
                       AuthKey.session: sessionData });
        }
    } errorBlock:^(QBResponse * _Nonnull response) {
        [response reject:reject];
    }];
}

- (void)loginWithFirebase:(NSString *)firebaseProjectId
                         :(NSString *)token
                 resolver:(QBResolveBlock)resolve
                 rejecter:(QBRejectBlock)reject {
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:firebaseProjectId
              objectKey:AuthKey.firebaseProjectId]) {
        return;
    }
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:token
              objectKey:AuthKey.token]) {
        return;
    }
    [QBRequest logInWithFirebaseProjectID:firebaseProjectId
                              accessToken:token
                             successBlock:^(QBResponse * _Nonnull response,
                                            QBUUser * _Nonnull tUser) {
        NSError *error = nil;
        NSDictionary *sessionData = [[QBSession currentSession] toQBResultData:&error];
        NSDictionary *userData = [tUser toQBResultData:&error];
        if ([error reject:reject]) {
            return;
        }
        if (resolve) {
            resolve(@{ AuthKey.user: userData,
                       AuthKey.session: sessionData });
        }
    } errorBlock:^(QBResponse * _Nonnull response) {
        [response reject:reject];
    }];
}

- (void)logout:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
    [QBRequest logOutWithSuccessBlock:^(QBResponse *response) {
        if (resolve) { resolve(nil); }
    } errorBlock:^(QBResponse *response) {
        [response reject:reject];
    }];
}

- (void)setSession:(NSDictionary *)info
          resolver:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject {
    if ([NSError reject:reject
                   info:info
           requirements:@[ [Requirement class:NSString.class
                                          key:QBSessionKey.token],
                           [Requirement class:NSNumber.class
                                          key:QBSessionKey.userId],
                           [Requirement class:NSNumber.class
                                          key:QBSessionKey.applicationId] ]
        ]) { return; }
    
    QBASession *session = [[QBASession alloc] init];
    session.token = info[QBSessionKey.token];
    NSNumber *userId = info[QBSessionKey.userId];
    session.userID = userId.unsignedIntValue;
    NSNumber *applicationId = info[QBSessionKey.applicationId];
    session.applicationID = applicationId.unsignedIntValue;
    
    [QBSettings setApplicationID:session.applicationID];
  
    QBSession *currentSession = [QBSession currentSession];
    
    NSString *expirationDate = info[QBSessionKey.expirationDate];
    NSDate *date = [NSDate dateFromQBTokenHeader:expirationDate];
    if (date) {
        [currentSession startSessionWithDetails:session
                                 expirationDate:date];
    } else {
        [currentSession startSessionWithDetails:session];
    }
  
    [currentSession toQBResultDataWithResolver:resolve rejecter:reject];
}

- (void)getSession:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject {
    if (!resolve) { return; }
    
    NSError *error = nil;
    NSDictionary *data = [[QBSession currentSession] toQBResultData:&error];
    NSString *token = data[QBSessionKey.token];
    if (token.length) {
        resolve(data);
        return;
    }
    
    resolve(nil);
}

- (void)clearSession:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject {
    if (!resolve) { return; }
   [QBRequest destroySessionWithSuccessBlock:^(QBResponse *response) {
            resolve(nil);
            printf("session destroyed");
        } errorBlock:^(QBResponse *response) {
            printf("session destroy error ");

            [response reject:reject];
        }];
    resolve(nil);
}

- (void)startSessionWithToken:(NSString *)token
                     resolver:(QBResolveBlock)resolve
                     rejecter:(QBRejectBlock)reject {
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:token
              objectKey:AuthKey.token]) {
        return;
    }
  
    [QBSessionManager.instance addDelegate:self];
    
    [QBSessionManager.instance startSessionWithToken:token];
    self.startTokenResolve = [resolve copy];
    self.startTokenReject = [reject copy];
}

@end

@implementation QBAuthModule (SessionManager)

- (void)sessionManager:(nonnull QBSessionManager *)manager
didNotStartSessionWithError:(NSError * _Nullable)error {
    [error reject:self.startTokenReject];
    [self clearStartTokenCallbacks];
}

- (void)sessionManager:(nonnull QBSessionManager *)manager
didStartSessionWithDetails:(nonnull QBASession *)details {
    QBSession *currentSession = [QBSession currentSession];
    [currentSession toQBResultDataWithResolver:self.startTokenResolve
                                      rejecter:self.startTokenReject];
    [self clearStartTokenCallbacks];
}

- (void)sessionManagerDidExpireSession:(nonnull QBSessionManager *)manager {
    [self postQBEventWithName:QBAuthSessionEvent.expireSession body:nil];
}


// Help
- (void)clearStartTokenCallbacks {
    self.startTokenResolve = nil;
    self.startTokenReject = nil;
}

@end
