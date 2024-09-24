package com.dowglasmaia.autoatendimentoserviceapi.domain.model;

import com.dowglasmaia.autoatendimentoserviceapi.domain.enums.RequestType;


public record Request(String id, RequestType subject, String customerId) {
    public Request{
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (subject == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        }
    }
}
