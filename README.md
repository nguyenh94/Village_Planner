# Village_Planner

Welcome to use Village Planner, a dining-plan app made for Trojans at USC Village.
Here are the steps to run Village Planner on Android Studio

1. Please use Android Studio Chipmunk (or above) to safely run the project
2. For the AVD version, please choose Pixel 2 with Android 7.0 (API Level 24, Nougat), please make sure you choose the target "Android 7.0 (Google APIs)" If you don't choose "Google APIs" the Google Map functionality would not work.
3. If prompted, please sync the Gradle build file so you get the most up-to-date dependencies
4. Before you run, please change your AVD location to somewhere at LA. If Village Planner detects you are not at LA, your service will be very limited. You can change your Emulator location according to this post: https://stackoverflow.com/a/47530560/11079192
For your convenience, you can choose latitude 34.0211506325, longitude -118.288850784, it's somewhere on the main campus, and now you are ready to grab something to eat at USC Village!

Here are some guidance to try our full functionalities:
A friendly advice: AVD Emulator is not very stable per se. During development, we often encountered scenarios where the same code, running first time would cause some error (like network not connected, firebase not connected, GPS not located, etc.) But when you re-run the app or restart the Emulator, everything works fine again. We are confident that our Village Planner fulfills all of the user requirements, so if you encountered some unexpected behaviors for the first time, please re-run the app. We would love to hear your feedback so we can make Village Planner better for the next iteration.

1. Upon launching, you will be prompted to grant us location premission. Please choose "allow" otherwise most of the Village Planner services would not work.
2. Re-run the app again, now you can try basic login/register functionality. When you add your photo in the registration page, please choose the source "photo", not gallary. Upon serious testing we find no photo will be added to the gallary.
3. After login, you will see "My Village" panel which shows all the restaurants on the map as annotations. You can click on one annotation and the restaurant name would pop up, say "CAVA."
4. Click on the pop-up "CAVA" and you will be redirected to the restaurant detail page. Here you will see the estimated queue time, and you can choose your mode of transportation so Village Planner can estimate your travel time.
5. You could either click 
    Get Direction: you will be redirected to the map page with the routing line corresponding to your travel mode.
    Set Reminder: you will be redirected to the reminder creator page to set up your reminder title, and your desired arrival time.
6. You can see all your reminders in the Reminders tab. Switching tabs will refresh the content automatically. You can change your reminder title and arrival time in this page as well. Please use the correct timing format like "11:45" or "18:50" when you change the arrival time.
7. You can see your account information in the Me tab. Here you can view your profile information like email, name, and your photo. You can change your photo by clicking your photo view.

Enjoy Village Planner and Fight On!
 
