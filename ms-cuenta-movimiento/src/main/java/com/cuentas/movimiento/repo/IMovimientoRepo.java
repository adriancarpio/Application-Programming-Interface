package com.cuentas.movimiento.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cuentas.movimiento.dto.MovimientoDTO;
import com.cuentas.movimiento.model.Movimiento;
import com.cuentas.movimiento.util.RespuestaGenerica;

public interface IMovimientoRepo extends IGenericRepo<Movimiento, Long>{

	@Query(value = "SELECT * FROM fn_movimientos_cuenta(:p_cliente, TO_DATE(:p_fecha_inicio, 'YYYY-MM-DD'), TO_DATE(:p_fecha_fin, 'YYYY-MM-DD'))", nativeQuery = true)
    List<Object[]> callFnMovimientosCuenta(@Param("p_cliente") String p_cliente, @Param("p_fecha_inicio") Date p_fecha_inicio, @Param("p_fecha_fin") Date p_fecha_fin);
    
	RespuestaGenerica<Movimiento> save(MovimientoDTO dto);

}
