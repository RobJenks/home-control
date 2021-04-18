#!/bin/bash

# Run within script dir
cd "$(dirname "${BASH_SOURCE[0]}")"

SERVICE_NAME=home-control-aggregation-ui
SERVICE_DESC="Home control aggregation UI"

DATE=`date '+%Y-%m-%d %H:%M:%S'`
echo "${SERVICE_DESC} starting at ${DATE}" | systemd-cat -t ${SERVICE_NAME} -p info

echo "Checking environment compatibility" | systemd-cat -t ${SERVICE_NAME} -p info
ENV_COMPAT=`docker-compose |& grep -e '--env-file'`
if [[ -z "$ENV_COMPAT" ]]; then
  echo "Adding environment data for compatibility mode (no --env-file)" | systemd-cat -t ${SERVICE_NAME} -p info
  cp ${SERVICE_NAME}.env .env
  ENV_CONFIG=""
else
  echo "Default environment config supported" | systemd-cat -t ${SERVICE_NAME} -p info
  ENV_CONFIG="--env-file ${SERVICE_NAME}.env"
fi

echo "Deployment configuration:" | systemd-cat -t ${SERVICE_NAME} -p info
docker-compose -p ${SERVICE_NAME} -f ${SERVICE_NAME}.yml ${ENV_CONFIG} config | systemd-cat -t ${SERVICE_NAME} -p info

echo "Stopping existing service if required" | systemd-cat -t ${SERVICE_NAME} -p info
docker-compose -p ${SERVICE_NAME} -f ${SERVICE_NAME}.yml ${ENV_CONFIG} down || true

echo "Checking for updated images" | systemd-cat -t ${SERVICE_NAME} -p info
docker-compose -p ${SERVICE_NAME} -f ${SERVICE_NAME}.yml ${ENV_CONFIG} pull

ENV_VARS=`cat ${SERVICE_NAME}.var`
if [ -z "$ENV_VARS" ]; then
  echo "No runtime environment provided" | systemd-cat -t ${SERVICE_NAME} -p emerg
  exit 1
fi

# Exports full content of ENV_VARS in [key=val ...] form, so disable warning
# shellcheck disable=SC2163
export "${ENV_VARS}"

echo "Attempting to start services" | systemd-cat -t ${SERVICE_NAME} -p info
docker-compose -p ${SERVICE_NAME} -f ${SERVICE_NAME}.yml ${ENV_CONFIG} up

RESULT=$?
if [ $RESULT -ne 0 ]; then
  echo "Failed to start process ($RESULT)" | systemd-cat -t ${SERVICE_NAME} -p emerg
  exit 1
fi

DATE=`date '+%Y-%m-%d %H:%M:%S'`
echo "${SERVICE_DESC} terminating at ${DATE}" | systemd-cat -t ${SERVICE_NAME} -p info
