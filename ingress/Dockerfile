FROM envoyproxy/envoy:v1.19.1
COPY ingress-config.yaml /etc/ingress-config.yaml
CMD /usr/local/bin/envoy -c /etc/ingress-config.yaml

