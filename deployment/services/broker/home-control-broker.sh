#!/bin/bash

SERVICE_NAME=home-control-broker

DATE=`date '+%Y-%m-%d %H:%M:%S'`
echo "Home control broker service starting at ${DATE}" | systemd-cat -t ${SERVICE_NAME} -p info

HOME_CONTROL_CONFIG=/usr/local/sbin/home-control

if [[ ! -d "$HOME_CONTROL_CONFIG" ]]; then
  echo "Failed to start home control broker services; configuration not available at '$HOME_CONTROL_CONFIG'" | systemd-cat -t ${SERVICE_NAME} -p emerg
  exit 1
fi

echo "Starting home control broker services ($HOME_CONTROL_CONFIG)" | systemd-cat -t ${SERVICE_NAME} -p info
cd $HOME_CONTROL_CONFIG
docker-compose -p ${SERVICE_NAME} -f ${SERVICE_NAME}.yml up

RESULT=$?
if [ $RESULT -ne 0 ]; then
  echo "Broker initialisation failed with error $RESULT" | systemd-cat -t ${SERVICE_NAME} -p emerg
  exit 1
fi

DATE=`date '+%Y-%m-%d %H:%M:%S'`
echo "Initialisation complete at ${DATE}" | systemd-cat -t ${SERVICE_NAME} -p info

exit 0
