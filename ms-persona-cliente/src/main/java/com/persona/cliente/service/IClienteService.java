package com.persona.cliente.service;

import java.util.List;
import java.util.Optional;
import com.persona.cliente.dto.EstadoCuentaDTO;
import com.persona.cliente.model.Cliente;

import reactor.core.publisher.Mono;

public interface IClienteService extends ICRUD<Cliente, Long>{
	
    Optional<Cliente> edit(Long id, Cliente obj);
	Mono<List<EstadoCuentaDTO>> obtenerReportes(String fechaInicio, String fechaFin, String cliente);
	Integer countClientesByNombre(String nombre);
}
