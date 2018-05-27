package com.ramit.demosecurity.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthEntryPoint extends BasicAuthenticationEntryPoint{
	
	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
      throws IOException, ServletException {
        response.addHeader("WWW-Authenticate", "Basic realm=" +getRealmName());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter(); // to send the response back to client
        writer.println("HTTP Basic Auth Status 401 - " + authEx.getMessage());
    }

	@Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("RamitAuthRealm");
        super.afterPropertiesSet();
    }
}
