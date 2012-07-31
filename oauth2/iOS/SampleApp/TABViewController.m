//
//  TABViewController.m
//  SampleApp
//
//  Copyright (c) 2012年 Tonchidot Corp. All rights reserved.
//

#import "TABViewController.h"

#import "TABViewController.h"
#import "GTMOAuth2ViewControllerTouch.h"
#import "GTMOAuth2SignIn.h"
#import "JSON.h"

/* 環境に合わせて設定 */
static NSString *const kTabClientId        = @"";
static NSString *const kTabClientSecret    = @"";
static NSString *const kCallbackUrl        = @"";

static NSString *const kKeychainItemName   = @"OAuth Sample";

static NSString *const kOauth2TokenApi     = @"https://tab.do/api/1/oauth2/token";
static NSString *const kAuthorizeOauth2Api = @"https://tab.do/oauth2/authorize";
static NSString *const kUsersMeApi         = @"http://tab.do/api/1/users/me.json";

@interface TABViewController ()
@property (retain, nonatomic) IBOutlet UIImageView *profileImageView;
@property (retain, nonatomic) IBOutlet UILabel *screenNameLabel;
@property (retain, nonatomic) IBOutlet UILabel *emailLabel;
@property (retain, nonatomic) IBOutlet UIButton *authTabButton;
@property (retain, nonatomic) GTMOAuth2Authentication *auth;
@end

@implementation TABViewController
@synthesize profileImageView = _profileImageView;
@synthesize emailLabel = _emailLabel;
@synthesize authTabButton = _authTabButton;
@synthesize screenNameLabel = _screenNameLabel;
@synthesize auth = _auth;

- (void)viewDidLoad
{
    [super viewDidLoad];
    _profileImageView.hidden = YES;
    _screenNameLabel.hidden = YES;
    _emailLabel.hidden = YES;
}

- (void)viewDidUnload
{
    [self setScreenNameLabel:nil];
    [self setEmailLabel:nil];
    [self setProfileImageView:nil];
    [self setAuthTabButton:nil];
    [super viewDidUnload];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}

- (IBAction)authTab:(UIButton *)sender {
    [self signInToTab];
}

- (GTMOAuth2Authentication *)authForTab {
    NSURL *tokenURL = [NSURL URLWithString:kOauth2TokenApi];
    GTMOAuth2Authentication *auth = [GTMOAuth2Authentication authenticationWithServiceProvider:@"Custom Service"
                                                                                      tokenURL:tokenURL
                                                                                   redirectURI:kCallbackUrl
                                                                                      clientID:kTabClientId
                                                                                  clientSecret:kTabClientSecret];
    return auth;
}

- (void)signInToTab {
    GTMOAuth2Authentication *auth = [self authForTab];
    auth.scope = @"all";
    NSURL *authURL = [NSURL URLWithString:kAuthorizeOauth2Api];
    
    GTMOAuth2ViewControllerTouch *viewController;
    viewController = [[[GTMOAuth2ViewControllerTouch alloc] initWithAuthentication:auth
                                                                  authorizationURL:authURL
                                                                  keychainItemName:kKeychainItemName
                                                                          delegate:self
                                                                  finishedSelector:@selector(viewController:finishedWithAuth:error:)] autorelease];
    [[self navigationController] pushViewController:viewController animated:YES];
}

- (void)viewController:(GTMOAuth2ViewControllerTouch *)viewController finishedWithAuth:(GTMOAuth2Authentication *)auth error:(NSError *)error {
    if (error != nil) {
        // Sign-in failed
        NSLog(@"Authentication error: %@", error);
    } else {
        // Sign-in succeeded
        self.auth = auth;
        [self getUserInfo];
    }
}

/* tabのユーザ情報を取得して表示 */
- (void)getUserInfo {
    _authTabButton.hidden = YES;
    NSURL *url = [NSURL URLWithString:kUsersMeApi];
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    GTMHTTPFetcher *fetcher = [GTMHTTPFetcher fetcherWithRequest:request];
    self.auth.shouldAuthorizeAllRequests = YES;
    [fetcher setAuthorizer:self.auth];
    [fetcher beginFetchWithCompletionHandler:^(NSData *data, NSError *error) {
        if (error != nil) {
            NSLog(@"%@", [error description]);
            _authTabButton.hidden = NO;
        } else {
            NSString *output = [[[NSString alloc] initWithData:data
                                                      encoding:NSUTF8StringEncoding] autorelease];
            NSDictionary *user = [[output JSONValue] objectForKey:@"user"];

            /* 表示部 */
            _screenNameLabel.text = [user objectForKey:@"screen_name"];
            _emailLabel.text = [user objectForKey:@"email"];

            NSData *dt = [NSData dataWithContentsOfURL:[NSURL URLWithString:[[user objectForKey:@"profile_image_url"] objectForKey:@"crop_M1"]]];
            _profileImageView.image = [[UIImage alloc] initWithData:dt];
            _screenNameLabel.hidden = NO;
            _emailLabel.hidden = NO;
            _profileImageView.hidden = NO;
            _authTabButton.hidden = YES;
        }
    }];
}

- (void)dealloc {
    [_screenNameLabel release];
    [_emailLabel release];
    [_profileImageView release];
    [_authTabButton release];
    [_auth release];
    [super dealloc];
}
@end
