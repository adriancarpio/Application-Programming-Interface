package com.persona.cliente.repo;

import org.springframework.data.jpa.repository.Query;

import com.persona.cliente.model.Cliente;

public interface IClienteRepo extends IGenericRepo<Cliente, Long> {
	
	@Query(value = """
	        SELECT count(1)
	        FROM public.clientes
	        WHERE nombre = :nombre
	        """, nativeQuery = true)
	Integer countClientesByNombre(String nombre);

}
