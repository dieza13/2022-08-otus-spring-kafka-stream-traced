package ru.otus.projs.correctingcontract.service;

import ru.otus.projs.common.models.ContractStatus;
import ru.otus.projs.common.models.TracedContractInfo;

public interface ContractCorrectingService {

    ContractStatus correctingContract(TracedContractInfo contract);

}
