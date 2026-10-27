#!/bin/bash

/usr/local/java/apache-tomcat-9.0.73/bin/startup.sh
/usr/sbin/sshd -D
tail -f  /dev/null
