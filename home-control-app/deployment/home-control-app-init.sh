#!/bin/sh
set -eu

ENV_VARS='${AGGREGATION_SERVICE_URL}' # space-separated  '${ABC} ${DEF} ${GHI}'

# Generate nginx config
./render-template.sh "${ENV_VARS}" nginx.conf.template /etc/nginx/nginx.conf

# Generate app runtime config
./render-template.sh "${ENV_VARS}" runtime-config.js.template /var/www/runtime-config.js

exec "$@"
