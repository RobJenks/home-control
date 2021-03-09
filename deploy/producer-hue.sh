#!/bin/sh

docker run -it --net=host edenhill/kafkacat:1.6.0 -P -b localhost:29092 -t status-event-topic -K: -H "__TypeId__=org.rj.homectl.status.hue.HueStatusEvent"

