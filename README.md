
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


**`git checkout -f compression`**

1. Supprimer les dossier `/tmp/zookeeper`

2. Lancer `ProducerApplication`

3. observer la taille des fichier de log dans
```
    ls -l /tmp/kafka-logs/test-topic-*/*.log
```

**`git checkout -f stream`**

1. Lancer `StreamApplication`

2. Copier la sortie console sous _Topolgies_ et copier dans _https://zz85.github.io/kafka-streams-viz/_

**`git checkout -f stream-join`**

1. Lancer `ReferentielProducerApplication` et `ProducerApplication`

2. Lancer `StreamApplication` et visualiser la _Topolgie_ (_https://zz85.github.io/kafka-streams-viz/_)

3. Observer les messages dans la topic _output_

4. remplacer le `leftJoin` dans le stream par un `join` ... quelle est la différence ?

**`git checkout -f stream-aggregate`**

1. Ecrire un stream qui permet d'avoir la somme des enveloppes par état. 
