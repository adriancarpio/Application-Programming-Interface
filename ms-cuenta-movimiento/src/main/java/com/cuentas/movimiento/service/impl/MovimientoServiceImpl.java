package com.cuentas.movimiento.service.impl;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.math.BigDecimal;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cuentas.movimiento.dto.CuentaDTO;
import com.cuentas.movimiento.dto.EstadoCuentaDTO;
import com.cuentas.movimiento.dto.MovimientoDTO;
import com.cuentas.movimiento.dto.TransaccionDTO;
import com.cuentas.movimiento.model.Movimiento;
import com.cuentas.movimiento.repo.IGenericRepo;
import com.cuentas.movimiento.repo.IMovimientoRepo;
import com.cuentas.movimiento.service.IMovimientoService;
import com.cuentas.movimiento.util.RespuestaGenerica;
import com.cuentas.movimiento.util.Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl extends CRUDImpl<Movimiento, Long> implements IMovimientoService {

	private final IMovimientoRepo repo;
	private final Utils util;
	private final CuentaServiceImpl serviceCuenta;
	private final TransaccionServiceImpl serviceTransaccion;

	@Override
	protected IGenericRepo<Movimiento, Long> getRepo() {
		return repo;
	}
	
	@Transactional
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
	public ResponseEntity<List<EstadoCuentaDTO>> callFnMovimientosCuenta(String p_cliente, String p_fecha_inicio, String p_fecha_fin) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date fechaInicioDate = null;
			Date fechaFinDate = null;

			if (p_fecha_inicio != null && !p_fecha_inicio.isEmpty()) {
				fechaInicioDate = formatter.parse(p_fecha_inicio);
			}
			if (p_fecha_fin != null && !p_fecha_fin.isEmpty()) {
				fechaFinDate = formatter.parse(p_fecha_fin);
			}

			List<Object[]> ltsCuenta = repo.callFnMovimientosCuenta(p_cliente, fechaInicioDate, fechaFinDate);
			List<EstadoCuentaDTO> estadoCuentas = ltsCuenta.stream().map(result -> {
			    EstadoCuentaDTO estadoCuenta = new EstadoCuentaDTO();
			    estadoCuenta.setFecha((String) result[0]);
			    estadoCuenta.setCliente((String) result[1]);
			    estadoCuenta.setNumeroCuenta((String) result[2]);
			    estadoCuenta.setTipo((String) result[3]);
			    estadoCuenta.setSaldoInicial((BigDecimal) result[4]);
			    estadoCuenta.setEstado((Boolean) result[5]);
			    estadoCuenta.setMovimiento((BigDecimal) result[6]);
			    estadoCuenta.setSaldoDisponible((BigDecimal) result[7]);
			    return estadoCuenta;
			}).collect(Collectors.toList());
			
			if(estadoCuentas != null && estadoCuentas.size() > 0) {
				return new ResponseEntity<>(estadoCuentas, OK);
			}
			return new ResponseEntity<>(estadoCuentas, NO_CONTENT);
		} catch (ParseException e) {
			return new ResponseEntity<>(null, NOT_FOUND);
		}
	}
	
	@Transactional
	@Override
	public RespuestaGenerica<Movimiento> save(MovimientoDTO dto, HttpServletRequest request) {
		BigDecimal valorRetiro = null;
		BigDecimal valorDeposito = null;
		Date fechaRegistro = new Date();
		String msj = "";
		String ipOrigen = request.getRemoteAddr(); 
		long tiempoActual = System.currentTimeMillis();
		Date fechaActual = new Date(tiempoActual);
	    TransaccionDTO transaccion = new TransaccionDTO();
        transaccion.setNumeroCuenta(dto.getNumeroCuenta());
        transaccion.setFecha(fechaRegistro);
        transaccion.setTipoMovimiento(dto.getTipoMovimiento());
        transaccion.setValor(dto.getValor());

		if (!dto.getNumeroCuenta().isBlank()) {
			CuentaDTO obj = util.convertToDtoCuenta(serviceCuenta.getCuentaByNumero(dto.getNumeroCuenta()));
			if (obj != null) {
				if (obj.getSaldoInicial().compareTo(dto.getValor()) < 0 && dto.getTipoMovimiento().equals("Retiro")) {
					return new RespuestaGenerica().respuestaError(null, "Saldo no disponible");
				}
				
		        transaccion.setSaldoInicial(obj.getSaldoInicial());
		        transaccion.setSaldoDisponible(obj.getSaldoInicial());
		        transaccion.setIpOrigen(ipOrigen);
		        transaccion.setRequestJson(dto.toString());

				dto.setSaldo(obj.getSaldoInicial());
				dto.setFecha(fechaActual);
				Movimiento movimiento = this.save(util.convertToEntityMovimiento(dto));

				if (movimiento != null) {
					msj = dto.getTipoMovimiento();
					if (dto.getTipoMovimiento().equals("Retiro")) {
						valorRetiro = obj.getSaldoInicial().subtract(dto.getValor());
						obj.setSaldoInicial(valorRetiro);
					}

					if (dto.getTipoMovimiento().equals("Deposito")) {
						valorDeposito = obj.getSaldoInicial().add(dto.getValor());
						obj.setSaldoInicial(valorDeposito);
					}
					serviceCuenta.edit(obj.getId(), util.convertToEntityCuenta(obj));
					URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
							.buildAndExpand(movimiento.getId()).toUri();
					
					transaccion.setResponseJson(new RespuestaGenerica().respuestaExitosa(movimiento, msj + " realizado con exitoso").toString());
					serviceTransaccion.save(util.convertToEntityTransaccion(transaccion));
					return new RespuestaGenerica().respuestaExitosa(movimiento, msj + " realizado con exitoso");
				}
			}
			return new RespuestaGenerica().respuestaError(null, "Datos invalidos");
		}
		return new RespuestaGenerica().respuestaError(null, "Ingrese un numero de cuenta valido");
	}
}
