package org.example;

import com.example.ServiceOne;
import com.example.Station;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import static org.example.toolkit.constantes.getSTREAMCONFPATH;
import static org.example.toolkit.constantes.getTOPICSTATION;


public class completeMessageStream {
    public static void main(String[] args) throws IOException, InterruptedException {

        BasicConfigurator.configure();
        final Properties props = new Properties();
        props.load(Files.newInputStream(Paths.get(getSTREAMCONFPATH())));
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
//-----------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------
        final Map<String, String> serdeConfig = Collections.singletonMap("schema.registry.url", "http://localhost:8081");

        final Serde<Station> Stationserd = new SpecificAvroSerde<>();
        Stationserd.configure(serdeConfig, false); // `false` for record values

        final Serde<ServiceOne> ServiceOneserd = new SpecificAvroSerde<>();
        ServiceOneserd.configure(serdeConfig, false); // `false` for record values
//-----------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------

        StreamsBuilder builder = new StreamsBuilder();


        GlobalKTable<String, Station> db_table = builder.globalTable(getTOPICSTATION(), Consumed.with(Serdes.String(), Stationserd));
        KStream<String, ServiceOne> service_stream = builder.stream("wikichangesrequests", Consumed.with(Serdes.String(), ServiceOneserd));
        //-----------------------------------------------------------------------------------------------------------------
      KStream<String, Station> output_stream = service_stream.join(db_table, (key, value) -> key, (value1, value2) -> value2);
      output_stream.to("wikichangesresponses", Produced.with(Serdes.String(),Stationserd));
//wikichangesrequests,wikichangesresponses,

//-----------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------

        Topology topovilib = builder.build();
        KafkaStreams strem = new KafkaStreams(topovilib, props);
        strem.start();
        Thread thread = new Thread(() ->
        {
            Logger.getLogger("application closed");
            strem.close();
        });
        Runtime.getRuntime().addShutdownHook(thread);

    }

}
