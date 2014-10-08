//
//  ProjectHoursTableViewCell.h
//  ProjectHoursiOS
//
//  Created by Ryan Wahle on 10/7/14.
//  Copyright (c) 2014 Ryan Wahle. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ProjectHoursTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *hoursLabel;
@property (weak, nonatomic) IBOutlet UILabel *completedLabel;

@end
