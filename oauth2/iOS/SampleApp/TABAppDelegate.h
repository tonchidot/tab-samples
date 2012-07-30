//
//  TABAppDelegate.h
//  SampleApp
//
//  Copyright (c) 2012年 Tonchidot Corp. All rights reserved.
//

#import <UIKit/UIKit.h>

@class TABViewController;

@interface TABAppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) UINavigationController *navigationController;
@property (strong, nonatomic) TABViewController *viewController;

@end
