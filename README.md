# README #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

CDCslots is a Native Android application to obtain latest info on available slots for driving lessons. Back-end server code is developed in Node.js to extract online data from [CDC](cdc.com.sg/Portal/login.aspx).

CDC releases new lesson slots for booking on the 15th of every month at 10 pm. If you are unable to obtain the slot of your choice, the only option is to constantly check the online portal, and hope that someone cancels their booking. 

### How do I get set up? ###

- Download the latest [Selenium Standalone Server](http://www.seleniumhq.org/download/) and start it
- Run npm install in /webservices
- Change the domain of the webservices in Android code
- Input your username and password in webservices.js

Note : CDC is a driving centre. In order to obtain a username and password, the person must head down to the centre to sign up in person. The account obtained is used to book and pay for lessons.

To view a video of the application running : https://youtu.be/EGQR9yEkLzg