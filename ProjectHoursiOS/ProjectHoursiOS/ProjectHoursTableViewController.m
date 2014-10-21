//
//  ProjectHoursTableViewController.m
//  ProjectHoursiOS
//
//  Created by Ryan Wahle on 10/7/14.
//  Copyright (c) 2014 Ryan Wahle. All rights reserved.
//

#import "ProjectHoursTableViewController.h"
#import "ProjectHoursTableViewCell.h"
#import "LoginNavigationController.h"
#import "AddProjectHoursViewController.h"
#import "Reachability.h"
#import <Parse/Parse.h>

@interface ProjectHoursTableViewController ()

@end

@implementation ProjectHoursTableViewController

NSArray *projectHoursArray = nil;
NSTimer *dataRefreshTimer = nil;

- (BOOL)isInternetAvailable {
    Reachability *networkReachability = [Reachability reachabilityForInternetConnection];
    NetworkStatus networkStatus = [networkReachability currentReachabilityStatus];
    
    if (networkStatus == NotReachable) {
        return false;
    }
    
    return true;
}

- (IBAction)logoutButton:(id)sender {
    [PFUser logOut];
    
    projectHoursArray = nil;
    [self.tableView reloadData];
    
    [self checkUserLogin];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self checkUserLogin];
    
    dataRefreshTimer = [NSTimer scheduledTimerWithTimeInterval:20.0 target:self selector:@selector(getProjectHoursDataFromParse) userInfo:nil repeats:YES];
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    
    [dataRefreshTimer invalidate];
}

- (void)checkUserLogin {
    PFUser *currentUser = [PFUser currentUser];
    if (currentUser == nil) {
        LoginNavigationController *loginViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"LoginNavigationController"];
        [self presentViewController:loginViewController animated:YES completion:nil];
    } else {
        [self getProjectHoursDataFromParse];
    }
}

- (void)getProjectHoursDataFromParse {
    NSLog(@"checking for data . . .");
    if ([self isInternetAvailable]) {
        PFQuery *projectHoursQuery = [PFQuery queryWithClassName:@"ProjectHours"];
        [projectHoursQuery findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
            if (error) {
                [[[UIAlertView alloc] initWithTitle:@"Database Error" message:error.description delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
            } else {
                projectHoursArray = [NSArray arrayWithArray:objects];
                [self.tableView reloadData];
            }
        }];
    }
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [projectHoursArray count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    ProjectHoursTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ProjectHoursCell" forIndexPath:indexPath];
    
    NSString *projectNameString = projectHoursArray[indexPath.row][@"projectName"];
    NSInteger hoursWorkedInteger = [projectHoursArray[indexPath.row][@"hoursWorked"] integerValue];
    BOOL projectCompleteBoolean = [projectHoursArray[indexPath.row][@"projectComplete"] boolValue];
    
    cell.nameLabel.text = projectNameString;
    cell.hoursLabel.text = [NSString stringWithFormat:@"%ld hours worked", (long)hoursWorkedInteger];
    
    if (projectCompleteBoolean) {
        cell.completedLabel.text = @"Completed";
    } else {
        cell.completedLabel.text = @"Not Completed";
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    AddProjectHoursViewController *editProjectHoursViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"AddProjectHoursViewController"];
    
    editProjectHoursViewController.editProjectHoursPFObject = projectHoursArray[indexPath.row];
    
    if ([self isInternetAvailable]) {
        [self.navigationController pushViewController:editProjectHoursViewController animated:YES];
    } else {
        [[[UIAlertView alloc] initWithTitle:@"Internet Connection Error" message:@"Please connect to the internet and try again!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
        [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
    }
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    return YES;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    [projectHoursArray[indexPath.row] deleteInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
        [self getProjectHoursDataFromParse];
    }];
}

-(BOOL)shouldPerformSegueWithIdentifier:(NSString *)identifier sender:(id)sender {
    if ([identifier isEqualToString:@"ProjectHoursDetailSegue"]) {
        if ([self isInternetAvailable] == NO) {
            [[[UIAlertView alloc] initWithTitle:@"Internet Connection Error" message:@"Please connect to the internet and try again!" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
            return false;
        }
    }
    
    return true;
}


@end
