package com.cuentas.movimiento.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
public class RespuestaGenerica<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int codigo;
    private String mensaje;
    @JsonInclude(value = Include.NON_EMPTY)
    private T respuesta;

    public RespuestaGenerica<T> respuestaExitosa(T respuesta,String msj) {
        this.codigo= HttpStatus.OK.value();
        this.mensaje=msj;
        this.respuesta = respuesta;
        return this;
    }

    public RespuestaGenerica<T> respuestaError(T respuesta,String msj) {
        this.codigo= HttpStatus.NOT_FOUND.value();
        this.mensaje=msj;
        this.respuesta = respuesta;
        return this;
    }   
}
