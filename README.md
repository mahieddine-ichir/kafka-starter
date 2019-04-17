
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

**`git checkout -f connect-jdbc`**

1. Lancer 
```
sqlite3 test-connect.db
```
et créer le schéma
```
CREATE TABLE envelope_labels (id INTEGER PRIMARY KEY Autoincrement NOT NULL, name varchar(32), label varchar(255));
```

Insérer quelques lignes, par exemple :

```
insert into envelope_labels (name, label) values ('ENVOYEE', 'Courrier envoyé');
insert into envelope_labels (name, label) values ('NPAI', 'Addresse inconnue');
```

2. Lancer 
```
sqlite3 test-connect-output.db
```
et créer le schéma
```
CREATE TABLE counts (id INTEGER PRIMARY KEY Autoincrement NOT NULL, name varchar(32), count integer);
```

3. Lancer, _kafka-connect_ 

```
bin/confluent start connect
``` 

et installer les deux connecteurs (sink et source)

```
curl -vX POST localhost:8083/connectors -H "Content-Type: application/json" -d @jdbc-sink.json
```

```
curl -vX POST localhost:8083/connectors -H "Content-Type: application/json" -d @jdbc-source.json
```

4. Démarrer le `ProducerApplication` et `StreamApplication`

5. Observer les topics `counts`, `output`, `nvelope_labels`, `test-sqlite-jdbc-envelope_labels`

6. Regarder le contenu de la table `counts` dans la base `test-connect-output.db`

