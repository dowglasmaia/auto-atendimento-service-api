package com.dowglasmaia.autoatendimentoserviceapi.infrastructure.logging;

import org.slf4j.LoggerFactory;

public class SLF4JLogger implements Logger {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(SLF4JLogger.class);

    @Override
    public void info(String message){
        logger.info(message);
    }

    @Override
    public void error(String message, Throwable throwable){
        logger.error(message, throwable);
    }
}
