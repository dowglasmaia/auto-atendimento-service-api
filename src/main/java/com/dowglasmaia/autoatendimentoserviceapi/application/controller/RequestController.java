package com.dowglasmaia.autoatendimentoserviceapi.application.controller;

import br.com.dowglasmaia.openapi.api.RequestsApi;
import br.com.dowglasmaia.openapi.model.RequestResponse;
import com.dowglasmaia.autoatendimentoserviceapi.application.controller.mapper.RequestMapper;
import com.dowglasmaia.autoatendimentoserviceapi.application.service.RequestDistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class RequestController implements RequestsApi {

    private final RequestDistributionService requestDistributionService;

    @Autowired
    public RequestController(RequestDistributionService requestDistributionService){
        this.requestDistributionService = requestDistributionService;
    }

    @Override
    public Mono<ResponseEntity<Void>> completeRequest(String agentId, String requestId, String requestType, ServerWebExchange exchange){
        return Mono.fromRunnable(() ->
              requestDistributionService.completeRequest(agentId, requestId, requestType));
    }

    @Override
    public Mono<ResponseEntity<RequestResponse>> distributeRequest(Mono<br.com.dowglasmaia.openapi.model.Request> request, ServerWebExchange exchange){
        return request
              .map(RequestMapper::toRequestDomain)
              .flatMap(requestDomain ->
                    Mono.fromRunnable(() -> requestDistributionService.distributeRequest(requestDomain))
                          .thenReturn(ResponseEntity.ok(RequestMapper.toRequestResponse(requestDomain)))
              );
    }

}

