# PopularMovies

An Android movie mobile app which displays a grid of movie posters. Developed as part of the Udacity Nanodegree course. The app lets the user to tap on any movie poster from the list of movie posters displayed on the Home Screen in the form of a grid.

The main screen is provided with three tabs Popular, Top-Rated and Favorites.

When the user taps on a movie poster on the Main Screen, a View Pager holding the more information such as movie details, trailers and reviews of the movie which the user tapped in the Main Screen will be displayed.

Users can also mark a movie as their favorite movie by tapping on the "Star" button Details tab of the ViewPager. This will make app to store that particular movie's information locally in the SQLite database to make that particular movie information accessibile even then the app is running offline. Once the movie marked favorite, then the button turns yellow in color to indicate that the movie is marked favorite already.

In case, if the user wants to remove a particular movie from his/her favorite movies list. Then the user can touch the "Star" button. The button will turn grey in color, and a toast message is displayed on the screen confirming the user that the movie is removed from the favorite movie list.

The app also supports Android Tablet UI. i.e. Users can also use the app on any Android Tablet device running android GINGERBREAD or above versions.

### Installation

To install the app on your device, there are three ways possible:

1.) Download the .apk file from the build directory and copy it on to your Android device and Install it.

2.) Import the project into the Android Studio IDE and hit the play button to install the app via ADB.

3.) Follow the commands to install the app via ADB using Command Prompt.

### Usage

MovieDB API key string (MovieDBApiKey) has been removed from gradle.properties file. Please insert your own key.

Include the unique key for the build by adding the following line to [USER_HOME]/.gradle/gradle.properties

### Dependencies

Below are the dependencies used in the project :

com.android.support:appcompat-v7:23.3.0

com.android.support:design:23.3.0

com.squareup.picasso:picasso:2.5.2

com.squareup.retrofit:converter-gson:2.0.0-beta2

com.squareup.okhttp3:okhttp:3.2.0

com.android.support:support-v4:23.3.0
