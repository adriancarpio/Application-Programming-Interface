package com.cuentas.movimiento.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cuentas.movimiento.dto.CuentaDTO;
import com.cuentas.movimiento.dto.EstadoCuentaDTO;
import com.cuentas.movimiento.dto.MovimientoDTO;
import com.cuentas.movimiento.model.Movimiento;
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

import com.cuentas.movimiento.service.impl.CuentaServiceImpl;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import com.cuentas.movimiento.service.impl.MovimientoServiceImpl;
import com.cuentas.movimiento.util.RespuestaGenerica;
import com.cuentas.movimiento.util.Utils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

	private final MovimientoServiceImpl service;

	private final CuentaServiceImpl serviceCuenta;

	private final Utils util;

	@PostMapping
	public RespuestaGenerica<Movimiento> save(@Valid @RequestBody MovimientoDTO dto) {
		BigDecimal valorRetiro = null;
		BigDecimal valorDeposito = null;
		String msj = "";
		long tiempoActual = System.currentTimeMillis();
		Date fechaActual = new Date(tiempoActual);

		if (!dto.getNumeroCuenta().isBlank()) {
			CuentaDTO obj = util.convertToDtoCuenta(serviceCuenta.getCuentaByNumero(dto.getNumeroCuenta()));
			if (obj != null) {
				if (obj.getSaldoInicial().compareTo(dto.getValor()) < 0 && dto.getTipoMovimiento().equals("Retiro")) {
					return new RespuestaGenerica().respuestaError(null, "Saldo no disponible");
				}

				dto.setSaldo(obj.getSaldoInicial());
				dto.setFecha(fechaActual);
				Movimiento movimiento = service.save(util.convertToEntityMovimiento(dto));

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
					return new RespuestaGenerica().respuestaExitosa(movimiento, msj + " realizado con exitoso");
				}
			}
			return new RespuestaGenerica().respuestaError(null, "Datos invalidos");
		}
		return new RespuestaGenerica().respuestaError(null, "Ingrese un numero de cuenta valido");
	}

	@PutMapping("/{id}")
	public ResponseEntity<MovimientoDTO> update(@PathVariable("id") Long id, @Valid @RequestBody MovimientoDTO dto) {
		dto.setId(id);
		Movimiento obj = service.update(util.convertToEntityMovimiento(dto), id);
		return new ResponseEntity<>(util.convertToDtoMovimiento(obj), OK);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<MovimientoDTO> edit(@PathVariable Long id, @RequestBody MovimientoDTO dto) {

		Optional<Movimiento> obj = service.edit(id, util.convertToEntityMovimiento(dto));
		if (obj.isPresent()) {
			Movimiento movimiento = obj.get();
			MovimientoDTO movimientoDTO = util.convertToDtoMovimiento(movimiento);
			return ResponseEntity.ok(movimientoDTO);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		service.delete(id);
		return new ResponseEntity<>(OK);
	}

	@GetMapping("/reportes")
	public ResponseEntity<List<EstadoCuentaDTO>> obtenerReportes(
			@RequestParam(name = "fechaInicio", required = false) String fechaInicio,
			@RequestParam(name = "fechaFin", required = false) String fechaFin,
			@RequestParam(name = "cliente", required = false) String cliente) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date fechaInicioDate = null;
			Date fechaFinDate = null;

			if (fechaInicio != null && !fechaInicio.isEmpty()) {
				fechaInicioDate = formatter.parse(fechaInicio);
			}
			if (fechaFin != null && !fechaFin.isEmpty()) {
				fechaFinDate = formatter.parse(fechaFin);
			}

			List<Object[]> ltsCuenta = service.callFnMovimientosCuenta(cliente, fechaInicioDate, fechaFinDate);
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
}
