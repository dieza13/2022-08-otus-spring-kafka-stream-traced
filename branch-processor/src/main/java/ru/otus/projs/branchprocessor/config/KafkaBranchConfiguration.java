package ru.otus.projs.branchprocessor.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.projs.branchprocessor.service.BranchingContractService;
import ru.otus.projs.common.models.ContractStatus;
import ru.otus.projs.common.models.TracedContractInfo;
import ru.otus.projs.tracing.deserializer.kafka.SimpleJsonDeserializer;
import ru.otus.projs.tracing.model.SimpleSerde;
import ru.otus.projs.tracing.serializer.kafka.SimpleJsonSerializer;

import java.util.Map;
import java.util.function.Function;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaBranchConfiguration {

    private final BranchingContractService branchingContractService;

    @Bean
    @SuppressWarnings("unchecked")
    public Function<KStream<String, TracedContractInfo>, KStream<String, TracedContractInfo>[]> branchProcess() {
        return input -> input
                .peek((k, v) -> log.info("Branching contract with id {} and status {}", v.getIntegrationId(), v.getStatus())
                )
                .split()
                .branch((k, v) -> branchingContractService.branchProcessing(ContractStatus.PENDING, v))
                .branch((k, v) -> branchingContractService.branchProcessing(ContractStatus.INCORRECT, v))
                .branch((k, v) -> branchingContractService.branchProcessing(ContractStatus.CHECKED, v))
                .branch((k, v) -> branchingContractService.branchProcessing(ContractStatus.CONFIRMED, v))
                .branch((k, v) -> branchingContractService.branchProcessing(ContractStatus.CANCELED, v))
                .noDefaultBranch()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue).toList()
                .toArray(new KStream[0])
                ;
    }

    @Bean
    public Serde<ru.otus.projs.common.models.TracedContractInfo> tracedContractSerde(Deserializer<ru.otus.projs.common.models.TracedContractInfo> contractInDeserializer,
                                                                                     Serializer<ru.otus.projs.common.models.TracedContractInfo> contractOutSerializer) {
        SimpleSerde<ru.otus.projs.common.models.TracedContractInfo> serde = new SimpleSerde<>(contractInDeserializer, contractOutSerializer);
        return serde;
    }

    @Bean
    public Deserializer<ru.otus.projs.common.models.TracedContractInfo> contractInDeserializer() {
        return new SimpleJsonDeserializer<>(ru.otus.projs.common.models.TracedContractInfo.class);
    }

    @Bean
    public Serializer<ru.otus.projs.common.models.TracedContractInfo> contractOutSerializer() {
        return new SimpleJsonSerializer<>();
    }

}
