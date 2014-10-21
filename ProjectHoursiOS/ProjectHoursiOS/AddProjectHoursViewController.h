//
//  AddProjectHoursViewController.h
//  ProjectHoursiOS
//
//  Created by Ryan Wahle on 10/7/14.
//  Copyright (c) 2014 Ryan Wahle. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>

@interface AddProjectHoursViewController : UIViewController <UIPickerViewDataSource, UIPickerViewDelegate>

@property (weak, nonatomic) PFObject *editProjectHoursPFObject;

@end
