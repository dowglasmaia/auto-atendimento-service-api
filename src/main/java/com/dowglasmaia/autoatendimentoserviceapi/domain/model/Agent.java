package com.dowglasmaia.autoatendimentoserviceapi.domain.model;


import java.util.ArrayList;
import java.util.List;

public class Agent {
    private String id;
    private int maxSimultaneousRequests;
    private List<Request> currentRequests;

    public Agent(String id, int maxSimultaneousRequests){
        this.id = id;
        this.maxSimultaneousRequests = maxSimultaneousRequests;
        this.currentRequests = new ArrayList<>();
    }

    public boolean isAvailable(){
        return currentRequests.size() < maxSimultaneousRequests;
    }

    public void assignRequest(Request request){
        currentRequests.add(request);
        System.out.println("Request added. Current size: " + currentRequests.size());
    }

    public boolean finishRequest(Request request){
        return currentRequests.remove(request);
    }

    public List<Request> getCurrentRequests(){
        return new ArrayList<>(currentRequests);
    }

    public String getId(){
        return id;
    }
}
