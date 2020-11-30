echo "Initialising brokers"
docker-compose up -d
sleep 5

echo "Creating topics"
docker-compose exec broker kafka-topics --create --topic status-event-topic --bootstrap-server broker:9092 --replication-factor 1 --partitions 1

echo "Deployment complete"

