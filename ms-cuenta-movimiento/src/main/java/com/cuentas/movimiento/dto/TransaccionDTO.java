package com.cuentas.movimiento.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionDTO {

	    private Long id;

	    private String numeroCuenta;

	    private Date fecha;

	    private String tipoMovimiento;

	    private BigDecimal valor;

	    private BigDecimal saldoInicial;

	    private BigDecimal saldoDisponible;
	    
	    private String ipOrigen;

	    private String requestJson;

	    private String responseJson;

}
