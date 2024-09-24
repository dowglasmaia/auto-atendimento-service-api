package com.dowglasmaia.autoatendimentoserviceapi.application.service;

import com.dowglasmaia.autoatendimentoserviceapi.domain.command.CommandFactory;
import com.dowglasmaia.autoatendimentoserviceapi.domain.command.CommandInvoker;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Agent;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Request;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Team;
import com.dowglasmaia.autoatendimentoserviceapi.infrastructure.repository.AgentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestDistributionService {

    private final AgentRepository agentRepository;
    private final CommandFactory commandFactory;
    private final CommandInvoker commandInvoker;


    public RequestDistributionService(AgentRepository agentRepository, CommandFactory commandFactory, CommandInvoker commandInvoker) {
        this.agentRepository = agentRepository;
        this.commandFactory = commandFactory;
        this.commandInvoker = commandInvoker;
    }

    public void distributeRequest(Request request) {
        List<Agent> agents = agentRepository.getAgentsByTeam(request.subject().name());

        Team team = new Team(request.subject().name(), agents);

        var command = commandFactory.createCommand(request, team);
        commandInvoker.addCommand(command);
        commandInvoker.executeCommands();
    }

    public void completeRequest(String agentId, String requestId, String requestType) {
        List<Agent> agents = agentRepository.getAgentsByTeam(requestType);

        Team team = new Team(requestType, agents);

        var completeCommand = commandFactory.createCompleteCommand(agentId, requestId, team);
        commandInvoker.addCommand(completeCommand);
        commandInvoker.executeCommands();
    }
}


