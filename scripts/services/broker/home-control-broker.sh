#!/bin/bash

DATE=`date '+%Y-%m-%d %H:%M:%S'`
echo "Home control broker service starting at ${DATE}" | systemd-cat -t 'home-control' -p info

HOME_CONTROL_CONFIG=/usr/local/sbin/home-control

if [[ ! -d "$HOME_CONTROL_CONFIG" ]]; then
  echo "Failed to start home control broker services; configuration not available at '$HOME_CONTROL_CONFIG'" | systemd-cat -t 'home-control' -p emerg
  exit 1
fi

echo "Starting home control broker services ($HOME_CONTROL_CONFIG)" | systemd-cat -t 'home-control' -p info
cd $HOME_CONTROL_CONFIG
docker-compose up -d

RESULT=$?
if [ $RESULT -ne 0 ]; then
  echo "Container initialisation failed with error $RESULT" | systemd-cat -t 'home-control' -p emerg
  exit 1
fi

echo "Initialisation complete at ${DATE}" | systemd-cat -t 'home-control' -p info

exit 0
