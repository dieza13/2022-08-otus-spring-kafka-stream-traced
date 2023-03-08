package ru.otus.projs.confirmedcontract.service;

import ru.otus.projs.common.models.TracedContractInfo;

public interface ContractConfirmedService {

    void onConfirmedContract(TracedContractInfo contract);

}
