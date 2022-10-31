# Poc ALZ

## discription :
1. we will retrieve the bike stations from the vilib api:([vilib station](https://api.jcdecaux.com/vls/v1/stations?apiKey=2a5d13ea313bf8dc325f8783f888de4eb96a8c14))
and we store them in a compacted KAFKA topic (a json list containing 2504 stations)
2. we will inject an incomplete message (a station with incomplete information)
3. we use kafka stream to complete the message and store it in another topic in real time
<br>

## needed tools:

1. Docker (and docker-compose)
2. java 8

## how to use :

### 1. build services locally:

 build :zookeeper kafka brocker, kafka-connect, schema-registry,ksql:
 <br> _docker-compose up -d

### 2. buid the project: 

 to build the avro classes(Station, ServiceOne)
<br> mvn clean package:

### 3. create topics:

   1. create **compacted** topic databaseCollection: used as table to store data in a kafka topic (update not insert) (it represent the collection in database):
    _kafka-topics --create --zookeeper zookeeper:2181 --topic databaseCollection --replication-factor 1 --partitions 1 --config "cleanup.policy=compact" --config min.cleanable.dirty.ratio=0.001 --config segment.ms=3000_
   2. create topic **wikichangesrequests**: random incomplete message will pushed here:
    _kafka-topics --create --zookeeper zookeeper:2181 --topic wikichangesrequests --replication-factor 1 --partitions 1_
   
   3. create to **wikichangesresponses**: to store the result,complete messagesse will finded and pushed in this message (using kafka streams)
kafka-topics --create --zookeeper zookeeper:2181 --topic wikichangesresponses --replication-factor 1 --partitions 1 
### 4. launch all main classes
<br>

## results:
all incoplete messages are completted and pushed in wikichangesresponses topic

