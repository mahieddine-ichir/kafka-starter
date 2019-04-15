
``
* bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic test-topic --property print.key=true \
    --key-deserializer org.apache.kafka.common.serialization.IntegerDeserializer
``