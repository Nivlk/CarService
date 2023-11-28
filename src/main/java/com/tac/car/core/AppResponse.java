package com.tac.car.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppResponse {
    public String responseCode;
    public String responseMessage;
    public Object data;
    public boolean error; // Nuevo campo para indicar si hay un error

    public AppResponse() {
        responseCode = "OK";
        responseMessage = "OK";
        error = false; // Inicializamos el campo error como false por defecto
    }
}
