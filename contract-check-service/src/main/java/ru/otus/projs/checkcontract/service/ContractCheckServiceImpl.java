package ru.otus.projs.checkcontract.service;

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
public class ContractCheckServiceImpl implements ContractCheckService {

    private final Random random = new Random();
    private final TraceService traceService;

    @Override
    public ContractStatus checkContract(TracedContractInfo contract) {
        return traceService.<ContractStatus>tracingAction((span)-> {

            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ContractStatus status = random.nextInt(8) % 2 == 0 ? ContractStatus.CHECKED : ContractStatus.INCORRECT;
            log.info("Contract with integrationId {} received, status on checking: {}", contract.getIntegrationId(), status.name());

            span.tag("integrationId", contract.getIntegrationId());
            span.tag("status", status.getValue());
            span.tag("contractNumber", contract.getContractNumber());
            return status;
        }, contract);
    }
}
