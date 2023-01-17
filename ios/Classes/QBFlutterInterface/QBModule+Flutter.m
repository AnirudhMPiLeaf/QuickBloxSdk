//
//  QBModule+Flutter.m
//  quickblox_sdk
//
//  Created by Injoit on 27.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBModule+Flutter.h"
#import <objc/runtime.h>

static void *_qb_flutterEvents;
static void *_qb_eventSinks;

@interface QBSELProvider : NSObject

+ (SEL)SELWithMetod:(NSString *)method;
+ (SEL)SELWithMetod:(NSString *)method argsCount:(NSInteger)argsCount;

@end

@implementation QBSELProvider

+ (SEL)SELWithMetod:(NSString *)method {
    return [QBSELProvider SELWithMetod:method argsCount:0];
}

+ (SEL)SELWithMetod:(NSString *)method argsCount:(NSInteger)argsCount {
    if (argsCount) {
        NSMutableString *argumentsFormat = @"".mutableCopy;
        for (NSUInteger i = 0; i < argsCount; i++) {
            [argumentsFormat appendString:@":"];
        }

        NSString *format = [NSString stringWithFormat:@"%@resolver:rejecter:", argumentsFormat.copy];
        NSString *selectorString = [method stringByAppendingFormat:@"%@", format];
        return NSSelectorFromString(selectorString);
    }
    
    NSString *selectorString = [method stringByAppendingFormat:@":rejecter:"];
    return NSSelectorFromString(selectorString);
}

@end

@implementation QBModule (Flutter)

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    SEL getSelector = [QBSELProvider SELWithMetod:call.method];
    
    QBResolveBlock resolve = ^(id _Nullable qbResult) {
        result(qbResult);
    };
    
    QBRejectBlock reject = ^(NSString * _Nonnull code,
                             NSString * _Nullable message,
                             NSError * _Nullable error) {
        result([FlutterError errorWithCode:[[code stringByAppendingString:@"\n"] stringByAppendingString:message] message:nil details:error.localizedDescription]);
    };
    
    id arguments = call.arguments;
    NSObject *objectValue = arguments;
    if ([arguments isKindOfClass:NSDictionary.class]) {
        NSMutableDictionary *args = @{}.mutableCopy;
        NSDictionary *objectData = (NSDictionary *)arguments;
        for (NSString *key in objectData.allKeys) {
            id value = objectData[key];
            if (![value isKindOfClass:NSNull.class]) {
                args[key] = value;
            }
        }
        objectValue = args.copy;
    }
    
    NSArray *methodArguments = @[];
    if ([arguments isKindOfClass:NSArray.class]) {
        NSArray *args = (NSArray *)arguments;
        SEL selector = [QBSELProvider SELWithMetod:call.method argsCount:1];
        if ([self.methodsWithArray containsObject:NSStringFromSelector(selector)]) {
            methodArguments = [NSArray arrayWithObject:objectValue];
        } else {
            methodArguments = [NSArray arrayWithArray:args];
        }
    } else if (objectValue) {
        methodArguments = [NSArray arrayWithObject:objectValue];
    }
    
    SEL setSelector = [QBSELProvider SELWithMetod:call.method argsCount:methodArguments.count?:1];
    
    if ([self respondsToSelector:setSelector]) {
        NSMethodSignature *signature  = [self methodSignatureForSelector:setSelector];
        NSInvocation      *invocation = [NSInvocation invocationWithMethodSignature:signature];
        [invocation setTarget:self];
        [invocation setSelector:setSelector];
        NSInteger index = 2;
        if (methodArguments.count) {
            for (id argument in methodArguments) {
                NSObject *objectValue = argument;
                [invocation setArgument:&objectValue atIndex:index];
                index += 1;
            }
        } else {
            index += 1;
        }
        [invocation setArgument:&resolve atIndex:index];
        index += 1;
        [invocation setArgument:&reject atIndex:index];
        
        [invocation invoke];
    } else if ([self respondsToSelector:getSelector]) {
        NSMethodSignature *signature  = [self methodSignatureForSelector:getSelector];
        NSInvocation      *invocation = [NSInvocation invocationWithMethodSignature:signature];
        [invocation setTarget:self];
        [invocation setSelector:getSelector];
        [invocation setArgument:&resolve atIndex:2];
        [invocation setArgument:&reject atIndex:3];
        
        [invocation invoke];
    } else {
        result(FlutterMethodNotImplemented);
    }
}

