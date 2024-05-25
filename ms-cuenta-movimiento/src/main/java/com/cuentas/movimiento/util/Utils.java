package com.cuentas.movimiento.util;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import com.cuentas.movimiento.dto.CuentaDTO;
import com.cuentas.movimiento.dto.MovimientoDTO;
import com.cuentas.movimiento.model.Cuenta;
import com.cuentas.movimiento.model.Movimiento;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class Utils {
	
	private final ModelMapper mapper;

	public  CuentaDTO convertToDtoCuenta(Cuenta obj) {
		return mapper.map(obj, CuentaDTO.class);
	}

	public  Cuenta convertToEntityCuenta(CuentaDTO dto) {
		return mapper.map(dto, Cuenta.class);
	}
	
	public MovimientoDTO convertToDtoMovimiento(Movimiento obj) {
		return mapper.map(obj, MovimientoDTO.class);
	}

	public Movimiento convertToEntityMovimiento(MovimientoDTO dto) {
		return mapper.map(dto, Movimiento.class);
	}

}
