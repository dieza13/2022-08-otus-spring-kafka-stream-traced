package ru.otus.projs.confirmedcontract.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.projs.common.models.TracedContractInfo;
import ru.otus.projs.confirmedcontract.service.ContractConfirmedService;
import ru.otus.projs.tracing.deserializer.kafka.SimpleJsonDeserializer;
import ru.otus.projs.tracing.model.SimpleSerde;
import ru.otus.projs.tracing.serializer.kafka.SimpleJsonSerializer;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Configuration
public class KafkaContractConfirmedConfiguration {

    private final ContractConfirmedService contractConfirmedService;

    @Bean
    public Consumer<KStream<String, TracedContractInfo>> contractOnConfirmedProcess() {
        return input -> input
                .foreach((id, contract) -> contractConfirmedService.onConfirmedContract(contract));
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
