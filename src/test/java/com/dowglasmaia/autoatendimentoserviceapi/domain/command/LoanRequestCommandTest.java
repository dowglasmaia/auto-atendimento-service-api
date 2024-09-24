package com.dowglasmaia.autoatendimentoserviceapi.domain.command;

import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Agent;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Request;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Team;
import org.junit.jupiter.api.Test;

import static com.dowglasmaia.autoatendimentoserviceapi.domain.command.mocks.MocksBuild.getDefaultAgents;
import static com.dowglasmaia.autoatendimentoserviceapi.domain.enums.RequestType.LOAN_REQUEST;
import static org.junit.jupiter.api.Assertions.*;

class LoanRequestCommandTest {

    @Test
    public void shouldAssignRequestToAvailableAgent(){
        Team loanTeam = new Team("Loan Team", getDefaultAgents());
        Request request = new Request("1", LOAN_REQUEST, "123");
        LoanRequestCommand command = new LoanRequestCommand(request, loanTeam);

        command.execute();

        assertTrue(loanTeam.getAgents().stream()
              .anyMatch(agent -> agent.getCurrentRequests().contains(request)));
    }

    @Test
    public void shouldQueueRequestWhenAllAgentsAreBusy(){
        Team loanTeam = new Team("Loan Team", getDefaultAgents());
        for (Agent agent : loanTeam.getAgents()) {
            agent.assignRequest(new Request("1", LOAN_REQUEST, "123"));
            agent.assignRequest(new Request("2", LOAN_REQUEST, "124"));
            agent.assignRequest(new Request("3", LOAN_REQUEST, "125"));
        }

        Request newRequest = new Request("4", LOAN_REQUEST, "126");

        LoanRequestCommand command = new LoanRequestCommand(newRequest, loanTeam);
        command.execute();

        assertTrue(loanTeam.getQueue().contains(newRequest));
    }

    @Test
    public void shouldQueueAndDequeueRequestsProperly(){

        Team loanTeam = new Team("Loan Team", getDefaultAgents());


        // Cenário: O agente 1 do Loan Team já está com 3 solicitações
        Agent agent = loanTeam.getAgents().get(0);
        agent.assignRequest(new Request("1", LOAN_REQUEST, "client1"));
        agent.assignRequest(new Request("2", LOAN_REQUEST, "client2"));
        agent.assignRequest(new Request("3", LOAN_REQUEST, "client3"));

        // Verifica que o agente está completamente ocupado
        assertFalse(agent.isAvailable());

        // Adiciona mais 4 solicitações ao Loan Team
        Request request4 = new Request("4", LOAN_REQUEST, "client4");
        new LoanRequestCommand(request4, loanTeam).execute();

        Request request5 = new Request("5", LOAN_REQUEST, "client5");
        new LoanRequestCommand(request5, loanTeam).execute();

        Request request6 = new Request("6", LOAN_REQUEST, "client6");
        new LoanRequestCommand(request6, loanTeam).execute();

        Request request7 = new Request("7", LOAN_REQUEST, "client7");
        new LoanRequestCommand(request7, loanTeam).execute();

        // Verifica que apenas a última solicitação foi enfileirada
        assertEquals(1, loanTeam.getQueue().size());

        // Simula o encerramento de um atendimento
        agent.finishRequest(agent.getCurrentRequests().get(0));

        // Verifica que o agente agora está disponível para uma nova solicitação
        assertTrue(agent.isAvailable());

        // Executa o próximo comando da fila
        Request nextRequest = loanTeam.getQueue().poll();
        if (nextRequest != null) {
            agent.assignRequest(nextRequest);
        }

        // Verifica se a fila foi decrementada e o próximo cliente foi atendido
        assertEquals(0, loanTeam.getQueue().size());
        assertTrue(agent.getCurrentRequests().contains(request7));
    }

    @Test
    public void shouldQueueAndDequeueMultipleRequestsAfterAllAgentsAreBusy() throws InterruptedException{

        Team loanTeam = new Team("Loan Team", getDefaultAgents());

        // Cenário: Todos os agentes do Loan Team já estão com 3 solicitações (max capacity)
        Agent agent1 = loanTeam.getAgents().get(0);
        Agent agent2 = loanTeam.getAgents().get(1);

        // Preenche os agentes com suas 3 solicitações simultâneas
        agent1.assignRequest(new Request("1", LOAN_REQUEST, "client1"));
        agent1.assignRequest(new Request("2", LOAN_REQUEST, "client2"));
        agent1.assignRequest(new Request("3", LOAN_REQUEST, "client3"));

        agent2.assignRequest(new Request("4", LOAN_REQUEST, "client4"));
        agent2.assignRequest(new Request("5", LOAN_REQUEST, "client5"));
        agent2.assignRequest(new Request("6", LOAN_REQUEST, "client6"));

        // Verifica que todos os agentes estão completamente ocupados
        assertFalse(agent1.isAvailable());
        assertFalse(agent2.isAvailable());

        // Adiciona mais 5 novas solicitações ao Loan Team
        Request request10 = new Request("10", LOAN_REQUEST, "client10");
        Request request11 = new Request("11", LOAN_REQUEST, "client11");
        Request request12 = new Request("12", LOAN_REQUEST, "client12");
        Request request13 = new Request("13", LOAN_REQUEST, "client13");
        Request request14 = new Request("14", LOAN_REQUEST, "client14");

        new LoanRequestCommand(request10, loanTeam).execute();
        new LoanRequestCommand(request11, loanTeam).execute();
        new LoanRequestCommand(request12, loanTeam).execute();
        new LoanRequestCommand(request13, loanTeam).execute();
        new LoanRequestCommand(request14, loanTeam).execute();

        // Verifica que as 5 novas solicitações foram enfileiradas
        assertEquals(5, loanTeam.getQueue().size());

        // Espera 2 segundos antes de liberar o agente 1
        Thread.sleep(2000);

        // Simula o encerramento de um atendimento pelo agente 1
        agent1.finishRequest(agent1.getCurrentRequests().get(0));

        // Verifica que o agente 1 agora está disponível para uma nova solicitação
        assertTrue(agent1.isAvailable());

        // O agente 1 deve pegar apenas uma das novas solicitações (request10)
        Request nextRequest = loanTeam.getQueue().poll();
        if (nextRequest != null && agent1.isAvailable()) {
            agent1.assignRequest(nextRequest);
        }

        // Verifica que a fila agora tem 4 solicitações restantes
        assertEquals(4, loanTeam.getQueue().size());

        // Verifica que o agente 1 tem 3 solicitações, incluindo a request10
        assertEquals(3, agent1.getCurrentRequests().size());
        assertTrue(agent1.getCurrentRequests().contains(request10));

        // Verifica que as outras solicitações ainda estão na fila
        assertTrue(loanTeam.getQueue().contains(request11));
        assertTrue(loanTeam.getQueue().contains(request12));
        assertTrue(loanTeam.getQueue().contains(request13));
        assertTrue(loanTeam.getQueue().contains(request14));
    }


}
