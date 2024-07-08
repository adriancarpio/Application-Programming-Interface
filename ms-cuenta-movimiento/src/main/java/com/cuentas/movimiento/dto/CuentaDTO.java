package com.cuentas.movimiento.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDTO {

    private Long id;

    @NotBlank(message = "El número de cuenta no puede estar en blanco")
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta no puede estar en blanco")
    private String tipoCuenta;

    @NotNull(message = "El saldo inicial no puede ser nulo")
    private BigDecimal saldoInicial;

    @NotNull(message = "El estado no puede ser nulo")
    private Boolean estado;

    @NotBlank(message = "El cliente no puede estar en blanco")
    private String cliente;
}
