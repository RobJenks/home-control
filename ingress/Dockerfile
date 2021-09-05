FROM envoyproxy/envoy:1.19.1
COPY ingress-config.yml /etc/ingress-config.yml
CMD /usr/local/bin/envoy -c /etc/ingress-config.yml

