FROM nginx:1.19.10-alpine

COPY ./build /var/www

COPY deployment/home-control-app-init.sh /
COPY deployment/render-template.sh /
COPY deployment/templates/nginx.conf.template /
COPY deployment/templates/runtime-config.js.template /

RUN mkdir /var/www/home-control && mv /var/www/assets /var/www/home-control/assets

RUN chmod +x home-control-app-init.sh
RUN chmod +x render-template.sh

EXPOSE 14800/tcp

ENTRYPOINT ["/home-control-app-init.sh"]
CMD ["nginx","-g","daemon off;"]
