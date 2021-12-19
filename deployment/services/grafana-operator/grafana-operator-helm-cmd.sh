# Install helm from script here: https://helm.sh/docs/intro/install/
sudo helm install home-control-grafana-operator grafana/grafana-agent-operator -f grafana-operator-helm-values.yaml -n home-control --debug --kubeconfig=/etc/rancher/k3s/k3s.yaml --kube-context=home-control
