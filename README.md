# VncThumbnailViewer
Easy way to share files from an online server.
From czechtech code, https://code.google.com/archive/p/vncthumbnailviewer/

## Features :
 - connects to multiple vnc server and mosaïc display
 - save and load a preconfigured host list
 - auto connect to servers on lan if daemon runing on vnc server

## User case (why I'm working on this) :
Someone asked me to setup a kindergarten classroom network, with a teacher desktop and 10 laptops for childrens.
Every computers are 10 years old and running a fresh install of Ubuntu 18.
The teacher's computer will be the 'master server' with VncThumbnailViewer and a Samba share.
Laptops will be vino enabled (with setup_client.sh script).

## Prerequisites :
 - Java 8
 - JDK + Maven for building

## Build :
 - ```mvn package```

## Run : 
### Visuailzer : 
```java -jar ```