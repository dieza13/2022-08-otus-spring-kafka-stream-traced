package ru.otus.projs.branchprocessor.service;


import ru.otus.projs.common.models.ContractStatus;
import ru.otus.projs.common.models.TracedContractInfo;

public interface BranchingContractService {
    boolean branchProcessing(ContractStatus contractStatus, TracedContractInfo contract);
}
