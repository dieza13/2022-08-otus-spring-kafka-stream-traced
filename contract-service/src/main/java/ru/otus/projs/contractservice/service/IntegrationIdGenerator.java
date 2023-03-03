package ru.otus.projs.contractservice.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IntegrationIdGenerator implements UniqueIdGenerator{
    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
