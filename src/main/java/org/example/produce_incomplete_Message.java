package org.example;

import com.example.ServiceOne;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Random;

import static org.example.toolkit.RecordsVilib.geOneRecordtServiceone;
import static org.example.toolkit.RecordsVilib.getJSONArray;
import static org.example.toolkit.constantes.getPRODUCERCONFPATH;
import static org.example.toolkit.constantes.getVlibUrl;


public class produce_incomplete_Message {
    public static void main(String[] args) throws IOException, JSONException, InterruptedException {

//        BasicConfigurator.configure();
        Properties producerProperties = new Properties();
        producerProperties.load(Files.newInputStream(Paths.get(getPRODUCERCONFPATH())));
        Producer<String, ServiceOne> producer = new KafkaProducer<String, ServiceOne>(producerProperties);
        while (true) {
            JSONArray getAllStations = getJSONArray(getVlibUrl());
            Integer i = new Random().nextInt(2500) + 1;
            ServiceOne serviceone = geOneRecordtServiceone(getAllStations, i);
            ProducerRecord<String, ServiceOne> producerRecord = new ProducerRecord<>(

                    "wikichangesrequests", serviceone.getContractName(), serviceone);
            producer.send(producerRecord, (metadata, exception) -> {
                if (exception == null) {
                    System.out.println(producerRecord.key() + " -----> " + producerRecord.value());
                } else {
                    exception.printStackTrace();
                }
            });
            producer.flush();
            Thread.sleep(1000);

        }
    }
}