package com.dowglasmaia.autoatendimentoserviceapi.domain.exception;

public class AgentCapacityReachedException extends RuntimeException {
    public AgentCapacityReachedException(String agentId){
        super("Agent " + agentId + " is at full capacity");
    }
}
