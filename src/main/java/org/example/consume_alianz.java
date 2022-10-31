package org.example;

import com.example.Station;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

import static org.example.toolkit.constantes.getCONSUMERCONFPATH;
import static org.example.toolkit.constantes.getTOPICSTATION;

public class consume_alianz {

    public static void main(String[] args) throws IOException {

        Properties consumerProperties = new Properties();
        consumerProperties.load(Files.newInputStream(Paths.get(getCONSUMERCONFPATH())));
        consumerProperties.load(Files.newInputStream(Paths.get(getCONSUMERCONFPATH())));

        try (KafkaConsumer<String, Station> kafkaConsumer = new KafkaConsumer<>(consumerProperties)) {
            String topic = getTOPICSTATION();
//            kafkaConsumer.subscribe(Collections.singleton(topic));
//            TopicPartition partition1 = new TopicPartition(topic, 0);
//            kafkaConsumer.assign(Arrays.asList(partition1));
            kafkaConsumer.subscribe(new ArrayList<>(Collections.singleton("wikichangesresponses")));
            System.out.println("Waiting for data...");

            while (true) {
                System.out.println("Polling");
                ConsumerRecords<String, Station> records = kafkaConsumer.poll(1000);

                for (ConsumerRecord<String, Station> record : records) {

                    System.out.println(record.value().getContractName()+ "--------," +record.value());
                }

                kafkaConsumer.commitSync();
            }
        }
    }
}
