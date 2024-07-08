package com.cuentas.movimiento.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoCuentaDTO {
	
    @NotBlank(message = "La fecha no puede estar vacía")
    private String fecha;

    @NotBlank(message = "El cliente no puede estar vacío")
    private String cliente;

    @NotBlank(message = "El número de cuenta no puede estar vacío")
    private String numeroCuenta;

    @NotBlank(message = "El tipo no puede estar vacío")
    private String tipo;

    @NotNull(message = "El saldo inicial no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo inicial debe ser mayor o igual a 0")
    private BigDecimal saldoInicial;

    @NotNull(message = "El estado no puede ser nulo")
    private Boolean estado;

    @NotNull(message = "El movimiento no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El movimiento debe ser mayor que 0")
    private BigDecimal movimiento;

    @NotNull(message = "El saldo disponible no puede ser nulo")
    private BigDecimal saldoDisponible;

}
