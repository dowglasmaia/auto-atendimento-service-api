package com.dowglasmaia.autoatendimentoserviceapi.domain.command;


import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Request;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Team;
import com.dowglasmaia.autoatendimentoserviceapi.infrastructure.logging.Logger;


public class CardRequestCommand implements Command {

    private final Request request;
    private final Team cardTeam;
    private final Logger logger;

    public CardRequestCommand(Request request, Team cardTeam, Logger logger){
        this.request = request;
        this.cardTeam = cardTeam;
        this.logger = logger;
    }

    @Override
    public void execute(){
        logger.info("Running CardRequestCommand for request: " + request.id());
        cardTeam.assignOrEnqueue(request);
    }
}

