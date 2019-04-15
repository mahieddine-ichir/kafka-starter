
_Créer la topic `test-topic` avec plusieurs partitions (4 par exemple)._

**`git checkout -f producer`**

1. Exécuter le `ProducerApplication`

2. Exécuter dans un shell :
```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic test-topic --property print.key=true \
    --key-deserializer org.apache.kafka.common.serialization.IntegerDeserializer
```

**`git checkout -f consumer`**

1. Exécuter le `ProducerApplication`
2. Exécuter plusieurs fois le `ConsumerApplication` et observer la sortie standard.




