package com.dowglasmaia.autoatendimentoserviceapi.infrastructure.repository;

import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Agent;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AgentRepository {

    private Map<String, List<Agent>> agentsByTeam = new HashMap<>();

    public AgentRepository(){
        agentsByTeam.put("CARD_ISSUE", List.of(
              new Agent("Card_Agent_1", 3),
              new Agent("Card_Agent_2", 3)));
        agentsByTeam.put("LOAN_REQUEST", List.of(
              new Agent("Loan_Agent_1", 3),
              new Agent("Loan_Agent_2", 3)));
        agentsByTeam.put("OTHER", List.of(
              new Agent("Other_Agent_1", 3),
              new Agent("Other_Agent_2", 3)));
    }

    public List<Agent> getAgentsByTeam(String teamName){
        if (!agentsByTeam.containsKey(teamName)) {
            throw new IllegalArgumentException("Team não encontrado: " + teamName);
        }
        return agentsByTeam.get(teamName);
    }
}
