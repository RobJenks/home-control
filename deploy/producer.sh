echo "Starting console producer"
docker-compose exec broker kafka-console-producer --topic status-event-topic --broker-list broker:9092 --property parse.key=true --property key.separator=":"

