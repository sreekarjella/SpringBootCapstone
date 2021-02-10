package com.eatz.customerservice.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtFilter extends GenericFilterBean {

	private ObjectMapper mapper;

	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		final String authHeader = request.getHeader("authorization");

		if ("OPTIONS".equals(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);

			chain.doFilter(req, res);
		} else {

			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				response.setContentType("application/json");
				PrintWriter writer = response.getWriter();
				if (mapper == null) {
					ServletContext ctxt = request.getServletContext();
					WebApplicationContext appCtxt = WebApplicationContextUtils.getWebApplicationContext(ctxt);
					mapper = appCtxt.getBean(ObjectMapper.class);
				}
				writer.print(mapper.writeValueAsString("Missing or invalid Authorization header"));
				writer.flush();
				return;
			}

			final String token = authHeader.substring(7);

			try {
				final Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();

				request.setAttribute("claims", claims);
			} catch (Exception e) {
				response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				response.setContentType("application/json");
				PrintWriter writer = response.getWriter();
				if (mapper == null) {
					ServletContext ctxt = request.getServletContext();
					WebApplicationContext appCtxt = WebApplicationContextUtils.getWebApplicationContext(ctxt);
					mapper = appCtxt.getBean(ObjectMapper.class);
				}
				writer.print(mapper.writeValueAsString(e.getMessage()));
				writer.flush();
				return;
			}

			chain.doFilter(req, res);
		}
	}

}
