#!/bin/bash

# Run within script dir
cd "$(dirname "${BASH_SOURCE[0]}")"

CONTAINER_NAME=home-control-monitor-hue

DATE=`date '+%Y-%m-%d %H:%M:%S'`
echo "Home control Hue monitor agent starting at ${DATE}" | systemd-cat -t 'home-control-monitor-hue' -p info

echo "Stopping existing service if required" | systemd-cat -t 'home-control-monitor-hue' -p info
docker container rm -f ${CONTAINER_NAME} || true

VER=`cat home-control-monitor-hue.version`
echo "Attempting to start services for version '$VER'" | systemd-cat -t 'home-control-monitor-hue' -p info

TK=`cat home-control-monitor-hue.tk`
docker run --name ${CONTAINER_NAME} -e INTERNAL_TOKEN=${TK} robjenks/home-control:monitor-agent-hue-${VER}

RESULT=$?
if [ $RESULT -ne 0 ]; then
  echo "Failed to start agent process ($RESULT)" | systemd-cat -t 'home-control-monitor-hue' -p emerg
  exit 1
fi

DATE=`date '+%Y-%m-%d %H:%M:%S'`
echo "Home control Hue monitor agent terminating at ${DATE}" | systemd-cat -t 'home-control-monitor-hue' -p info

