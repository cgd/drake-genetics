#!/bin/bash
APPDIR=`dirname $0`;

# Run with debugging disabled
unset CP
for jar in $APPDIR/lib/*.jar; do
    if [ $CP ]; then
        CP="$CP:$jar"
    else
        CP=$jar
    fi
done

# run with GWT server
java -Xmx256M -cp "$APPDIR/src/java:$APPDIR/build/java:$CP" com.google.gwt.dev.DevMode \
    -gen $APPDIR/build/gen -war $APPDIR/build/www org.jax.drakegenetics.gwtclientapp.DrakeGeneticsApplication

################################################################################
# NOTE:
# ignore everything below this comment. We're just keeping it around for
# reference so that we can reuse it if needed
################################################################################

# run with external server
#java -Xmx256M -cp "$APPDIR/src/java:$APPDIR/build/java:$CP" com.google.gwt.dev.DevMode -noserver -gen $APPDIR/build/gen -war $APPDIR/build/war -startupUrl http://localhost:8080/$1 org.jax.pubarray.gwtqueryapp.QueryApplication

# OLD SCRIPT:
#java -XstartOnFirstThread -Xmx256M -cp "$APPDIR/src/java:$APPDIR/build/java:$APPDIR/../../../third-party/gwt-mac-1.6.4/gwt-user.jar:$APPDIR/../../../third-party/gwt-mac-1.6.4/gwt-dev-mac.jar" com.google.gwt.dev.HostedMode -noserver -gen $APPDIR/build/gen -war $APPDIR/build/war -startupUrl http://localhost:8080/$1 org.jax.pubarray.gwtqueryapp.QueryApplication

# Run with debugging enabled
#java -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y -XstartOnFirstThread -Xmx256M -cp "$APPDIR/src/java:/Users/kss/projects/third-party/gwt-mac-1.5.3/gwt-user.jar:/Users/kss/projects/third-party/gwt-mac-1.5.3/gwt-dev-mac.jar" com.google.gwt.dev.GWTShell -noserver http://localhost:8080/haplotype-gwt-1.0/org.jax.mousemap.converterapp.MouseMapConverterApplication/MouseMapConverterApplication.html
