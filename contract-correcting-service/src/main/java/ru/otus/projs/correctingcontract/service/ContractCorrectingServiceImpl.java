package ru.otus.projs.correctingcontract.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.projs.common.models.ContractStatus;
import ru.otus.projs.common.models.TracedContractInfo;
import ru.otus.projs.tracing.service.TraceService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractCorrectingServiceImpl implements ContractCorrectingService {

    private final TraceService traceService;

    @Override
    public ContractStatus correctingContract(TracedContractInfo contract) {

        return traceService.<ContractStatus>tracingAction((span)-> {

            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ContractStatus status = ContractStatus.PENDING;
            log.info("Contract with integrationId {} received status on correcting: {}", contract.getIntegrationId(), status.name());

            span.tag("integrationId", contract.getIntegrationId());
            span.tag("status", status.getValue());
            span.tag("contractNumber", contract.getContractNumber());
            return status;
        }, contract);
    }
}
