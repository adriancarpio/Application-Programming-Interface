package com.cuentas.movimiento.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.cuentas.movimiento.dto.EstadoCuentaDTO;
import com.cuentas.movimiento.dto.MovimientoDTO;
import com.cuentas.movimiento.model.Movimiento;
import com.cuentas.movimiento.util.RespuestaGenerica;

import jakarta.servlet.http.HttpServletRequest;

public interface IMovimientoService extends ICRUD<Movimiento, Long>{
	  Optional<Movimiento> edit(Long id, Movimiento obj);
	  
	  ResponseEntity<List<EstadoCuentaDTO>> callFnMovimientosCuenta(String p_cliente, String p_fecha_inicio, String p_fecha_fin);
	  
	  RespuestaGenerica<Movimiento> save(MovimientoDTO dto, HttpServletRequest request);
}
