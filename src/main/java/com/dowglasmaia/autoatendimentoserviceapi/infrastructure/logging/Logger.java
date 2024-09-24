package com.dowglasmaia.autoatendimentoserviceapi.infrastructure.logging;

public interface Logger {

    void info(String message);
    void error(String message, Throwable throwable);
}
