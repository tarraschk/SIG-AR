This folder contains compiled dependencies for building the Outdoor AR Library without AndroidARLibrary project.

This folder is for those who are using the Outdoor AR Library without AndroidARLibrary project checked out.  
Those who have the AndroidARLibrary project DO NOT NEED to use the files in this folder.

[How to make Outdoor AR Library build without Android AR Library]
1. Remove Android library project reference to the Android AR Library project.
	- Right click on the "OutdoorARLibrary" project, and select "Properties"
	- From the properties dialog, choose "Android" pane from the left side.
	- From the Library list at the bottom, select the "AndroidARLibrary" project and press "Remove" button.
	- LEAVE the "Is Library" check box CHECKED, and press "OK" to close the dialog.
	
2. Copy (DO NOT MOVE) all of the files (EXCEPT FOLDERS) in this (libs_androidar) folder into the "libs" folder.

3. Copy (DO NOT MOVE) all of the files (EXCEPT FOLDERS) in libs_android/armeabi folder into the "libs/armeabi" folder.

4. Whenever you update the Outdoor AR Library project from the SVN, copy the content of this (libs_androidar) folder again into the "libs" folder to keep them up-to-date.

5. When you have to check in the code, DO NOT check in the "libs" folder.
