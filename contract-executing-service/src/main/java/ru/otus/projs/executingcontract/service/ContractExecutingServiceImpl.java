package ru.otus.projs.executingcontract.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.projs.common.models.ContractStatus;
import ru.otus.projs.common.models.TracedContractInfo;
import ru.otus.projs.tracing.service.TraceService;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractExecutingServiceImpl implements ContractExecutingService {

    private final Random random = new Random();
    private final TraceService traceService;

    @Override
    public ContractStatus executingContract(TracedContractInfo contract) {

        return traceService.<ContractStatus>tracingAction((span)-> {

            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ContractStatus status = random.nextInt(8) % 2 == 0 ? ContractStatus.CONFIRMED : ContractStatus.CANCELED;
            log.info("Contract with integrationId {} received status on executing: {}", contract.getIntegrationId(), status.name());

            span.tag("integrationId", contract.getIntegrationId());
            span.tag("status", status.getValue());
            span.tag("contractNumber", contract.getContractNumber());
            return status;
        }, contract);
    }
}
