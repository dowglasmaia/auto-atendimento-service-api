package com.dowglasmaia.autoatendimentoserviceapi.domain.command;

import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Agent;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Request;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Team;
import com.dowglasmaia.autoatendimentoserviceapi.infrastructure.logging.Logger;
import com.dowglasmaia.autoatendimentoserviceapi.infrastructure.repository.AgentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static com.dowglasmaia.autoatendimentoserviceapi.domain.enums.RequestType.LOAN_REQUEST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class CommandFactoryTest {

    private CommandFactory commandFactory;

    Logger logger;


    @MockBean
    private AgentRepository agentRepository;

    @BeforeEach
    void setUp(){
        logger = mock(Logger.class);
        commandFactory = new CommandFactory(logger);
    }
    @Test
    void shouldAssignRequestToAvailableAgent() {
        // Usando o nome correto do time conforme esperado pelo CommandFactory
        List<Agent> agents = List.of(new Agent("Loan_Agent_1", 3)); // Agente correto para LOAN_REQUEST
        Request request = new Request("1", LOAN_REQUEST, "Client1"); // Criação do request
        Team team = new Team("LOAN_REQUEST", agents); // Nome do time deve ser LOAN_REQUEST

        // Criando o comando com o request e o time corretos
        Command command = commandFactory.createCommand(request, team);
        command.execute(); // Executa o comando

        // Asserções para verificar se a requisição foi atribuída corretamente
        assertEquals(1, agents.get(0).getCurrentRequests().size());
        assertTrue(agents.get(0).getCurrentRequests().contains(request));
    }

    @Test
    void shouldQueueRequestWhenNoAgentIsAvailable(){
        // Cria o agente com capacidade máxima de 3 requisições e já atribui 3 requisições a ele
        Agent agent = new Agent("Agent1", 3);
        agent.assignRequest(new Request("1", LOAN_REQUEST, "Client1"));
        agent.assignRequest(new Request("2", LOAN_REQUEST, "Client2"));
        agent.assignRequest(new Request("3", LOAN_REQUEST, "Client3"));  // Limite atingido

        // Cria a lista de agentes com o agente indisponível
        List<Agent> agents = List.of(agent);

        // Cria o time antes da execução do comando
        Team team = new Team("LOAN_REQUEST", agents);

        // Nova requisição que deverá ser enfileirada
        Request newRequest = new Request("4", LOAN_REQUEST, "Client4");

        // Cria o comando passando o time diretamente
        Command command = commandFactory.createCommand(newRequest, team); // Passa o team diretamente
        command.execute();

        // Verifica se a nova requisição foi corretamente enfileirada
        assertEquals(1, team.getQueue().size());
        assertTrue(team.getQueue().contains(newRequest));
    }


    @Test
    void shouldAssignNextRequestFromQueueAfterFinishingCurrent(){
        Agent agent = new Agent("Agent1", 1);  // Agente pode lidar com uma única requisição simultaneamente
        Request request1 = new Request("1", LOAN_REQUEST, "Client1");
        Request request2 = new Request("2", LOAN_REQUEST, "Client2");

        // O agente já está ocupado com uma requisição
        agent.assignRequest(request1);

        // Criamos o time com o agente
        Team team = new Team("Loan Team", List.of(agent));

        // Adicionamos a segunda requisição à fila do time
        team.getQueue().offer(request2);

        // Criamos o comando para finalizar a requisição atual e atribuir a próxima da fila
        Command completeCommand = commandFactory.createCompleteCommand(agent.getId(), request1.id(), team);
        completeCommand.execute();

        // Verificamos se a segunda requisição foi atribuída ao agente após ele finalizar a primeira
        assertTrue(agent.getCurrentRequests().contains(request2));
        assertEquals(1, agent.getCurrentRequests().size());
    }


    @Test
    void shouldThrowExceptionWhenAgentNotFound(){
        List<Agent> agents = List.of(new Agent("Agent1", 3));  // Existe apenas Agent1

        // Criamos o time com o agente existente
        Team team = new Team("Loan Team", agents);

        // Tentamos finalizar uma requisição de um agente inexistente
        assertThrows(IllegalArgumentException.class, () -> {
            Command completeCommand = commandFactory.createCompleteCommand("non-existent-agent", "request-id", team);
            completeCommand.execute();
        });
    }


}
