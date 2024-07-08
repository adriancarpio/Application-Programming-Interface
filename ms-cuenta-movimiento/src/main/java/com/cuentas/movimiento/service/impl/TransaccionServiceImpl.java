package com.cuentas.movimiento.service.impl;

import org.springframework.stereotype.Service;

import com.cuentas.movimiento.model.Transaccion;
import com.cuentas.movimiento.repo.IGenericRepo;
import com.cuentas.movimiento.repo.ITransaccionRepo;
import com.cuentas.movimiento.service.ITransaccionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransaccionServiceImpl extends CRUDImpl<Transaccion, Long> implements ITransaccionService{
	
	private final ITransaccionRepo repo;

	@Override
	protected IGenericRepo<Transaccion, Long> getRepo() {
		return repo;
	}

}
