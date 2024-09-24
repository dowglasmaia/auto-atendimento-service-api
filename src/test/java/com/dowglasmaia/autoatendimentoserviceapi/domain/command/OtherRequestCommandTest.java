package com.dowglasmaia.autoatendimentoserviceapi.domain.command;

import com.dowglasmaia.autoatendimentoserviceapi.domain.enums.RequestType;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Agent;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Request;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Team;
import com.dowglasmaia.autoatendimentoserviceapi.infrastructure.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class OtherRequestCommandTest {

    Logger logger;

    @BeforeEach
    void setUp(){
        logger = mock(Logger.class);
    }

    @Test
    public void shouldAssignRequestToAvailableAgent(){
        Team otherTeam = new Team("Other Team", getDefaultAgents());
        Request request = new Request("1", RequestType.OTHER, "123");
        OtherRequestCommand command = new OtherRequestCommand(request, otherTeam, logger);

        command.execute();

        assertTrue(otherTeam.getAgents().stream()
              .anyMatch(agent -> agent.getCurrentRequests().contains(request)));
    }

    @Test
    public void shouldQueueRequestWhenAllAgentsAreBusy(){
        Team otherTeam = new Team("Other Team", getDefaultAgents());
        for (Agent agent : otherTeam.getAgents()) {
            agent.assignRequest(new Request("1", RequestType.OTHER, "123"));
            agent.assignRequest(new Request("2", RequestType.OTHER, "124"));
            agent.assignRequest(new Request("3", RequestType.OTHER, "125"));
        }

        Request newRequest = new Request("4", RequestType.OTHER, "126");

        OtherRequestCommand command = new OtherRequestCommand(newRequest, otherTeam, logger);
        command.execute();

        assertTrue(otherTeam.getQueue().contains(newRequest));
    }

    @Test
    public void shouldQueueAndDequeueRequestsProperly(){
        // Cria o time com os agentes
        Team otherTeam = new Team("Other Team", getDefaultAgents());

        // O agente 1 está ocupado com 3 requisições
        Agent agent1 = otherTeam.getAgents().get(0);
        agent1.assignRequest(new Request("1", RequestType.OTHER, "client1"));
        agent1.assignRequest(new Request("2", RequestType.OTHER, "client2"));
        agent1.assignRequest(new Request("3", RequestType.OTHER, "client3"));

        // O agente 2 também está ocupado com 3 requisições
        Agent agent2 = otherTeam.getAgents().get(1);
        agent2.assignRequest(new Request("4", RequestType.OTHER, "client4"));
        agent2.assignRequest(new Request("5", RequestType.OTHER, "client5"));
        agent2.assignRequest(new Request("6", RequestType.OTHER, "client6"));

        // Verifica que ambos os agentes estão completamente ocupados
        assertFalse(agent1.isAvailable());
        assertFalse(agent2.isAvailable());

        // Adiciona novas requisições que devem ser enfileiradas
        Request request7 = new Request("7", RequestType.OTHER, "client7");
        new OtherRequestCommand(request7, otherTeam, logger).execute();

        Request request8 = new Request("8", RequestType.OTHER, "client8");
        new OtherRequestCommand(request8, otherTeam, logger).execute();

        // Verifica que as novas solicitações foram enfileiradas
        assertEquals(2, otherTeam.getQueue().size());

        // Simula o encerramento de um atendimento pelo agente 1
        agent1.finishRequest(agent1.getCurrentRequests().get(0));

        // Verifica que o agente 1 agora está disponível para uma nova solicitação
        assertTrue(agent1.isAvailable());

        // Executa o próximo comando da fila
        Request nextRequest = otherTeam.getQueue().poll();
        if (nextRequest != null) {
            agent1.assignRequest(nextRequest);
        }

        // Verifica se a fila foi decrementada e o próximo cliente foi atendido
        assertEquals(1, otherTeam.getQueue().size());
        assertTrue(agent1.getCurrentRequests().contains(request7));
    }


    private List<Agent> getDefaultAgents(){
        return List.of(
              new Agent("Other Agent 1", 3),
              new Agent("Other Agent 2", 3)
        );
    }
}
