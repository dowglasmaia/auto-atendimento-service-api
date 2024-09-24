package com.dowglasmaia.autoatendimentoserviceapi.domain.command;

import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Request;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Team;
import com.dowglasmaia.autoatendimentoserviceapi.infrastructure.logging.Logger;

public class CommandFactory {

    private final Logger logger;

    public CommandFactory(Logger logger){
        this.logger = logger;
    }

    public Command createCommand(Request request, Team team){
        logger.info("Running createCommand for request: " + request.id() + " and team: " + team.getName());

        if ("CARD_ISSUE".equals(team.getName())) {
            return new CardRequestCommand(request, team, logger);
        } else if ("LOAN_REQUEST".equals(team.getName())) {
            return new LoanRequestCommand(request, team);
        } else if ("OTHER".equals(team.getName())) {
            return new OtherRequestCommand(request, team, logger);
        }

        throw new IllegalArgumentException("Unknown team type: " + team.getName());
    }

    public Command createCompleteCommand(String agentId, String requestId, Team team){
        logger.info("Running createCompleteCommand for agentId: " + agentId
              + ", and team: " + team.getName()
              + ", and requestId: " + requestId);
        return new CompleteRequestCommand(agentId, requestId, team, logger);
    }
}


