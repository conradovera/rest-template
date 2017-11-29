package com.github.conradovera.mapper;

public interface ManejadorExcepcion {
	
	Respuesta doResponse(Exception ex);
	Respuesta doResponse(Exception ex, ManejadorExcepcionNegocio manejadorExcepcionNegocio);

}
