package ru.otus.projs.correctingcontract.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.projs.common.models.TracedContractInfo;
import ru.otus.projs.correctingcontract.service.ContractCorrectingService;
import ru.otus.projs.tracing.deserializer.kafka.SimpleJsonDeserializer;
import ru.otus.projs.tracing.model.SimpleSerde;
import ru.otus.projs.tracing.serializer.kafka.SimpleJsonSerializer;

import java.util.function.Function;

@RequiredArgsConstructor
@Configuration
public class KafkaContractCorrectingConfiguration {

    private final ContractCorrectingService contractCorrectingService;

    @Bean
    public Function<KStream<String, TracedContractInfo>, KStream<String, TracedContractInfo>> contractCorrectingProcess() {
        return input -> input
                .peek((id, contract) -> contract.setStatus(contractCorrectingService.correctingContract(contract)))
                .map(KeyValue::new);
    }



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

}
