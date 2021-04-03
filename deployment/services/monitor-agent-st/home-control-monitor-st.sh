#!/bin/bash

# Run within script dir
cd "$(dirname "${BASH_SOURCE[0]}")"

SERVICE_NAME=home-control-monitor-st

DATE=`date '+%Y-%m-%d %H:%M:%S'`
echo "Home control ST monitor agent starting at ${DATE}" | systemd-cat -t ${SERVICE_NAME} -p info

echo "Stopping existing service if required" | systemd-cat -t ${SERVICE_NAME} -p info
docker-compose -p ${SERVICE_NAME} -f ${SERVICE_NAME}.yml --env-file ${SERVICE_NAME}.env down || true

TK=`cat ${SERVICE_NAME}.tk`
if [ -z "$TK" ]; then
  echo "No authentication token provided" | systemd-cat -t ${SERVICE_NAME} -p emerg
  exit 1
fi

echo "Attempting to start services" | systemd-cat -t ${SERVICE_NAME} -p info
INTERNAL_TOKEN="${TK}" docker-compose -p ${SERVICE_NAME} -f ${SERVICE_NAME}.yml --env-file ${SERVICE_NAME}.env up

RESULT=$?
if [ $RESULT -ne 0 ]; then
  echo "Failed to start agent process ($RESULT)" | systemd-cat -t ${SERVICE_NAME} -p emerg
  exit 1
fi

DATE=`date '+%Y-%m-%d %H:%M:%S'`
echo "Home control ST monitor agent terminating at ${DATE}" | systemd-cat -t ${SERVICE_NAME} -p info