- (NSString *)channelName {
    NSString *className = NSStringFromClass(self.class);
    return [@"Flutter" stringByAppendingString:[className stringByReplacingOccurrencesOfString:@"Module" withString:@"Channel"]];;
}

- (void)setupRegistrar:(NSObject<FlutterPluginRegistrar> *)registrar {
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:self.channelName
                                     binaryMessenger:[registrar messenger]];
    [registrar addMethodCallDelegate:(NSObject <FlutterPlugin> *)self channel:channel];
    
    for (NSString *eventName in self.events) {
        NSString *flutterEventName = [NSString stringWithFormat:@"%@/%@", self.channelName, eventName];
        FlutterEventChannel *eventChannel =
        [FlutterEventChannel eventChannelWithName:flutterEventName
                                  binaryMessenger:[registrar messenger]];
        [eventChannel setStreamHandler:self];
        self.flutterEvents[flutterEventName] = sdkEvent(eventName);
    }
}

- (NSMutableDictionary<NSString *, NSString *>*)flutterEvents {
    NSMutableDictionary<NSString *, NSString *>*result = objc_getAssociatedObject(self,
                                                                                  &_qb_flutterEvents);
    if (result == nil) {
        result = @{}.mutableCopy;
        objc_setAssociatedObject(self,
                                 &_qb_flutterEvents,
                                 result,
                                 OBJC_ASSOCIATION_RETAIN_NONATOMIC);
    }
    return result;
}

- (NSMutableDictionary<NSString *, FlutterEventSink>*)eventSinks
{
    NSMutableDictionary<NSString *, FlutterEventSink>*result = objc_getAssociatedObject(self,
                                                                                        &_qb_eventSinks);
    if (result == nil) {
        result = @{}.mutableCopy;
        objc_setAssociatedObject(self,
                                 &_qb_eventSinks,
                                 result,
                                 OBJC_ASSOCIATION_RETAIN_NONATOMIC);
    }
    return result;
}

- (FlutterError * _Nullable)onCancelWithArguments:(id _Nullable)arguments {
    NSString *flutterEventName = (NSString *)arguments;

    NSString *event = self.flutterEvents[flutterEventName];
    
    if (self.eventSinks[event]) {
        [self.eventSinks removeObjectForKey:event];
        [NSNotificationCenter.defaultCenter removeObserver:self
          name:event
        object:nil];
    }
    
    return nil;
}

- (FlutterError * _Nullable)onListenWithArguments:(id _Nullable)arguments
                                        eventSink:(nonnull FlutterEventSink)events {
    NSString *flutterEventName = (NSString *)arguments;

    NSString *event = self.flutterEvents[flutterEventName];
    
    if ((!events) && self.eventSinks[event]) {
        [self.eventSinks removeObjectForKey:event];
        [NSNotificationCenter.defaultCenter removeObserver:self
                                                      name:event
                                                    object:nil];
        return nil;
    }
    
    if (self.eventSinks[event]) {
        self.eventSinks[event] = [events copy];
        return nil;
    }
    
    self.eventSinks[event] = [events copy];
    
    [NSNotificationCenter.defaultCenter addObserver:self
                                           selector:@selector(didReceiveQBEventNotification:)
                                               name:event
                                             object:nil];
    return nil;
}

- (void)didReceiveQBEventNotification:(NSNotification *) notification {
    FlutterEventSink eventSink = self.eventSinks[notification.name];
    if (!eventSink) {
        return;
    }
    
    if ([notification.object isKindOfClass:NSDictionary.class]) {
        NSDictionary *object = notification.object;
        NSMutableDictionary *data = object.mutableCopy;
        NSString *type = data[QBBridgeEventKey.type];
        // Remove QB substring from event name
        if (type.length) {
            NSString *subsctring = @"";
            if ([type containsString:@"QB/"]) {
                subsctring = @"QB/";
            }
            if ([type containsString:@"@QB/"]) {
                subsctring = @"@QB/";
            }
            if (subsctring.length) {
                NSString *clearType = [type stringByReplacingOccurrencesOfString:subsctring withString:@""];
                NSString *flutterEventName = [NSString stringWithFormat:@"%@/%@", self.channelName, clearType];
                data[QBBridgeEventKey.type] = flutterEventName;
                eventSink(data.copy);
                return;
            }
        }
    }
    
    eventSink(notification.object);
}

@end
