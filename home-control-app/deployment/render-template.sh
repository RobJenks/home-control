#!/bin/sh
set -eu

ENV_VARS=$1
TEMPLATE=$2
TARGET=$3

envsubst "${ENV_VARS}" < "${TEMPLATE}" > "${TARGET}"
exit $?
