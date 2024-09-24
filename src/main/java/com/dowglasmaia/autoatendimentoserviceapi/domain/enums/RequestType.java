package com.dowglasmaia.autoatendimentoserviceapi.domain.enums;

public enum RequestType {
    CARD_ISSUE("Problemas com cartão"),
    LOAN_REQUEST("contratação de empréstimo"),
    OTHER("outro assunto");

    private final String description;

    RequestType(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
