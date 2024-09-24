package com.dowglasmaia.autoatendimentoserviceapi.domain.command;

import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Agent;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Team;
import com.dowglasmaia.autoatendimentoserviceapi.infrastructure.logging.Logger;

public class CompleteRequestCommand implements Command {

    private final String agentId;
    private final String requestId;
    private final Team team;
    private final Logger logger;

    public CompleteRequestCommand(String agentId, String requestId, Team team, Logger logger){
        this.agentId = agentId;
        this.requestId = requestId;
        this.team = team;
        this.logger = logger;
    }

    @Override
    public void execute(){
        Agent agent = team.getAgents().stream()
              .filter(a -> a.getId().equals(agentId))
              .findFirst()
              .orElseThrow(() -> new IllegalArgumentException("Agent not found: " + agentId));

        agent.getCurrentRequests().stream()
              .filter(r -> r.id().equals(requestId))
              .findFirst()
              .ifPresentOrElse(
                    agent::finishRequest,
                    () -> {
                        logger.error("Requisition not found for the agent: " + agentId, new IllegalArgumentException());
                        throw new IllegalArgumentException("Requisition not found for the agent: " + agentId);
                    });

        if (!team.getQueue().isEmpty() && agent.isAvailable()) {
            agent.assignRequest(team.getQueue().poll());
        }
    }
}
