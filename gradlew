#!/usr/bin/env sh
##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

DEFAULT_JVM_OPTS=""

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> $begin:math:text$.*$end:math:text$$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

PRGDIR=`dirname "$PRG"`

[ -z "$APP_HOME" ] && APP_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

APP_HOME=`cd "$APP_HOME" && pwd`

if [ -z "$JAVA_HOME" ]; then
  JAVACMD=java
else
  JAVACMD="$JAVA_HOME/bin/java"
fi

if [ -z "$GRADLE_OPTS" ]; then
  GRADLE_OPTS="$DEFAULT_JVM_OPTS"
fi

exec "$JAVACMD" $GRADLE_OPTS -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
