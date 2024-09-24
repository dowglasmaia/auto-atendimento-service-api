package com.dowglasmaia.autoatendimentoserviceapi.domain.command.mocks;

import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Agent;

import java.util.List;

public class MocksBuild {

    public static List<Agent> getDefaultAgents(){
        return List.of(
              new Agent("Agent 1", 3),
              new Agent("Agent 2", 3)
        );
    }

}
