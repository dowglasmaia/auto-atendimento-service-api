package com.dowglasmaia.autoatendimentoserviceapi.domain.model;


import java.util.ArrayList;
import java.util.List;

public class Agent {
    private String id;
    private int maxSimultaneousRequests;
    private List<Request> currentRequests;

    // Construtor público para permitir a criação de múltiplos agentes
    public Agent(String id, int maxSimultaneousRequests){
        this.id = id;
        this.maxSimultaneousRequests = maxSimultaneousRequests;
        this.currentRequests = new ArrayList<>();
    }

    // Verifica se o agente está disponível
    public boolean isAvailable(){
        return currentRequests.size() < maxSimultaneousRequests;
    }

    // Atribui uma nova requisição ao agente
    public void assignRequest(Request request){
        currentRequests.add(request);
        System.out.println("Request added. Current size: " + currentRequests.size());
    }

    // Remove uma requisição do agente
    public boolean finishRequest(Request request){
        return currentRequests.remove(request);
    }

    // Retorna a lista atual de requisições
    public List<Request> getCurrentRequests(){
        return new ArrayList<>(currentRequests);  // Retorna uma cópia para evitar modificações externas
    }

    // Retorna o ID do agente
    public String getId(){
        return id;
    }
}
