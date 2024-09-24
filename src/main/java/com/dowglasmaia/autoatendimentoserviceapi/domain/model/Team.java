package com.dowglasmaia.autoatendimentoserviceapi.domain.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


import java.util.concurrent.atomic.AtomicInteger;

public class Team {
    private String name;
    private Queue<Request> queue = new LinkedList<>();
    private List<Agent> agents;
    private AtomicInteger agentIndex = new AtomicInteger(0); // Índice para rodar entre os agentes

    public Team(String name, List<Agent> agents){
        this.name = name;
        this.agents = agents;
    }

    public String getName(){
        return name;
    }

    public Queue<Request> getQueue(){
        return queue;
    }

    public List<Agent> getAgents(){
        return agents;
    }

    public void assignOrEnqueue(Request request) {
        // Tentar atribuir a requisição a um agente disponível usando round-robin
        for (int i = 0; i < agents.size(); i++) {
            // O índice é incrementado de forma circular
            int currentIndex = agentIndex.getAndUpdate(index -> (index + 1) % agents.size());
            Agent currentAgent = agents.get(currentIndex);

            if (currentAgent.isAvailable()) {
                System.out.println("Assigning request to the agent: " + currentAgent.getId());
                currentAgent.assignRequest(request);
                return; // Saímos do método após atribuir a requisição
            }
        }

        // Se nenhum agente estiver disponível, a requisição é enfileirada
        System.out.println("Filling Request: " + request.id());
        queue.offer(request);
    }

}

