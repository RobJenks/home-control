#!/bin/bash

### Installs Grafana Operator CRDs into the current kubectl namespace ###

git clone https://github.com/grafana/agent.git /tmp/tmp_graf_agent
cd /tmp/tmp_graf_agent
sudo kc apply -f production/operator/crds
rm -rf /tmp/tmp_graf_agent
