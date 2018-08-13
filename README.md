# edaf55_project
Project for Concurrent Programming course using AXIS Camera, EDAF55 at LTH

The system consists of a server running on an AXIS camera and a Client running on the JVM. The server software detects motion and propagates images at high speed to the client when motion is detected. The Client connects to two separate cameras and propagates the signal to increase FPS to the second camera when the first detects motion. The Client software displays the images from the cameras and contains I/O to manually set the settings of the cameras.

The server application is written in C

The client application is written in Java

The server application uses built in functionality of the Axis camera to detect motion
