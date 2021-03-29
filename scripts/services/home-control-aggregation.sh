#!/bin/bash

# Run within script dir
cd "$(dirname "${BASH_SOURCE[0]}")"

CONTAINER_NAME=home-control-aggregation

DATE=`date '+%Y-%m-%d %H:%M:%S'`
echo "Home control aggregation service starting at ${DATE}" | systemd-cat -t 'home-control-aggregation' -p info

echo "Stopping existing service if required" | systemd-cat -t 'home-control-aggregation' -p info
docker container rm -f ${CONTAINER_NAME} || true

VER=`cat home-control-aggregation.version`
echo "Attempting to start services for version '$VER'" | systemd-cat -t 'home-control-aggregation' -p info

docker run --name ${CONTAINER_NAME} -p 14200:14200 -p 14201:14201 robjenks/home-control:aggregation-${VER}

RESULT=$?
if [ $RESULT -ne 0 ]; then
  echo "Failed to start agent process ($RESULT)" | systemd-cat -t 'home-control-aggregation' -p emerg
  exit 1
fi

DATE=`date '+%Y-%m-%d %H:%M:%S'`
echo "Home control aggregation service terminating at ${DATE}" | systemd-cat -t 'home-control-aggregation' -p info

