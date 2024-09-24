package com.dowglasmaia.autoatendimentoserviceapi.domain.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Team {
    private String name;
    private Queue<Request> queue = new LinkedList<>();
    private List<Agent> agents;
    private AtomicInteger agentIndex = new AtomicInteger(0);

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

    public void assignOrEnqueue(Request request){
        for (int i = 0; i < agents.size(); i++) {
            int currentIndex = agentIndex.getAndUpdate(index -> (index + 1) % agents.size());
            Agent currentAgent = agents.get(currentIndex);

            if (currentAgent.isAvailable()) {
                currentAgent.assignRequest(request);
                return;
            }
        }

        queue.offer(request);
    }

}

