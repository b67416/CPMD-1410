//
//  RegisterViewController.m
//  ProjectHoursiOS
//
//  Created by Ryan Wahle on 10/6/14.
//  Copyright (c) 2014 Ryan Wahle. All rights reserved.
//

#import "RegisterViewController.h"
#import "Reachability.h"
#import <Parse/Parse.h>

@interface RegisterViewController ()

@property (weak, nonatomic) IBOutlet UITextField *usernameTextField;
@property (weak, nonatomic) IBOutlet UITextField *passwordTextField;

@end

@implementation RegisterViewController

- (BOOL)isInternetAvailable {
    Reachability *networkReachability = [Reachability reachabilityForInternetConnection];
    NetworkStatus networkStatus = [networkReachability currentReachabilityStatus];
    
    if (networkStatus == NotReachable) {
        return false;
    }
    
    // if ([self isInternetAvailable] == NO) {
    //  [[[UIAlertView alloc] initWithTitle:@"Internet Connection Error" message:@"Please connect to the internet and try again!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
    //  return;
    // }
    
    return true;
}

- (IBAction)createAccountButton:(id)sender {
    if ([self isInternetAvailable] == NO) {
        [[[UIAlertView alloc] initWithTitle:@"Internet Connection Error" message:@"Please connect to the internet and try again!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
        return;
    }
    
    PFUser *newUser = [PFUser user];
    newUser.username = self.usernameTextField.text;
    newUser.password = self.passwordTextField.text;
    
    if (self.usernameTextField.text.length == 0 || self.passwordTextField.text.length ==0) {
        [[[UIAlertView alloc] initWithTitle:@"Login Error" message:@"You must enter both a username and password!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
    } else {
    [newUser signUpInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
        if (error) {
            [[[UIAlertView alloc] initWithTitle:@"Registration Error" message:@"Username already exists! Please try again with a different username!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
        } else {
            [self.navigationController dismissViewControllerAnimated:YES completion:nil];
        }
    }];
    }
}

@end
