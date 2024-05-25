package com.cuentas.movimiento.repo;

import org.springframework.data.jpa.repository.Query;

import com.cuentas.movimiento.model.Cuenta;

public interface ICuentaRepo extends IGenericRepo<Cuenta, Long>{
	
	@Query(value = """
			SELECT id, estado, numero_cuenta, saldo_inicial, tipo_cuenta, cliente
	               FROM public.cuentas where numero_cuenta = :numeroCuenta
			""", nativeQuery = true)
	Cuenta getCuentaByNumero(String numeroCuenta);
}
