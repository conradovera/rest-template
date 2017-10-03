package com.github.conradovera.mapper;

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
	
	@SuppressWarnings("unchecked")
	public ManejadorExcepcionImp() {
		// TODO Auto-generated constructor stub
		
		String sms3xx="La respuesta a la petición puede ser encontrada bajo otra URI, comunique con al  administrador del sistema";
		String sms4xx="La solicitud contiene sintaxis incorrecta o no puede procesarse";
		String sms5xx="Ocurrio un error interno en el servidor. Favor de comunicar con el administrador del sistema.";
				
		excepciones = new TreeMap<String, String>();
		excepciones.put(RuntimeException.class.getName(), "");
		excepciones.put(WebApplicationException.class.getName(), "Ocurrio un error al procesar la peticion");
		
		//ERRORES 3xx
		excepciones.put(RedirectionException.class.getName(), sms3xx);
				
		//ERRORES 4xx
		excepciones.put(ClientErrorException.class.getName(), sms4xx);
		excepciones.put(BadRequestException.class.getName(), "La solicitud contiene sintaxis errónea");
		excepciones.put(ForbiddenException.class.getName(), "El cliente no tiene los privilegios para realizar la peticion");
		excepciones.put(NotAcceptableException.class.getName(), "El servidor no es capaz de devolver los datos en ninguno de los formatos aceptados por el cliente");
		excepciones.put(NotAllowedException.class.getName(), "El metodo para acceder al recurso solicitado es incorrecto");
		excepciones.put(NotAuthorizedException.class.getName(), "El acceso al recurso no fue autorizado");
		excepciones.put(NotFoundException.class.getName(), "El recurso no fue encontrado");
		excepciones.put(NotSupportedException.class.getName(), "La petición del navegador tiene un formato no soportado por el servidor");		 
		
		//ERRORES 5xx
		excepciones.put(ServerErrorException.class.getName(), sms5xx);
		excepciones.put(InternalServerErrorException.class.getName(), sms5xx);
		excepciones.put(ServiceUnavailableException.class.getName(), sms5xx);
		
		//excepciones.put(ContabilidadBussinesExcepcion.class.getName(), "");
	}
	 
	
	@Override
	public Respuesta doResponse(Exception ex) {
	
		return this.obtenerExcepcion(ex);
	}
	
	@Override
	public Respuesta doResponse(Exception ex, String ipRequest) {
	
		return this.obtenerExcepcion(ex, ipRequest);
	}
	
	
	private Respuesta obtenerExcepcion(Exception ex){
		//log.info("===== Procesando la excepcion recibida =====> "+ex.getClass().getName());
		
		Integer status = 500;
		String sms ="Ocurrio un error interno en el servidor. Favor de comunicar con el administrador del sistema.";
		
		
		
//		if(ex instanceof ContabilidadBussinesExcepcion){
//			status = 422;
//			sms = ((ContabilidadBussinesExcepcion)ex).getMessage();
//			
//		}else if(ex instanceof NoResultBusinessException){
//			status = 404;
//			sms = ((NoResultBusinessException)ex).getMessage();
//		}
//		
//		//Recepciono y obtengo todas las excepciones que arrroja el jx-rs
//		else 
			
		if(ex instanceof WebApplicationException){
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
		
		return res;
	}


	
	private Respuesta obtenerExcepcion(Exception ex, String ipRequest){
		Respuesta  res;		
		String logs = (ipRequest.equals("localhost")||ipRequest.equals("127.0.0.1"))?ex.getMessage():"";
		
		res = this.obtenerExcepcion(ex);
		res.setLog(logs);
		
		return res;
	}


	
	@SuppressWarnings("rawtypes")
	public Map getExcepciones() {
		return excepciones;
	}

	@SuppressWarnings("unchecked")
	public void setExcepciones(@SuppressWarnings("rawtypes") Map excepciones) {
		this.excepciones = excepciones;
	}


}
