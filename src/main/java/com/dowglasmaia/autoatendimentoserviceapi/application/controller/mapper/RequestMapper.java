package com.dowglasmaia.autoatendimentoserviceapi.application.controller.mapper;


import br.com.dowglasmaia.openapi.model.RequestResponse;
import com.dowglasmaia.autoatendimentoserviceapi.domain.enums.RequestType;
import com.dowglasmaia.autoatendimentoserviceapi.domain.model.Request;

import java.util.UUID;

public class RequestMapper {

    public static Request toRequestDomain(br.com.dowglasmaia.openapi.model.Request request){
        return new Request(
              UUID.randomUUID().toString(),
              RequestType.valueOf(request.getSubject().name()),
              request.getCustomerId()
        );
    }

    public static RequestResponse toRequestResponse(Request request){
        RequestResponse response = new RequestResponse();
        response.setRequestId(request.id());
        return response;
    }
}
