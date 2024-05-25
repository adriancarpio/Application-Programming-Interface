package com.cuentas.movimiento.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cuentas.movimiento.model.Movimiento;
import com.cuentas.movimiento.repo.IGenericRepo;
import com.cuentas.movimiento.repo.IMovimientoRepo;
import com.cuentas.movimiento.service.IMovimientoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl extends CRUDImpl<Movimiento, Long> implements IMovimientoService {

	private final IMovimientoRepo repo;


	@Override
	protected IGenericRepo<Movimiento, Long> getRepo() {
		return repo;
	}

	@Override
	public Optional<Movimiento> edit(Long id, Movimiento obj) {
		return repo.findById(id).map(dto -> {
			if (obj.getId() == null) {
				obj.setId(id);
			}

			if (obj.getNumeroCuenta() != null && !obj.getNumeroCuenta().isBlank()) {
				obj.setNumeroCuenta(dto.getNumeroCuenta());
			}

			Optional.ofNullable(obj.getFecha()).ifPresent(dto::setFecha);
			Optional.ofNullable(obj.getSaldo()).ifPresent(dto::setSaldo);
			Optional.ofNullable(obj.getTipoMovimiento()).ifPresent(dto::setTipoMovimiento);
			Optional.ofNullable(obj.getValor()).ifPresent(dto::setValor);

			return repo.save(obj);
		});
	}

	@Override
	public List<Object[]> callFnMovimientosCuenta(String p_cliente, Date p_fecha_inicio, Date p_fecha_fin) {
		return repo.callFnMovimientosCuenta(p_cliente, p_fecha_inicio, p_fecha_fin);
	}
}
