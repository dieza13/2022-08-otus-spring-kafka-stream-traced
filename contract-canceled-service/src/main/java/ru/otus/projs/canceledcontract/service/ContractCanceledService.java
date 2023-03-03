package ru.otus.projs.canceledcontract.service;

import ru.otus.projs.common.models.TracedContractInfo;

public interface ContractCanceledService {

    void onCanceledContract(TracedContractInfo contract);

}
