package ru.otus.projs.branchprocessor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.projs.common.models.ContractStatus;
import ru.otus.projs.common.models.TracedContractInfo;
import ru.otus.projs.tracing.service.TraceService;

@Service
@RequiredArgsConstructor
public class BranchingContractServiceImpl implements BranchingContractService {

    private final TraceService traceService;

    @Override
    public boolean branchProcessing(ContractStatus contractStatus, TracedContractInfo contract) {
        if (contractStatus.equals(contract.getStatus()))
            return traceService.tracingAction((span)-> {
                span.tag("integrationId", contract.getIntegrationId());
                span.tag("status", contract.getStatus().getValue());
                span.tag("contractNumber", contract.getContractNumber());
                return true;
            }, contract);
        return false;
    }
}
