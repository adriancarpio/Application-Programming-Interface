package com.cuentas.movimiento.dto;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDTO {


    private Long id;

    @NotBlank(message = "El número de cuenta no puede estar vacío")
    private String numeroCuenta;

    private Date fecha;

    @NotBlank(message = "El tipo de movimiento no puede estar vacío")
    private String tipoMovimiento;

    @NotNull(message = "El valor no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El valor debe ser mayor que 0")
    private BigDecimal valor;

    @NotNull(message = "El saldo no puede ser nulo")
    private BigDecimal saldo;
}
