package com.persona.cliente.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoCuentaDTO {
	
	 private String fecha;
	 private String cliente;
	 private String numeroCuenta;
	 private String tipo;
	 private BigDecimal saldoInicial;
     private Boolean estado;
     private BigDecimal movimiento;
     private BigDecimal saldoDisponible;

}
