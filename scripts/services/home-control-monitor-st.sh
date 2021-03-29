#!/bin/bash

# Run within script dir
cd "$(dirname "${BASH_SOURCE[0]}")"

CONTAINER_NAME=home-control-monitor-st

DATE=`date '+%Y-%m-%d %H:%M:%S'`
echo "Home control ST monitor agent starting at ${DATE}" | systemd-cat -t 'home-control-monitor-st' -p info

echo "Stopping existing service if required" | systemd-cat -t 'home-control-monitor-st' -p info
docker container rm -f ${CONTAINER_NAME} || true

VER=`cat home-control-monitor-st.version`
echo "Attempting to start services for version '$VER'" | systemd-cat -t 'home-control-monitor-st' -p info

TK=`cat home-control-monitor-st.tk`
docker run --name ${CONTAINER_NAME} -p 14230:14230 -p 14231:14231 -e INTERNAL_TOKEN=${TK} robjenks/home-control:monitor-agent-st-${VER}

RESULT=$?
if [ $RESULT -ne 0 ]; then
  echo "Failed to start agent process ($RESULT)" | systemd-cat -t 'home-control-monitor-st' -p emerg
  exit 1
fi

DATE=`date '+%Y-%m-%d %H:%M:%S'`
echo "Home control ST monitor agent terminating at ${DATE}" | systemd-cat -t 'home-control-monitor-st' -p info

