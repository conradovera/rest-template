package com.github.conradovera.filtros;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/servicios/*")
public class AccesoRecursoOrigenCruzado implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		/** para el cors **/
		addHeadersCors(httpResponse);// para el cors
		if (httpRequest.getMethod().equals("OPTIONS")) {
			return;
		}

		chain.doFilter(request, response);

	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	/**
	 * Agrega los cabezados que pide el cors maldito sean jaja XD
	 * 
	 * @param httpResponse
	 */
	private void addHeadersCors(HttpServletResponse httpResponse) {
		httpResponse.addHeader("Access-Control-Allow-Origin", "*");
		httpResponse.addHeader("Access-Control-Allow-Headers", "origin, content-type");
		httpResponse.addHeader("Access-Control-Allow-Credentials", "true");
		httpResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
	}

}
