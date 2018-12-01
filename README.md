# JRedrabbit

JAVA_HOME="/usr/lib/jvm/java-1.8.0-openjdk-amd64/bin"
CATALINA="/opt/tomcat"
CLASSPATH=$CATALINA/lib/gson-2.8.5.jar:$CATALINA/lib/servlet-api.jar:$CATALINA/lib/red-rabbit-1.0.jar:$CATALINA/lib/mysql-connector-java-8.0.13.jar:$CLASSPATH

PATH=$PATH:$JAVA_HOME:$CLASSPATH:$CATALINA
export JAVA_HOME
export CATALINA
export CLASSPATH
