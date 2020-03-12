# FitnessAdvisor

## Contribution guidelines
  1.  Clone the repo to your local machine
  2.  Create a new branch 
      `git checkout -b feature_branch_name`
  3.  Push the local branch to the remote
      `git push -u origin feature_branch_name`
  4.  Do your work on the branch
  5.  Commit and push your changes, make sure to include a description of what your changes are
      <br>a. `git pull origin master` to retrieve the latest code from master (double check that none of your code breaks after you do this)
      <br>b. `git add .` to add all files that have been modified (`git add <filename>` if you only want to add specific changes)
      <br>c. `git commit -m "Comment about what changes you have made"` 
      <br>d. `git push`
  6.  Create a pull request to merge changes to master 
  
  Don't make changes directly to master!! (changes to the readme are ok)
  Pull requests require at least one other person to approve a merge, but you can force a merge with admin privileges if really necessary.
  
  ## Android Studio tips
  - Before you run the project for the first time, make sure to clean and rebuild the project (Build > Clean Project; Build > Rebuild)
  - Some good resources for Android dev + Firebase:
     - [FirebaseAuth](https://firebase.google.com/docs/auth/android/start) for login/registration
     - [Changing to a new activity](https://developer.android.com/training/basics/firstapp/starting-activity)
     - [Built-in UI elements](https://developer.android.com/guide/topics/ui)
     - [Buttons and listeners](https://developer.android.com/reference/android/widget/Button)
     - [Getting elements from view programatically](https://developer.android.com/reference/android/view/View#findViewById(int)) - make sure you give UI elements unique ids
  - Android Studio has a built in terminal, you can run git commands from there.
  - The emulator is quite slow, so if you have an android phone, I recommend enabling delevoper mode and using USB debugging instead
  - Use [Logcat](https://developer.android.com/studio/debug/am-logcat) for debugging, generate a unique tag for you activities to help filter debugging messages ![debug](https://github.com/CS125-2020/FitnessAdvisor/blob/master/imgs/debug.png)
  
  ## Testing Firebase
  ### FirebaseAuth
   1.  Run the app, you should see a page with 2 fields and a "Register" button at the bottom.
   2.  Enter an email (for testing purposes, you can use any email with the format [text]@[text].com) and a password - the email must be a unique email that does not already exist in the database
   3.  Go to the [Firebase Console](https://console.firebase.google.com/) (log in with your uci.edu emails)
   4.  Go to "Authentication" and look for your email in the list of emails
   ![alt text](https://github.com/CS125-2020/FitnessAdvisor/blob/master/imgs/authentication_img.png)
   5. After you are done testing, you can delete the account to clean up space
   
  ### Realtime Database
   1. After completing registration, you should be redirected to a preferences page. Fill out the fields as required and click "Finish"
   2. Go to the Firebase Console and select Databases > Realtime Database
   ![alt text](https://github.com/CS125-2020/FitnessAdvisor/blob/master/imgs/database_img.png)
   3. Expand the "users" branch. Your pushed data should be listed under the user id matching the user you registered with (can be seen in the Authentication tab)
   ![alt text](https://github.com/CS125-2020/FitnessAdvisor/blob/master/imgs/database_img_2.png)
