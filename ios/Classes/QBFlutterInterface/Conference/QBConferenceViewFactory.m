//
//  QBConferenceViewFactory.m
//  quickblox_sdk
//
//  Created by Injoit on 12.02.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import "QBConferenceViewFactory.h"
#import "QBConferenceFlutterVideoView.h"

@interface QBConferenceViewFactory ()

@property (nonatomic, strong) NSObject<FlutterBinaryMessenger> *messenger;

@end

@implementation QBConferenceViewFactory

+ (instancetype)factoryWithMessenger:(NSObject<FlutterBinaryMessenger> *)messenger {
    return [[QBConferenceViewFactory alloc] initWithBinaryMessenger:messenger];
}

- (instancetype)initWithBinaryMessenger:(NSObject<FlutterBinaryMessenger>*)messenger {
    self = [super init];
    if (self) {
        _messenger = messenger;
    }
    return self;
}

- (nonnull NSObject<FlutterPlatformView> *)createWithFrame:(CGRect)frame
                                            viewIdentifier:(int64_t)viewId
                                                 arguments:(id _Nullable)args {
    QBConferenceFlutterVideoView *view = [QBConferenceFlutterVideoView viewWithMessenger:self.messenger
                                                                           frame:frame
                                                                  viewIdentifier:viewId];
    view.delegate = self.delegate;
    return view;
}

@end
