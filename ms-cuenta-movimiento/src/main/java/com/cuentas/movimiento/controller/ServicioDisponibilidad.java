package com.cuentas.movimiento.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cuentas.movimiento.util.RespuestaGenerica;

@RestController
@RequestMapping("/apiRest")
public class ServicioDisponibilidad {
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/sd.disponibilidad", produces = "application/json")
    public ResponseEntity<RespuestaGenerica> emisionComprobanteXmlPlus(HttpServletRequest request) {
        return new ResponseEntity<>(new RespuestaGenerica().respuestaExitosa(null, "OK " + request.getRequestURL() + " --> " + request.getRemoteAddr()), HttpStatus.OK);
    }
}
