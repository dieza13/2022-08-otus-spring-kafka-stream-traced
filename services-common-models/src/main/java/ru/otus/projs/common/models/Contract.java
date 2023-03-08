package ru.otus.projs.common.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Contract {

    private String integrationId;
    @NotNull
    private String contractNumber;
    private ContractStatus status;
}
