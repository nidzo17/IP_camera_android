# IP_Camera_android
Android application for surveillance with rotating IP camera with motion detection algorithm

Mobile application for surveillance with Vivotek PZ7131 IP camera. For developing the
application I used Android SDK with Eclipse IDE. Firstly, I had to set up router and the IP
camera to assign static IP address. With static IP address we can send commands directly to IP
camera. CGI commands are used for rotating IP camera, for video and audio streaming and
command for catching frames of IP camera. The application is able to detect motions because
there is a function that compares successive frames. The function for comparing frames executes
as asynchronous task. If motion is detected, the application sends alarm to the user's interface.
Tested on: LG Nexus 4
