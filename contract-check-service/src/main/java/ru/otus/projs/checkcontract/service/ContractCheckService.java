package ru.otus.projs.checkcontract.service;

import ru.otus.projs.common.models.ContractStatus;
import ru.otus.projs.common.models.TracedContractInfo;

public interface ContractCheckService {

    ContractStatus checkContract(TracedContractInfo contract);

}
