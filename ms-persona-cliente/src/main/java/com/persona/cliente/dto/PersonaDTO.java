package com.persona.cliente.dto;


import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class PersonaDTO {
	
	@NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El género no puede estar vacío")
    private String genero;

    @Min(value = 0, message = "La edad no puede ser negativa")
    private int edad;

    @NotBlank(message = "La identificación no puede estar vacía")
    private String identificacion;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String direccion;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "\\d+", message = "El teléfono debe contener solo números")
    @Size(max = 20, message = "El telefono debe maximo 10 caracteres")
    private String telefono;
    
}
