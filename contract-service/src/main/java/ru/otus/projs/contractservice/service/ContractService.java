package ru.otus.projs.contractservice.service;

import ru.otus.projs.common.models.Contract;
import ru.otus.projs.common.models.ContractStatus;

import java.util.function.Function;

public interface ContractService {

    Function<Contract, Contract> pushContract();
    Function<String, ContractStatus> getStatus();



}
