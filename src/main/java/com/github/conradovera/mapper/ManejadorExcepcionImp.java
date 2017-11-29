package com.github.conradovera.mapper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.TreeMap;
//import org.jboss.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.WebApplicationException;

public class ManejadorExcepcionImp implements ManejadorExcepcion{
	
	private Map<String, String> excepciones ;
//	private Logger log = Logger.getLogger(ManejadorExcepcionImp.class.getName());
	
	public ManejadorExcepcionImp() {
		// TODO Auto-generated constructor stub
		
		String sms3xx="La respuesta a la petici\u00F3n puede ser encontrada bajo otra URI, comun\u00EDquese con el administrador del sistema";
		String sms4xx="La solicitud contiene sintaxis incorrecta o no puede procesarse";
		String sms5xx="Ocurri\u00F3 un error interno en el servidor. Favor de comunicarse con el administrador del sistema.";
				
		excepciones = new TreeMap<String, String>();
		excepciones.put(RuntimeException.class.getName(), "");
		excepciones.put(WebApplicationException.class.getName(), "Ocurri\u00F3 un error al procesar la petici\u00F3n");
		
		//ERRORES 3xx
		excepciones.put(RedirectionException.class.getName(), sms3xx);
				
		//ERRORES 4xx
		excepciones.put(ClientErrorException.class.getName(), sms4xx);
		excepciones.put(BadRequestException.class.getName(), "La solicitud contiene sintaxis err\u00F3nea");
		excepciones.put(ForbiddenException.class.getName(), "El cliente no tiene los privilegios para realizar la petici\u00F3n");
		excepciones.put(NotAcceptableException.class.getName(), "El servidor no es capaz de devolver los datos en alguno de los formatos aceptados por el cliente");
		excepciones.put(NotAllowedException.class.getName(), "El m\u00E9todo para acceder al recurso solicitado es incorrecto");
		excepciones.put(NotAuthorizedException.class.getName(), "El acceso al recurso no fue autorizado");
		excepciones.put(NotFoundException.class.getName(), "El recurso no fue encontrado");
		excepciones.put(NotSupportedException.class.getName(), "La petici\u00F3n del navegador tiene un formato no soportado por el servidor");		 
		
		//ERRORES 5xx
		excepciones.put(ServerErrorException.class.getName(), sms5xx);
		excepciones.put(InternalServerErrorException.class.getName(), sms5xx);
		excepciones.put(ServiceUnavailableException.class.getName(), sms5xx);
		
		//excepciones.put(ContabilidadBussinesExcepcion.class.getName(), "");
	}
	 
	//@Override
	public Respuesta doResponse(Exception ex) {
	
		return this.obtenerExcepcion(ex,new ManejadorExcepcionDefault());
		
	}
	
	//@Override
	public Respuesta doResponse(Exception ex, ManejadorExcepcionNegocio manejadorExcepcionNegocio) {
	
		return this.obtenerExcepcion(ex, manejadorExcepcionNegocio);
		
	}
		
	private Respuesta obtenerExcepcion(Exception ex,ManejadorExcepcionNegocio manejadorExcepcionNegocio){
		//log.info("===== Procesando la excepcion recibida =====> "+ex.getClass().getName());
		
		Integer status = 500;
		String sms ="Ocurrio un error interno en el servidor. Favor de comunicar con el administrador del sistema.";
		
		
		
		if(ex instanceof ExcepcionNegocio){
			manejadorExcepcionNegocio.manejarExcepcionNegocio((ExcepcionNegocio) ex);
		//Recepciono y obtengo todas las excepciones que arrroja el jx-rs
		}else if(ex instanceof WebApplicationException){
					status = ((WebApplicationException)ex).getResponse().getStatus();
					sms = (String) excepciones.get(ex.getClass().getName());
					ex.printStackTrace();//aqui igual debe pintar el stacktrace
		}
		
		else{//excepciones no contenidas en el estandar ni en politicas deben ser errorres 
			//primer parche mal hecho XD
			
			ex.printStackTrace();
		}
			
		
		//log.error("Status generado: "+status+"---> Detalle del error: "+ex.getMessage());
		Respuesta  res = new Respuesta();
		res.setMensaje(sms);
		res.setStatus(status);
		//https://stackoverflow.com/a/4812589
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		res.setLog(errors.toString());
		return res;
	}

	/*private Respuesta obtenerExcepcion(Exception ex, String ipRequest){
		Respuesta  res;		
		String logs = (ipRequest.equals("localhost")||ipRequest.equals("127.0.0.1"))?ex.getMessage():"";
		
		res = this.obtenerExcepcion(ex);
		res.setLog(logs);
		
		return res;
	}*/
	
	@SuppressWarnings("rawtypes")
	public Map getExcepciones() {
		return excepciones;
	}

	@SuppressWarnings("unchecked")
	public void setExcepciones(@SuppressWarnings("rawtypes") Map excepciones) {
		this.excepciones = excepciones;
	}


}
