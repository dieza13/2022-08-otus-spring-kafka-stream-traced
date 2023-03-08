package ru.otus.projs.contractservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.projs.common.models.Contract;
import ru.otus.projs.common.models.ContractStatus;
import ru.otus.projs.common.models.TracedContractInfo;
import ru.otus.projs.contractservice.config.KafkaServiceProperties;

import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaContractService implements ContractService {

    private final UniqueIdGenerator idGenerator;
    private final KafkaTemplate kafkaTemplate;

    private final KafkaServiceProperties props;

    private final InteractiveQueryService interactiveQueryService;

    @Override
    public Function<Contract, Contract> pushContract() {

        return input->{
            String integrationId = idGenerator.generateId();
            TracedContractInfo contract = TracedContractInfo.builder()
                    .integrationId(integrationId)
                    .contractNumber(input.getContractNumber())
                    .status(ContractStatus.PENDING)
                    .build();
            String topicName = props.getProcessingTopic();
            log.info("Push contract with integrationId {} to processing stream", contract.getIntegrationId());

            kafkaTemplate.send(topicName,contract.getIntegrationId(), contract);

            return contract;
        };
    }

    @Override
    public Function<String, ContractStatus> getStatus() {
        return integrationId -> {
            ReadOnlyKeyValueStore<String, String> keyValueStore  = interactiveQueryService
                    .getQueryableStore(props.getStoreName(), QueryableStoreTypes.keyValueStore());
            return ContractStatus.valueOf(keyValueStore.get(integrationId));
        };
    }
}