FROM nginx:1.19.10-alpine

COPY ./build /var/www

COPY deployment/aggregation-ui-init.sh /
COPY deployment/render-template.sh /
COPY deployment/templates/nginx.conf.template /
COPY deployment/templates/runtime-config.js.template /

RUN chmod +x aggregation-ui-init.sh
RUN chmod +x render-template.sh

EXPOSE 14800/tcp

ENTRYPOINT ["/aggregation-ui-init.sh"]
CMD ["nginx","-g","daemon off;"]
