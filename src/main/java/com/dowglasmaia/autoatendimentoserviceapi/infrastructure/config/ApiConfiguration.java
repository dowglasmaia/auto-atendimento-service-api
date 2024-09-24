package com.dowglasmaia.autoatendimentoserviceapi.infrastructure.config;

import com.dowglasmaia.autoatendimentoserviceapi.domain.command.CommandFactory;
import com.dowglasmaia.autoatendimentoserviceapi.domain.command.CommandInvoker;
import com.dowglasmaia.autoatendimentoserviceapi.infrastructure.logging.Logger;
import com.dowglasmaia.autoatendimentoserviceapi.infrastructure.logging.SLF4JLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfiguration {


    @Bean
    public Logger logger() {
        return new SLF4JLogger();
    }

    @Bean
    public CommandFactory commandFactory(){
        return new CommandFactory(logger());
    }

    @Bean
    public CommandInvoker commandInvoker(){
        return new CommandInvoker(logger());
    }

}
