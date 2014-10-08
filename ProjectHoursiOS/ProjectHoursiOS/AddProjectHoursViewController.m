//
//  AddProjectHoursViewController.m
//  ProjectHoursiOS
//
//  Created by Ryan Wahle on 10/7/14.
//  Copyright (c) 2014 Ryan Wahle. All rights reserved.
//

#import "AddProjectHoursViewController.h"
#import <Parse/Parse.h>

@interface AddProjectHoursViewController ()

@property (weak, nonatomic) IBOutlet UITextField *projectNameTextField;
@property (weak, nonatomic) IBOutlet UIPickerView *projectHoursPicker;
@property (weak, nonatomic) IBOutlet UISwitch *projectCompleteSwitch;

@end

@implementation AddProjectHoursViewController

NSInteger selectedProjectHours = 1;

- (IBAction)saveButton:(id)sender {
    if (self.projectNameTextField.text.length == 0) {
        [[[UIAlertView alloc] initWithTitle:@"Error" message:@"You must enter a project name!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
    } else {
        PFObject *newProjectHours = [PFObject objectWithClassName:@"ProjectHours"];
        newProjectHours[@"projectName"] = self.projectNameTextField.text;
        newProjectHours[@"hoursWorked"] = @(selectedProjectHours);
        newProjectHours[@"projectComplete"] = @(self.projectCompleteSwitch.isOn);
        newProjectHours.ACL = [PFACL ACLWithUser:[PFUser currentUser]];
        
        [newProjectHours saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
            if (error) {
                [[[UIAlertView alloc] initWithTitle:@"Error" message:error.description delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
            } else {
                [self.navigationController popViewControllerAnimated:YES];
            }
        }];
    }
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return 24;
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    return [NSString stringWithFormat:@"%ld", row + 1];
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    selectedProjectHours = row + 1;
}

@end
