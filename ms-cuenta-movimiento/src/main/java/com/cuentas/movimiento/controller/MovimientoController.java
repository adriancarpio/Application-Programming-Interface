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

import com.cuentas.movimiento.dto.EstadoCuentaDTO;
import com.cuentas.movimiento.dto.MovimientoDTO;
import com.cuentas.movimiento.model.Movimiento;
import static org.springframework.http.HttpStatus.OK;
import java.util.List;
import java.util.Optional;
import com.cuentas.movimiento.service.impl.MovimientoServiceImpl;
import com.cuentas.movimiento.util.RespuestaGenerica;
import com.cuentas.movimiento.util.Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

	private final MovimientoServiceImpl service;

	private final Utils util;

	@PostMapping
	public RespuestaGenerica<Movimiento> save(@Valid @RequestBody MovimientoDTO dto, HttpServletRequest request) {
		return service.save(dto,request);
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
		return service.callFnMovimientosCuenta(cliente, fechaInicio, fechaFin);
	}
}
