package com.tac.car.car;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppResponse {
    private String responseCode;
    private String responseMessage;
    private Object data;

    public AppResponse() {
        this.responseCode = "OK";
        this.responseMessage = "OK";
    }

    // Additional constructor for custom response
    public AppResponse(String responseCode, String responseMessage, Object data) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.data = data;
    }
}