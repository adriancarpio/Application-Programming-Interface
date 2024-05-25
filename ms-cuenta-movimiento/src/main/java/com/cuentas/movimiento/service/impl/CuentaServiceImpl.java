package com.cuentas.movimiento.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cuentas.movimiento.model.Cuenta;
import com.cuentas.movimiento.repo.ICuentaRepo;
import com.cuentas.movimiento.repo.IGenericRepo;
import com.cuentas.movimiento.service.ICuentaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl extends CRUDImpl<Cuenta, Long> implements ICuentaService{
	
	private final ICuentaRepo repo;
	
	@Override
	protected IGenericRepo<Cuenta, Long> getRepo() {
		return repo;
	}

	@Transactional
	@Override
	public Optional<Cuenta> edit(Long id, Cuenta obj) {
        return repo.findById(id).map(dto -> {
            if (obj.getId() == null) {
            	obj.setId(id);
            }
            
            if (obj.getNumeroCuenta() != null && !obj.getNumeroCuenta().isBlank()) {
            	obj.setNumeroCuenta(dto.getNumeroCuenta());
            }
            
            Optional.ofNullable(obj.getCliente()).ifPresent(dto::setCliente);
            Optional.ofNullable(obj.getEstado()).ifPresent(dto::setEstado);
            Optional.ofNullable(obj.getTipoCuenta()).ifPresent(dto::setTipoCuenta);
            Optional.ofNullable(obj.getSaldoInicial()).ifPresent(dto::setSaldoInicial);
            
            return repo.save(obj);
        });
	}

	@Override
	public Cuenta getCuentaByNumero(String numeroCuenta) {
		return repo.getCuentaByNumero(numeroCuenta);
	}
}
