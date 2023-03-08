package ru.otus.projs.canceledcontract.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.projs.common.models.ContractStatus;
import ru.otus.projs.common.models.TracedContractInfo;
import ru.otus.projs.tracing.service.TraceService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractCanceledServiceImpl implements ContractCanceledService {
    private final TraceService traceService;

    @Override
    public void onCanceledContract(TracedContractInfo contract) {

        traceService.<ContractStatus>tracingAction((span)-> {

            log.info("Contract with integrationId {} was canceled", contract.getIntegrationId());

            span.tag("integrationId", contract.getIntegrationId());
            span.tag("status", contract.getStatus().getValue());
            span.tag("contractNumber", contract.getContractNumber());

        }, contract);
    }
}
