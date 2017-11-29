package com.github.conradovera.mapper;

//import javax.ws.rs.core.Response.Status;

public class ManejadorExcepcionDefault implements ManejadorExcepcionNegocio{

	public Respuesta manejarExcepcionNegocio(ExcepcionNegocio excepcionNegocio) {
		// TODO Auto-generated method stub
		Integer status = 422;
		String sms = excepcionNegocio.getLocalizedMessage();
		
		Respuesta  res = new Respuesta();
		res.setMensaje(sms);
		res.setStatus(status);
		return res;
	}

}
