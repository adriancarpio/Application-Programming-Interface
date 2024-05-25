package com.persona.cliente;

import com.intuit.karate.junit5.Karate;

class MsPersonaClienteApplicationTests {

	   @Karate.Test
	    Karate testReportesIntegracion() {
	        return Karate.run("classpath:Karate/reportes.feature").relativeTo(getClass());
	    }
	   
	   @Karate.Test
	    Karate testGetCliente() {
	        return Karate.run("classpath:Karate/cliente.feature").relativeTo(getClass());
	    }

}
