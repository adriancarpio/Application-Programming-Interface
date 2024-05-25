package com.persona.cliente.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;

import com.persona.cliente.dto.ClienteDTO;
import com.persona.cliente.dto.EstadoCuentaDTO;
import com.persona.cliente.model.Cliente;
import com.persona.cliente.service.impl.ClienteServiceImpl;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

	private final ModelMapper mapper;

	private final ClienteServiceImpl service;

	@GetMapping("/{id}")
	public ResponseEntity<ClienteDTO> findById(@PathVariable("id") Long id) {
		Cliente obj = service.findById(id);
		return new ResponseEntity<>(this.convertToDto(obj), OK);
	}

	@PostMapping
	public ResponseEntity<Void> save(@Valid @RequestBody ClienteDTO dto) {
		Cliente obj = service.save(convertToEntity(dto));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		service.delete(id);
		return new ResponseEntity<>(OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClienteDTO> update(@PathVariable("id") Long id, @Valid @RequestBody ClienteDTO dto) {
		dto.setId(id);
		Cliente obj = service.update(convertToEntity(dto), id);
		return new ResponseEntity<>(convertToDto(obj), OK);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ClienteDTO> edit(@PathVariable Long id, @RequestBody ClienteDTO dto) {

		Optional<Cliente> obj = service.edit(id, convertToEntity(dto));
		if (obj.isPresent()) {
            Cliente cliente = obj.get();
            ClienteDTO clienteDTO = convertToDto(cliente);
            return ResponseEntity.ok(clienteDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
	}
	
    @GetMapping("/reportes")
    public ResponseEntity<Mono<List<EstadoCuentaDTO>>> obtenerReportes(
    		@RequestParam(name = "fechaInicio", required = false) String fechaInicio,
			@RequestParam(name = "fechaFin", required = false) String fechaFin,
			@RequestParam(name = "cliente", required = false) String cliente) {
    	
    	try {
    		
    		if(!cliente.isBlank()) {
        		Integer existe = service.countClientesByNombre(cliente);
        		if(existe == 0) {
        			 return new ResponseEntity<>(null, NO_CONTENT);
        		}
    		}
        
	    	Mono<List<EstadoCuentaDTO>> estadoCuentaDTO = service.obtenerReportes(fechaInicio, fechaFin, cliente);
	    		    	
	        return new ResponseEntity<>(estadoCuentaDTO, OK);
        
		} catch (Exception e) {
			return new ResponseEntity<>(null, NOT_FOUND);
		}
    }

	private ClienteDTO convertToDto(Cliente obj) {
		return mapper.map(obj, ClienteDTO.class);
	}

	private Cliente convertToEntity(ClienteDTO dto) {
		return mapper.map(dto, Cliente.class);
	}

}
