package com.cuentas.movimiento.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.cuentas.movimiento.model.Movimiento;

public interface IMovimientoService extends ICRUD<Movimiento, Long>{
	  Optional<Movimiento> edit(Long id, Movimiento obj);
	  
	  List<Object[]> callFnMovimientosCuenta(String p_cliente, Date p_fecha_inicio, Date p_fecha_fin);
}
