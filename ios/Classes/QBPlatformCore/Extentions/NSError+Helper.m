//
//  NSError+Helper.m
//  quickblox_sdk
//
//  Created by Injoit on 24.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "NSError+Helper.h"

@implementation Requirement

+ (id)class:(Class)class key:(NSString *)key {
  return [[Requirement alloc] initWithClass:class key:key];
}

- (id)initWithClass:(Class)requirementClass key:(NSString *)key
{
  self = [super init];
  if (self) {
    _requirementClass = requirementClass;
    _key = key;
    NSString *className = NSStringFromClass(_requirementClass);
    _type = [className stringByReplacingOccurrencesOfString:@"NS"
                                                 withString:@""];
  }
  return self;
}

@end

@implementation NSError (Helper)

+ (NSError *)errrorWithRNQBMessage:(NSString *)message {
  NSString *sourceString = [[NSThread callStackSymbols] objectAtIndex:1];
  NSCharacterSet *separatorSet = [NSCharacterSet characterSetWithCharactersInString:@" -[]+?.,"];
  NSArray *components = [sourceString  componentsSeparatedByCharactersInSet:separatorSet];
  NSMutableArray *array = [NSMutableArray arrayWithArray:components];
  [array removeObject:@""];
  NSString *domain = [NSString stringWithFormat:@"Class<%@> method<%@>", array[3], array[4]];
  NSString *key = NSLocalizedRecoverySuggestionErrorKey;
  NSDictionary *userInfo = @{ key: message.length ? message : @"" };
  NSError *error = [NSError errorWithDomain:domain
                                       code:-1
                                   userInfo:userInfo];
  return error;
}

+ (void)reject:(QBRejectBlock)reject
       message:(NSString *)message {
  NSString *sourceString = [[NSThread callStackSymbols] objectAtIndex:1];
  NSCharacterSet *separatorSet = [NSCharacterSet characterSetWithCharactersInString:@" -[]+?.,"];
  NSArray *components = [sourceString  componentsSeparatedByCharactersInSet:separatorSet];
  NSMutableArray *array = [NSMutableArray arrayWithArray:components];
  [array removeObject:@""];
  NSString *domain = [NSString stringWithFormat:@"Class<%@> method<%@>", array[3], array[4]];
  NSString *key = NSLocalizedRecoverySuggestionErrorKey;
  NSDictionary *userInfo = @{ key: message.length ? message : @"" };
  NSError *error = [NSError errorWithDomain:domain
                                       code:-1
                                   userInfo:userInfo];
  [error reject:reject];
}

+ (BOOL)reject:(QBRejectBlock)reject
  checkerClass:(Class)checkerClass
        object:(NSObject *)object
     objectKey:(NSString *)key {
  NSString *errorMessage = @"";
  NSString *stringType = NSStringFromClass(checkerClass);
  stringType = [stringType stringByReplacingOccurrencesOfString:@"NS"
                                                     withString:@""];
  if (![object isKindOfClass:checkerClass]) {
    errorMessage = [NSString stringWithFormat:@"\"%@\" should be of type %@",key, stringType];
  }
  
  if (!object) {
    errorMessage = [NSString stringWithFormat:@"\"%@\" is required.",key];
  }
  
  if ([object isKindOfClass:NSDictionary.class] ||
      [object isKindOfClass:NSArray.class] ||
      [object isKindOfClass:NSSet.class]) {
    NSArray *objectArray = (NSArray *)object;
    if (!objectArray.count) {
      errorMessage = [NSString stringWithFormat:@"\"%@\" is required.",key];
    }
  }
  
  if ([object isKindOfClass:NSString.class]) {
    NSString *objectString = (NSString *)object;
    NSCharacterSet *charSet = [NSCharacterSet whitespaceAndNewlineCharacterSet];
    if (![objectString stringByTrimmingCharactersInSet:charSet].length) {
      errorMessage = [NSString stringWithFormat:@"\"%@\" is required.",key];
    }
  }
  
  if (errorMessage.length) {
    NSString *sourceString = [[NSThread callStackSymbols] objectAtIndex:1];
    NSCharacterSet *separatorSet = [NSCharacterSet characterSetWithCharactersInString:@" -[]+?.,"];
    NSMutableArray *array = [NSMutableArray arrayWithArray:[sourceString  componentsSeparatedByCharactersInSet:separatorSet]];
    [array removeObject:@""];
    NSString *domain = [NSString stringWithFormat:@"Class<%@> method<%@>", array[3], array[4]];
    NSString *key = NSLocalizedRecoverySuggestionErrorKey;
    NSDictionary *userInfo = @{ key: errorMessage };
    NSError *error = [NSError errorWithDomain:domain
                                         code:-1
                                     userInfo:userInfo];
    [error reject:reject];
    return YES;
  }
  
  return NO;
}

+ (BOOL)reject:(QBRejectBlock)reject
          info:(NSDictionary<NSString *, id>*)info
  requirements:(NSArray<Requirement *> *)requirements {
  for (Requirement *requirement in requirements) {
    NSString *errorMessage = @"";
    id object = info[requirement.key];
    if (![object isKindOfClass:requirement.requirementClass]) {
      errorMessage = [NSString stringWithFormat:@"\"%@\" should be of type %@",requirement.key, requirement.type];
    }
    
    if (!object) {
      errorMessage = [NSString stringWithFormat:@"\"%@\" is required.",requirement.key];
    }
    
    if ([object isKindOfClass:NSDictionary.class] ||
        [object isKindOfClass:NSArray.class] ||
        [object isKindOfClass:NSSet.class]) {
      NSArray *objectArray = (NSArray *)object;
      if (!objectArray.count) {
        errorMessage = [NSString stringWithFormat:@"\"%@\" is empty.", requirement.key];
      }
    }
    
    if ([object isKindOfClass:NSString.class]) {
      NSString *objectString = (NSString *)object;
      NSCharacterSet *charSet = [NSCharacterSet whitespaceAndNewlineCharacterSet];
      if (![objectString stringByTrimmingCharactersInSet:charSet].length) {
        errorMessage = [NSString stringWithFormat:@"\"%@\" is empty.", requirement.key];
      }
    }
    
    if (errorMessage.length) {
      NSString *sourceString = [[NSThread callStackSymbols] objectAtIndex:1];
      NSCharacterSet *separatorSet = [NSCharacterSet characterSetWithCharactersInString:@" -[]+?.,"];
      NSMutableArray *array = [NSMutableArray arrayWithArray:[sourceString  componentsSeparatedByCharactersInSet:separatorSet]];
      [array removeObject:@""];
      NSString *domain = [NSString stringWithFormat:@"Class<%@> method<%@>", array[3], array[4]];
      NSString *key = NSLocalizedRecoverySuggestionErrorKey;
      NSDictionary *userInfo = @{ key: errorMessage };
      NSError *error = [NSError errorWithDomain:domain
                                           code:-1
                                       userInfo:userInfo];
      [error reject:reject];
      return YES;
    }
  }
  return NO;
}

- (BOOL)reject:(QBRejectBlock)reject {
  if (reject) {
    reject([@(self.code) stringValue],
           self.localizedRecoverySuggestion
           ?: self.localizedDescription,
           self);
  }
  return YES;
}

@end
