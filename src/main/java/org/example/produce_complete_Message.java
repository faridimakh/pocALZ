package org.example;

import com.example.Station;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static org.example.toolkit.RecordsVilib.geOneRecordtStation;
import static org.example.toolkit.RecordsVilib.getJSONArray;
import static org.example.toolkit.constantes.*;


public class produce_complete_Message {
    public static void main(String[] args) throws IOException, JSONException {

//        BasicConfigurator.configure();
        Properties producerProperties = new Properties();
        producerProperties.load(Files.newInputStream(Paths.get(getPRODUCERCONFPATH())));
        Producer<String, Station> producer = new KafkaProducer<String, Station>(producerProperties);
        while (true) {
            // copied from avro examples
            JSONArray getAllStations = getJSONArray(getVlibUrl());
            for (int i = 0; i < getAllStations.length(); i++) {
                Station station = geOneRecordtStation(getAllStations, i);
                ProducerRecord<String, Station> producerRecord = new ProducerRecord<>(

                        getTOPICSTATION(), station.getContractName(), station);
                producer.send(producerRecord, (metadata, exception) -> {
                    if (exception == null) {
                        System.out.println(producerRecord.key() + " -----> " + producerRecord.value());
                    } else {
                        exception.printStackTrace();
                    }
                });
            }
            ;
            producer.flush();

        }

    }
}
