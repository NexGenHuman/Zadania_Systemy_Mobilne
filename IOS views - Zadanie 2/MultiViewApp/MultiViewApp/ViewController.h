//
//  ViewController.h
//  MultiViewApp
//
//  Created by student on 11/10/2021.
//  Copyright © 2021 student. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SecondViewController.h"

@interface ViewController : UIViewController<SecondViewControllerDelegate>

@property (nonatomic, weak) IBOutlet UILabel *messageLabel;
@property (nonatomic, weak) IBOutlet UITextField *inputField;
@property (nonatomic, weak) IBOutlet UITextField *secondInputField;

-(IBAction)enter;
-(IBAction)secondEnter;

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender;

@end

