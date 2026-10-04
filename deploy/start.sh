#!/bin/sh
# Cloud platforms (Render, Railway, Fly...) tell us which port to listen on via $PORT.
sed -i "s/Connector port=\"8080\"/Connector port=\"${PORT:-8080}\"/" /usr/local/tomcat/conf/server.xml
exec catalina.sh run
