package com.cuentas.movimiento.service;

import java.util.Optional;

import com.cuentas.movimiento.model.Cuenta;


public interface ICuentaService extends ICRUD<Cuenta, Long>{
	
    Optional<Cuenta> edit(Long id, Cuenta obj);
    
    Cuenta getCuentaByNumero(String numeroCuenta);

}
