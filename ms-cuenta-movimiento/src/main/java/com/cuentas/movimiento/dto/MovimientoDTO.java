package com.cuentas.movimiento.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDTO {

    private Long id;

    private String numeroCuenta;

    private Date fecha;

    private String tipoMovimiento;
    
    private BigDecimal valor;

    private BigDecimal saldo;
}
