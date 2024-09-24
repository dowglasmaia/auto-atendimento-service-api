package com.dowglasmaia.autoatendimentoserviceapi.infrastructure.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentConfig {

    @Value("${agent.max-simultaneous-requests}")
    private int maxSimultaneousRequests;

    public int getMaxSimultaneousRequests() {
        return maxSimultaneousRequests;
    }
}
