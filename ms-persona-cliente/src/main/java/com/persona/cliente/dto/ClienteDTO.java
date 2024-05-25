package com.persona.cliente.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO extends PersonaDTO {
	
	 private Long id;
	 
	 private String contrasena;

     private Boolean estado;
}
