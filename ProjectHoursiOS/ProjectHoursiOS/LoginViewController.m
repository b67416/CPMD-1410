//
//  LoginViewController.m
//  ProjectHoursiOS
//
//  Created by Ryan Wahle on 10/2/14.
//  Copyright (c) 2014 Ryan Wahle. All rights reserved.
//

#import "LoginViewController.h"
#import <Parse/Parse.h>

@interface LoginViewController ()

@property (weak, nonatomic) IBOutlet UITextField *usernameTextField;
@property (weak, nonatomic) IBOutlet UITextField *passwordTextField;

@end

@implementation LoginViewController

- (IBAction)signinButton {
    if (self.usernameTextField.text.length == 0 || self.passwordTextField.text.length ==0) {
        [[[UIAlertView alloc] initWithTitle:@"Login Error" message:@"You must enter both a username and password!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
    } else {
        [PFUser logInWithUsernameInBackground:self.usernameTextField.text password:self.passwordTextField.text block:^(PFUser *user, NSError *error) {
            if (user) {
                [self dismissViewControllerAnimated:YES completion:nil];
            } else {
                [[[UIAlertView alloc] initWithTitle:@"Login Error" message:[error userInfo][@"error"] delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
            }
        }];
    }
}

@end
