//
//  QBConferenceCache.m
//  quickblox_sdk
//
//  Created by Injoit on 12.02.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import "QBConferenceCache.h"
#import "QBConferenceWrapper.h"

@interface QBConferenceCache ()

@property (nonatomic, strong) NSMutableDictionary<NSNumber *, NSString *> *sessionCache;
@property (nonatomic, strong) NSMutableDictionary<NSString *, QBConferenceWrapper *> *wrapperCache;

@end

@implementation QBConferenceCache

- (instancetype)init
{
    self = [super init];
    if (self) {
        _sessionCache = @{}.mutableCopy;
        _wrapperCache = @{}.mutableCopy;
    }
    return self;
}

- (id)add:(QBRTCConferenceSession *)session {
    if (!session) { return nil; }
    QBConferenceWrapper *wrapper =
    [[QBConferenceWrapper alloc] initWithSession:session];
    self.sessionCache[wrapper.session.ID] = wrapper.id;
    self.wrapperCache[wrapper.id] = wrapper;
    return wrapper.sessionInfo;
}

- (NSString *)sessionUUIDString:(QBRTCConferenceSession *)session {
    if (!session) { return nil; }
    return self.sessionCache[session.ID];
}

- (QBRTCConferenceSession *)sessionWithUUID:(NSString *)uuidString {
    return self.wrapperCache[uuidString].session;
}

- (QBConferenceWrapper *_Nullable)wrapperWithUUID:(NSString *)uuidString {
    return self.wrapperCache[uuidString];
}

- (id)remove:(QBRTCConferenceSession *)session {
    if (!session) { return nil; }
    NSString *wrapperId = self.sessionCache[session.ID];
    if (wrapperId.length) {
        QBConferenceWrapper *wrapper = self.wrapperCache[wrapperId];
        return wrapper.sessionInfo;
    }
    return nil;
}

@end
