package ru.otus.projs.confirmedcontract.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.projs.common.models.TracedContractInfo;
import ru.otus.projs.tracing.service.TraceService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractConfirmedServiceImpl implements ContractConfirmedService {

    private final TraceService traceService;

    @Override
    public void onConfirmedContract(TracedContractInfo contract) {

        traceService.tracingAction((span)-> {
            log.info("Contract with integrationId {} was confirmed", contract.getIntegrationId());
            span.tag("integrationId", contract.getIntegrationId());
            span.tag("status", contract.getStatus().getValue());
            span.tag("contractNumber", contract.getContractNumber());
            return null;
        }, contract);
    }

}
