package ru.otus.projs.executingcontract.service;

import ru.otus.projs.common.models.ContractStatus;
import ru.otus.projs.common.models.TracedContractInfo;

public interface ContractExecutingService {

    ContractStatus executingContract(TracedContractInfo contract);

}
