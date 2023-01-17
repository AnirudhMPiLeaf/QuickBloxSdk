//
//  QBWebRTCVideoView.m
//  quickblox_sdk
//
//  Created by Injoit on 09.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import "QBWebRTCVideoView.h"

// Temp fix video scaling
// TODO: remove extention after scaling bug fix for the Remote Video View
// (Metal settings, native iOS SDK)
@implementation NSString (QBRemoteLayerFixVideoGravity)

- (NSString *)qbRemoteLayerFix {
    if ([self isEqualToString:AVLayerVideoGravityResizeAspectFill]) {
        return AVLayerVideoGravityResizeAspect;
    } else if ([self isEqualToString:AVLayerVideoGravityResizeAspect]) {
        return AVLayerVideoGravityResizeAspectFill;
    } else if ([self isEqualToString:AVLayerVideoGravityResize]) {
        return self;
    } else {
        return AVLayerVideoGravityResizeAspect;
    }
}

- (NSString *)qbCameraLayerFix {
    if ([self isEqualToString:AVLayerVideoGravityResizeAspect]) {
        return AVLayerVideoGravityResizeAspectFill;
    } else if ([self isEqualToString:AVLayerVideoGravityResizeAspectFill]) {
        return AVLayerVideoGravityResizeAspect;
    } else if ([self isEqualToString:AVLayerVideoGravityResize]) {
        return self;
    } else {
        return AVLayerVideoGravityResizeAspectFill;
    }
}

@end

@interface QBWebRTCVideoView ()

@property (strong, nonatomic) UIView *containerView;

@end

@implementation QBWebRTCVideoView

- (void)setMirror:(BOOL)mirror {
  if (_mirror == mirror) {
    return;
  }
  if (_containerView) {
    NSInteger sx = _mirror ? -1.0 : 1.0;
    _containerView.transform = CGAffineTransformMakeScale( sx, 1.0);
  }
  _mirror = mirror;
  
}

- (void)setVideoLayer:(AVCaptureVideoPreviewLayer *)videoLayer {
    if (!self.containerView) {
        self.containerView = [[UIView alloc] initWithFrame:self.bounds];
        self.containerView.backgroundColor = [UIColor clearColor];
        if (self.mirror) {
            _containerView.transform = CGAffineTransformMakeScale( -1.0, 1.0);
        }
        [self insertSubview:self.containerView atIndex:0];
    }
    self.containerView.frame = self.bounds;
    
    if (_videoLayer) {
        [self.containerView.layer replaceSublayer:_videoLayer with:videoLayer];
    } else {
        [self.containerView.layer insertSublayer:videoLayer atIndex:0];
    }
    _videoLayer = videoLayer;
    [self layoutSubviews];
}
	
- (void)layoutSubviews {
  [super layoutSubviews];
  self.containerView.frame = self.bounds;
  self.videoLayer.frame = self.bounds;
  [self updateOrientationIfNeeded];
}

- (void)willMoveToSuperview:(UIView *)newSuperview {
  [super willMoveToSuperview:newSuperview];
  [self updateOrientationIfNeeded];
}

- (void)updateOrientationIfNeeded {
  
  AVCaptureConnection *previewLayerConnection = self.videoLayer.connection;
  UIInterfaceOrientation interfaceOrientation = [[UIApplication sharedApplication] statusBarOrientation];
  AVCaptureVideoOrientation videoOrientation = (AVCaptureVideoOrientation)interfaceOrientation;
  
  BOOL isVideoOrientationSupported = [previewLayerConnection isVideoOrientationSupported];
  if (isVideoOrientationSupported
      && previewLayerConnection.videoOrientation != videoOrientation) {
    [previewLayerConnection setVideoOrientation:videoOrientation];
  }
}

@end
