#!/usr/bin/env bash

CMD=$1
NOHUP=${NOHUP:=$(which nohup)}
PS=${PS:=$(which ps)}

# default java
JAVA_CMD=${JAVA_CMD:=$(which java)}

get_pid() {
    cat "${SECURITY_PID}" 2> /dev/null
}

pid_running() {
    kill -0 $1 2> /dev/null
}

die() {
    echo $*
    exit 1
}

if [ -n "$JAVA_HOME" ]
then
    # try to use $JAVA_HOME
    if [ -x "$JAVA_HOME"/bin/java ]
    then
        JAVA_CMD="$JAVA_HOME"/bin/java
    else
        die "$JAVA_HOME"/bin/java is not executable
    fi
fi

# resolve links - $0 may be a softlink
#ALERTMANAGERCTL="$0"

#while [ -h "$ALERTMANAGERCTL" ]; do
#    ls=$(ls -ld "$ALERTMANAGERCTL")
#    link=$(expr "$ls" : '.*-> \(.*\)$')
#    if expr "$link" : '/.*' > /dev/null; then
#        ALERTMANAGERCTL="$link"
#    else
#        ALERTMANAGERCTL=$(dirname "$ALERTMANAGERCTL")/"$link"
#   fi
#done

# take variables from environment if set
#ALERTMANAGERCTL_DIR=${ALERTMANAGERCTL_DIR:=$(dirname "$ALERTMANAGERCTL")}
SERVICE_JAR=${SERVICE_JAR:=target/security-3.0.0-SNAPSHOT-exec.jar}
#ALERTMANAGER_CONF=${ALERTMANAGER_CONF:=/opt/alertmanager/server.conf}
SECURITY_PID=${SECURITY_PID:=/opt/alertmanager/alertmanager.pid}
LOG_FILE=${LOG_FILE:=console.log}
#LOG4J=${LOG4J:=}
DEFAULT_JAVA_OPTS="--SERVER_PORT=8094 --PSQL_HOST=localhost --PSQL_PORT=5432 --PSQL_DB=security --PSQL_PSWD=postgres -Djdk.tls.acknowledgeCloseNotify=true -Xms1g -Xmx1g -XX:NewRatio=1 -XX:+ResizeTLAB -XX:+UseConcMarkSweepGC -XX:+CMSConcurrentMTEnabled -XX:+CMSClassUnloadingEnabled -XX:-OmitStackTraceInFastThrow"

if $JAVA_CMD -XX:+PrintFlagsFinal 2>&1 |grep -q UseParNewGC; then
	DEFAULT_JAVA_OPTS="${DEFAULT_JAVA_OPTS} -XX:+UseParNewGC"
fi

JAVA_OPTS="${JAVA_OPTS:="$DEFAULT_JAVA_OPTS"}"

start() {
    echo "Starting security service ..."
#    cd "$ALERTMANAGERCTL_DIR/.."
    "${NOHUP}" "${JAVA_CMD}" -jar "${SERVICE_JAR}" ${JAVA_OPTS} -p "${SECURITY_PID}" >> "${LOG_FILE}" 2>> "${LOG_FILE}" &
}

run() {
    echo "Running security service ..."
#   cd "$ALERTMANAGERCTL_DIR/.."
    exec "${JAVA_CMD}" -jar "${SERVICE_JAR}" ${JAVA_OPTS} -p "${SECURITY_PID}"
}

stop() {
    if [ ! -f "${SECURITY_PID}" ]; then
      die "Not stopping. PID file not found: ${SECURITY_PID}"
    fi

    PID=$(get_pid)

    echo "Stopping security service ($PID) ..."
    echo "Waiting for security service to halt."

    kill $PID

    while "$PS" -p $PID > /dev/null; do sleep 1; done;
    rm -f "${SECURITY_PID}"

    echo "security service stopped"
}

restart() {
    echo "Restarting security service ..."
    stop
    start
}

status() {
    PID=$(get_pid)
    if [ ! -z $PID ]; then
        if pid_running $PID; then
            echo "security service running with PID ${PID}"
            return 0
        else
            rm "${SECURITY_PID}"
            die "Removed stale PID file ${SECURITY_PID} with ${PID}."
        fi
    fi

    die "security service not running"
}

case "$CMD" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    status)
        status
        ;;
    run)
        run
        ;;
    *)
        echo "Usage $0 {start|stop|restart|status|run}"
esac




#java -jar target/security-3.0.0-SNAPSHOT-exec.jar --SERVER_PORT=8094 --PSQL_HOST=localhost --PSQL_PORT=5432 --PSQL_DB=security --PSQL_PSWD=postgres #> console.log &
