package com.resources.Controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resources.CustomExceptions.BadCredentialsException;
import com.resources.auth.AuthenticationSessionDetails;
import com.resources.auth.SimpleAuthCredentials;
import com.resources.store.Services.UserService;

@WebServlet("/api/v1/auth/LDAP")
public class AuthenticationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserService userService = new UserService();
    public AuthenticationController() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		ObjectMapper mapper = new ObjectMapper();
		PrintWriter out = response.getWriter();
		SimpleAuthCredentials credentials = mapper.readValue(request.getInputStream(), SimpleAuthCredentials.class);
		try {
			AuthenticationSessionDetails authDetails = userService.authenticate(credentials);
			if(authDetails != null) {
				HttpSession session = request.getSession(true);
				//Date creationDate = new Date(session.getCreationTime());
				//Date lastAccessTime = new Date(session.getLastAccessedTime());
				int visitsCount = 0;
				
				if(!session.isNew()) {
					visitsCount += 1;
					session.setAttribute("visitsCount", visitsCount);                       
				}else {
					session.setAttribute("userID", authDetails.getUser_id());
					session.setAttribute("roles", authDetails.getRoles());
					session.setAttribute("visitsCount", visitsCount);
					session.setMaxInactiveInterval(300000);

				}
				
				out.print("You have been authenticated successfully.");
			}
		} catch (BadCredentialsException e) {
			// TODO Auto-generated catch block
			out.print(e.getLocalizedMessage());
		}
	}

}
