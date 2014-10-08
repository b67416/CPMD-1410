//
//  RegisterViewController.m
//  ProjectHoursiOS
//
//  Created by Ryan Wahle on 10/6/14.
//  Copyright (c) 2014 Ryan Wahle. All rights reserved.
//

#import "RegisterViewController.h"
#import <Parse/Parse.h>

@interface RegisterViewController ()

@property (weak, nonatomic) IBOutlet UITextField *usernameTextField;
@property (weak, nonatomic) IBOutlet UITextField *passwordTextField;

@end

@implementation RegisterViewController

- (IBAction)createAccountButton:(id)sender {
    PFUser *newUser = [PFUser user];
    newUser.username = self.usernameTextField.text;
    newUser.password = self.passwordTextField.text;
    
    if (self.usernameTextField.text.length == 0 || self.passwordTextField.text.length ==0) {
        [[[UIAlertView alloc] initWithTitle:@"Login Error" message:@"You must enter both a username and password!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
    } else {
    [newUser signUpInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
        if (error) {
            [[[UIAlertView alloc] initWithTitle:@"Registration Error" message:[error userInfo][@"error"] delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
        } else {
            [self.navigationController dismissViewControllerAnimated:YES completion:nil];
        }
    }];
    }
}

@end
