package com.dowglasmaia.autoatendimentoserviceapi.domain.command;

import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Agent;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Request;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Team;


public class LoanRequestCommand implements Command {

    private final Request request;
    private final Team loanTeam;

    public LoanRequestCommand(Request request, Team loanTeam){
        this.request = request;
        this.loanTeam = loanTeam;
    }

    @Override
    public void execute(){
        loanTeam.getAgents().stream()
              .filter(Agent::isAvailable)
              .findFirst()
              .ifPresentOrElse(
                    agent -> {
                        agent.assignRequest(request);
                    },
                    () -> {
                        loanTeam.getQueue().offer(request);
                    }
              );
    }
}