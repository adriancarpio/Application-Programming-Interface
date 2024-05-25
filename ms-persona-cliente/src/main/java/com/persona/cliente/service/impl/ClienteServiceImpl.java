package com.persona.cliente.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.persona.cliente.dto.EstadoCuentaDTO;
import com.persona.cliente.model.Cliente;
import com.persona.cliente.repo.IClienteRepo;
import com.persona.cliente.repo.IGenericRepo;
import com.persona.cliente.service.IClienteService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl extends CRUDImpl<Cliente, Long> implements IClienteService {
	
	private final IClienteRepo repo;
	
	private final WebClient.Builder webClientBuilder;

	
	@Override
	protected IGenericRepo<Cliente, Long> getRepo() {
		return repo;
	}
	
	@Transactional
	@Override
	public Optional<Cliente> edit(Long id, Cliente obj) {
        return repo.findById(id).map(dto -> {
            if (obj.getId() == null) {
            	obj.setId(id);
            }
            
            if (obj.getNombre() != null && !obj.getNombre().isBlank()) {
            	obj.setNombre(dto.getNombre());
            }
            
            Optional.ofNullable(obj.getDireccion()).ifPresent(dto::setDireccion);
            Optional.ofNullable(obj.getIdentificacion()).ifPresent(dto::setIdentificacion);
            Optional.ofNullable(obj.getGenero()).ifPresent(dto::setGenero);
            Optional.ofNullable(obj.getEdad()).ifPresent(dto::setEdad);
            Optional.ofNullable(obj.getTelefono()).ifPresent(dto::setTelefono);
            Optional.ofNullable(obj.getContrasena()).ifPresent(dto::setContrasena);
            Optional.ofNullable(obj.getEstado()).ifPresent(dto::setEstado);
            
            return repo.save(obj);
        });
	}

	@Override
	public Mono<List<EstadoCuentaDTO>> obtenerReportes(String fechaInicio, String fechaFin, String cliente) {

	    return webClientBuilder.build()//localhost ms-2
	            .get()
	            .uri("http://localhost:9596/api/v1/movimientos/reportes?cliente=" + cliente + "&fechaInicio=" + fechaInicio + "&fechaFin=" + fechaFin)
	            .retrieve()
	            .bodyToFlux(EstadoCuentaDTO.class)
	            .collectList()
	            .flatMap(estadoCuentas -> {
	                if (estadoCuentas.isEmpty()) {
	                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente no válido"));
	                }
	                return Mono.just(estadoCuentas);
	            })
	            .onErrorResume(WebClientResponseException.class, ex ->
	                    Mono.error(new ResponseStatusException(ex.getStatusCode(), ex.getResponseBodyAsString()))
	            );
	}


	@Override
	public Integer countClientesByNombre(String nombre) {
		return repo.countClientesByNombre(nombre);
	}
}
