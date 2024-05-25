package com.cuentas.movimiento.controller;
import java.net.URI;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.cuentas.movimiento.dto.CuentaDTO;
import com.cuentas.movimiento.model.Cuenta;
import com.cuentas.movimiento.service.impl.CuentaServiceImpl;
import com.cuentas.movimiento.util.Utils;

//import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
//import static org.springframework.http.HttpStatus.NOT_FOUND;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {
	
	private final CuentaServiceImpl service;
	private final Utils util;
	
	@GetMapping("/{id}")
	public ResponseEntity<CuentaDTO> findById(@PathVariable("id") Long id) {
		Cuenta obj = service.findById(id);
		return new ResponseEntity<>(util.convertToDtoCuenta(obj), OK);
	}

	@PostMapping
	public ResponseEntity<Void> save(@Valid @RequestBody CuentaDTO dto) {
		Cuenta obj = service.save(util.convertToEntityCuenta(dto));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CuentaDTO> update(@PathVariable("id") Long id, @Valid @RequestBody CuentaDTO dto) {
		dto.setId(id);
		Cuenta obj = service.update(util.convertToEntityCuenta(dto), id);
		return new ResponseEntity<>(util.convertToDtoCuenta(obj), OK);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<CuentaDTO> edit(@PathVariable Long id, @RequestBody CuentaDTO dto) {

		Optional<Cuenta> obj = service.edit(id, util.convertToEntityCuenta(dto));
		if (obj.isPresent()) {
			Cuenta cuenta = obj.get();
			CuentaDTO cuentaDTO = util.convertToDtoCuenta(cuenta);
            return ResponseEntity.ok(cuentaDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		service.delete(id);
		return new ResponseEntity<>(OK);
	}
}
