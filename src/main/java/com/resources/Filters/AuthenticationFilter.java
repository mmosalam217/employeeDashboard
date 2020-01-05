package com.resources.Filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import com.resources.CustomExceptions.MissingCredentialsException;

@WebFilter( filterName= "AuthenticationFilter", urlPatterns = {"/api/v1/employee"})
public class AuthenticationFilter implements Filter {
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		HttpSession session = req.getSession(false);
		
		if(session != null) {
			String userID = (String) session.getAttribute("userID");
			String roles = (String) session.getAttribute("roles");
			if(userID == null || roles == null) {
				session.setMaxInactiveInterval(0);
				res.sendError(403, "Please authenticate yourself");
				//out.print("Missing Credentials!");
			}else {
				if(roles.contains("admin")) {
					chain.doFilter(request, response);
				}else {
					res.sendError(403, "Access Denied");
					//out.print("Access Denied!");
				}
			}
		}else {
			res.sendError(403, "Please authenticate yourself");

		}
		
		
	}

}
