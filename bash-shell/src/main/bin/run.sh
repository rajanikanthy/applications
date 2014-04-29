#!/bin/sh

# Script to start jetty server

CLASSPATH=$(echo "lib"/*.jar | tr ' ' ':')

echo "Starting server with classpath : $CLASSPATH"
#java -classpath "./conf/conf.properties:$CLASSPATH" -Dapp.id=BashShellServer com.bhn.bashshell.BashShellServer
nohup java -classpath "./conf/conf.properties:$CLASSPATH" -Dapp.id=BashShellServer com.bhn.bashshell.BashShellServer &

