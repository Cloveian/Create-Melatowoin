#!/bin/sh
#
# Gradle start up script for POSIX systems (Linux, macOS)
#

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Resolve links
PRG="$0"
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

# Auto-detect Java 17 if JAVA_HOME is not already pointing at a Java 17+ JDK.
# This lets the Gradle daemon run on Java 17 even when the system default is newer.
_current_major() { "$1/bin/java" -version 2>&1 | sed -n 's/.*version "\([0-9]*\).*/\1/p' | head -1; }
if [ -z "$JAVA_HOME" ] || [ "$(_current_major "$JAVA_HOME")" != "17" ]; then
    for _dir in /usr/lib/jvm/*/; do
        _release="$_dir/release"
        if [ -f "$_release" ] && grep -q 'JAVA_VERSION="17' "$_release" && [ -x "$_dir/bin/java" ]; then
            JAVA_HOME="$_dir"
            break
        fi
    done
fi

# Determine the Java command to use
if [ -n "$JAVA_HOME" ] ; then
    JAVACMD="$JAVA_HOME/bin/java"
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME"
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
fi

exec "$JAVACMD" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
