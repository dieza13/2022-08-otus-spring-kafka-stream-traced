package ru.otus.projs.contractservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;
import ru.otus.projs.common.models.TracedContractInfo;
import ru.otus.projs.tracing.deserializer.kafka.SimpleJsonDeserializer;
import ru.otus.projs.tracing.model.SimpleSerde;
import ru.otus.projs.tracing.serializer.kafka.SimpleJsonSerializer;

import java.util.function.Function;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ServiceConfig {
    private final KafkaServiceProperties props;

    @Bean
    public Serde<TracedContractInfo> tracedContractSerde(Deserializer<TracedContractInfo> contractInDeserializer,
                                                         Serializer<TracedContractInfo> contractOutSerializer) {
        SimpleSerde<TracedContractInfo> serde = new SimpleSerde<>(contractInDeserializer, contractOutSerializer);
        return serde;
    }

    @Bean
    public Deserializer<TracedContractInfo> contractInDeserializer() {
        return new SimpleJsonDeserializer<>(TracedContractInfo.class);
    }

    @Bean
    public Serializer<TracedContractInfo> contractOutSerializer() {
        return new SimpleJsonSerializer<>();
    }

    @Bean
    public Function<KStream<String, TracedContractInfo>, KStream<String, TracedContractInfo>> contractProcessor() {

        return contractStream -> {
            KTable<String, String> statusContractTable = statusMaterializedTable().apply(contractStream);
            return contractStream.leftJoin(statusContractTable,
                    (order, status) -> order,
                    Joined.with(Serdes.String(), new JsonSerde<>(), Serdes.String()))
                    .peek((k,v) -> log.info("contract {} pass to router", v.getIntegrationId()));
        };
    }

    public Function<KStream<String, TracedContractInfo>, KTable<String, String>> statusMaterializedTable() {
        return input -> input
                .peek((k,v) -> log.info("ok")
                )
                .groupBy((s, contract) -> contract.getIntegrationId(),
                        Grouped.with(new Serdes.StringSerde(), new JsonSerde<>(TracedContractInfo.class, new ObjectMapper())))
                .aggregate(String::new,(s, contract, status) -> contract.getStatus().getValue(),
                        Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as(props.getSTORE_NAME())
                                .withKeySerde(Serdes.String()).
                                withValueSerde(Serdes.String())
                );
    }

}
