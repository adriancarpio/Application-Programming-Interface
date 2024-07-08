package com.persona.cliente.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoCuentaDTO {
	
	@NotNull(message = "El id no puede ser nulo")
    @Min(value = 1, message = "El id debe ser mayor o igual a 1")
    private Long id;
    
    @NotBlank(message = "La contraseña no puede estar en blanco")
    @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
    private String contrasena;

    @NotBlank(message = "La fecha no puede estar en blanco")
    private String fecha;

    @NotBlank(message = "El cliente no puede estar en blanco")
    private String cliente;

    @NotBlank(message = "El número de cuenta no puede estar en blanco")
    private String numeroCuenta;

    @NotBlank(message = "El tipo no puede estar en blanco")
    private String tipo;

    @NotNull(message = "El saldo inicial no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El saldo inicial debe ser mayor que 0")
    private BigDecimal saldoInicial;

    @NotNull(message = "El estado no puede ser nulo")
    private Boolean estado;

    @NotNull(message = "El movimiento no puede ser nulo")
    private BigDecimal movimiento;

    @NotNull(message = "El saldo disponible no puede ser nulo")
    private BigDecimal saldoDisponible;

}
