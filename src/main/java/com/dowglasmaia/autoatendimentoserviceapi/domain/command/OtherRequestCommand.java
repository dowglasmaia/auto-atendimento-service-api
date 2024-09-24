package com.dowglasmaia.autoatendimentoserviceapi.domain.command;

import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Agent;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Request;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Team;
import com.dowglasmaia.autoatendimentoserviceapi.infrastructure.logging.Logger;


public class OtherRequestCommand implements Command {

    private final Request request;
    private final Team otherTeam;
    private final Logger logger;

    public OtherRequestCommand(Request request, Team otherTeam, Logger logger){
        this.request = request;
        this.otherTeam = otherTeam;
        this.logger = logger;
    }

    @Override
    public void execute(){
        logger.info("Running command for request: " + request.id());

        otherTeam.getAgents().stream()
              .filter(Agent::isAvailable)
              .findFirst()
              .ifPresentOrElse(
                    agent -> {
                        logger.info("Assigning request to the agent: " + agent.getId());
                        agent.assignRequest(request);
                    },
                    () -> {
                        logger.info("Filling Request: " + request.id());
                        otherTeam.getQueue().offer(request);
                    }
              );
    }
}