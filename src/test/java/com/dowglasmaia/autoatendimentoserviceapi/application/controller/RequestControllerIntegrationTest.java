package com.dowglasmaia.autoatendimentoserviceapi.application.controller;

import com.dowglasmaia.autoatendimentoserviceapi.MaiaBotServiceApplication;
import com.dowglasmaia.autoatendimentoserviceapi.domain.enums.RequestType;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Agent;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Request;
import com.dowglasmaia.autoatendimentoserviceapi.infrastructure.logging.Logger;
import com.dowglasmaia.autoatendimentoserviceapi.infrastructure.repository.AgentRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = MaiaBotServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RequestControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @MockBean
    private AgentRepository agentRepository;

    @MockBean
    private Logger logger;

    @BeforeEach
    public void setUp(){
        RestAssured.port = port;
    }

    @Test
    void shouldHandleLoanRequest(){
        Request loanRequest = new Request(UUID.randomUUID().toString(), RequestType.LOAN_REQUEST, "client1");

        Response response = given()
              .body(loanRequest)
              .contentType("application/json")
              .when()
              .post("/api/v1/requests")
              .then()
              .statusCode(200)
              .extract().response();

        String requestId = response.jsonPath().getString("requestId");
        assertThat(requestId, is(notNullValue()));
    }

    @Test
    void shouldCompleteLoanRequest(){
        Request request = new Request("1", RequestType.LOAN_REQUEST, "client789");
        Agent loanAgent1 = new Agent("Loan_Agent_1", 3);
        loanAgent1.assignRequest(request);


        when(agentRepository.getAgentsByTeam("LOAN_REQUEST"))
              .thenReturn(List.of(loanAgent1));


        given()
              .param("requestId", "1")
              .param("requestType", RequestType.LOAN_REQUEST)
              .when()
              .put("/api/v1/requests/{agentId}/complete", "Loan_Agent_1")
              .then()
              .statusCode(200);
    }


    @Test
    void shouldHandleCardIssueRequest(){
        Request cardRequest = new Request("2", RequestType.CARD_ISSUE, "client2");
        given()
              .body(cardRequest)
              .contentType("application/json")
              .when()
              .post("/api/v1/requests")
              .then()
              .statusCode(200);
    }

    @Test
    void shouldCompleteCardIssueRequest(){
        Request request = new Request("2", RequestType.CARD_ISSUE, "client123");

        Agent cardAgent1 = new Agent("Card_Agent_1", 3);
        cardAgent1.assignRequest(request);

        when(agentRepository.getAgentsByTeam("CARD_ISSUE"))
              .thenReturn(List.of(cardAgent1));

        given()
              .param("requestId", "2")
              .param("requestType", RequestType.CARD_ISSUE)
              .when()
              .put("/api/v1/requests/{agentId}/complete", "Card_Agent_1")
              .then()
              .statusCode(200);


    }

    @Test
    void shouldHandleOtherRequest(){
        Request otherRequest = new Request("3", RequestType.OTHER, "client3");

        given()
              .body(otherRequest)
              .contentType("application/json")
              .when()
              .post("/api/v1/requests")
              .then()
              .statusCode(200);
    }

    @Test
    void shouldCompleteOtherRequest(){
        Request request = new Request("3", RequestType.OTHER, "client456");

        Agent otherAgent1 = new Agent("Other_Agent_1", 3);
        otherAgent1.assignRequest(request);

        when(agentRepository.getAgentsByTeam("OTHER"))
              .thenReturn(List.of(otherAgent1));

        given()
              .param("requestId", "3")
              .param("requestType", RequestType.OTHER)
              .when()
              .put("/api/v1/requests/{agentId}/complete", "Other_Agent_1")
              .then()
              .statusCode(200);
    }

}
