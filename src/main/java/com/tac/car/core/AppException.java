package com.tac.car.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppException extends Throwable {
    public String responseCode;
    public String responseMessage;
    public Object data;

    public AppException() {
        responseCode = "NOK";
        responseMessage = "NOK";
    }

}
