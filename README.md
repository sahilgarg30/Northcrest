# NorthCrest
*CATEGORY 1 OF THE CODEFEST'17 APPATHON*
*NAME OF THE APP: NorthCrest*

*INTRODUCTION*

*North Crest is an app provided for North Crest bank customers where they can view all their bank activitites such as their current balance, profile info, 
transaction history and so on.They can also make easy transactions via the app.
*It is a user friendly environment that has normal email/google authentication and thus provides access to multiple accounts on one device.

*Users can view their profile information, current bank balance, make transactions(debit/credit), receive and redeem their rewards, view transaction history, 
change their basic information, find information about North Crest on the support page all on one platform.

*The app provides a multi-user support system, such that multiple users can access their information/details on the same device.
*This app guarantees a smooth, error free, easy flowing and a hassle-free experience for it's users.

*HOW TO USE!!*

1)Log in/Sign Up: Users are required to sign up either via google account or they can make a new account using their email id. If the user has already signed up 
		  before, then he/she can log in with their email id or again use google sign in.
2)Enter basic details: After sign up, users are required to enter their basic details such as name, date of birth, account number, picture,etc. This step is skipped if user has
			already signed in before.
3)My Profile fragment opens: That's it! Now the user has logged in and is inside the app. The first page seen by the user displays all their basic information
			      feeded in the app by them.
4)Navigate around the app: The navigation drawer consists of the following options:
			a)My Profile: First page that opens. Users view their profile here.
			b)Balance: Current bank balance can be viewed here. It is automatically updated on any transaction/rewards activity.
			c)History: View your transaction history here in a list. The person you have transacted with, amount that is debited or credited gets automatically 
				   updated here. *NOTE*:Users can clear all their transactions by pressing the Delete transaction button present in the menu on the
				   top right corner of the screen.
			d)Rewards: The rewards (in equivalents of dollars) of each user gained during transactions is displayed here along with an explanation on how 
				   the rewards system of this bank works. On clicking GET REWARDS the rewards are added to your balance and made 0. On each payment made, 
				   1% of the amount is added  as rewards and if the payment is made to "NorthCrest Stores", 1.25% of the total payment is added to the rewards.
			e)Support: It is a help page for the users with contact information of the bank. Users can make direct calls/ emails from this fragment and
				   they can also open the bank's official website in their browser directly from this page.
			f)Sign Out: Signs you out of the app. User may log in from another account or from the same account once again.
5)Make Transactions: A floating action button takes you to another activity where the users can make transactions. They are required to input the name of the person
		     they are transacting with along with the amount of money which can be debitted/creditted from/to their account depending on their choice of radio 
		     button. On clicking ADD TRANSACTION the balance, rewards, transaction history, everything is updated accordingly. Future versions of the app
		     will include a payment gateway such as razorpay integrated in it.
6)Menu at the top right corner: This menu has two options:  Settings, Delete Transactions
				*Settings lets you edit all your bank and profile information in one page. Profile picture can be selected either from gallery or 
				captured from camera. Users have a choice to leave it blank as well. They can edit their balance(if they wish to do so) and also their
				profile information such as name/phone/address etc.
				*Users can clear all their transactions by pressing the Delete transaction button present in the menu on the
				top right corner of the screen


*MORE INFORMATION ON HOW THE APP FUNCTIONS*

1)Authentication: Firebase authentication implemented. Login via email/google account is allowed. Users have to enter their details only on their first login and do
		  do not require to do so on second login onwards on each device. This is handled by shared preferences(stored in local memory of the device).
2)Shared Preferences(explained): Controls current state and basic flow of the app along with holding authentication details.
3)Database: All the users' details are stored in the device's local database and accessed/updated as and when required for the app's functionalities. There is a 
	    separate table in the database that stores the transactions of every user.
4)Cursor Adapter: Cursor adapter implemented along with list view in order to view and edit transaction history.
5)Navigation Drawer: Works with fragments. Each fragment is added to a single activity. The header of the navigation drawer displays bank name, bank logo, user's
		     email id with which he/she has logged in.
6)Intents: Implicit intents used for Camera/gallery, for phone calls, for email, for opening web browser.
7)Material Design: App follows the material design guidelines strictly and has easy to use UI with appealing looks. 
