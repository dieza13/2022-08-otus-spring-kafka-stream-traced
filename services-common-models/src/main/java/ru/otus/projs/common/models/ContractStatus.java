package ru.otus.projs.common.models;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ContractStatus {
    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    CANCELED("CANCELED"),
    CHECKED("CHECKED"),
    INCORRECT("INCORRECT");

    private final String value;

    public String getValue() {
        return this.value;
    }
}
