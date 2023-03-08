package ru.otus.projs.contractservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.projs.common.models.Contract;
import ru.otus.projs.common.models.ContractStatus;
import ru.otus.projs.contractservice.service.ContractService;

@RestController
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping(path = "/contract")
    public Contract pushContract(@RequestBody Contract contract) {
        return contractService.pushContract().apply(contract);
    }

    @GetMapping(path = "/contract/{integrationId}/status")
    public ContractStatus getContractStatus(@PathVariable String integrationId) {
        return contractService.getStatus().apply(integrationId);
    }
}
