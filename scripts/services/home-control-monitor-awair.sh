#!/bin/bash

# Run within script dir
cd "$(dirname "${BASH_SOURCE[0]}")"

CONTAINER_NAME=home-control-monitor-awair

DATE=`date '+%Y-%m-%d %H:%M:%S'`
echo "Home control Awair monitor agent starting at ${DATE}" | systemd-cat -t 'home-control-monitor-awair' -p info

echo "Stopping existing service if required" | systemd-cat -t 'home-control-monitor-awair' -p info
docker container rm -f ${CONTAINER_NAME} || true

VER=`cat home-control-monitor-awair.version`
echo "Attempting to start services for version '$VER'" | systemd-cat -t 'home-control-monitor-awair' -p info

docker run --name ${CONTAINER_NAME} robjenks/home-control:monitor-agent-awair-${VER}

RESULT=$?
if [ $RESULT -ne 0 ]; then
  echo "Failed to start agent process ($RESULT)" | systemd-cat -t 'home-control-monitor-awair' -p emerg
  exit 1
fi

DATE=`date '+%Y-%m-%d %H:%M:%S'`
echo "Home control Awair monitor agent terminating at ${DATE}" | systemd-cat -t 'home-control-monitor-awair' -p info

