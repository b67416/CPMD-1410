//
//  ProjectHoursViewController.m
//  ProjectHoursiOS
//
//  Created by Ryan Wahle on 10/2/14.
//  Copyright (c) 2014 Ryan Wahle. All rights reserved.
//

#import "ProjectHoursViewController.h"
#import "LoginViewController.h"
#import "LoginNavigationController.h"
#import <Parse/Parse.h>

@interface ProjectHoursViewController ()

@end

@implementation ProjectHoursViewController

- (IBAction)logoutButton:(id)sender {
    [PFUser logOut];
    [self checkUserLogin];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    NSLog(@"ProjectHoursViewController loaded . . .");
    
    [self checkUserLogin];
}
     
- (void)checkUserLogin {
    PFUser *currentUser = [PFUser currentUser];
    if (currentUser) {
        NSLog(@"User %@ is logged in . . .", currentUser.username);
    } else {
        LoginNavigationController *loginViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"LoginNavigationController"];
        [self presentViewController:loginViewController animated:YES completion:nil];
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
