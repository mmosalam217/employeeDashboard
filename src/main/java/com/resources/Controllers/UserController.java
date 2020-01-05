package com.resources.Controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resources.CustomExceptions.UserNotFoundException;
import com.resources.IOContainer.annotations.Inject;
import com.resources.store.Services.UserService;
import com.resources.store.models.User;

/**
 * Servlet implementation class UserController
 */
@WebServlet("/api/v1/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/*
	@Inject()
	private UserService userService;
	*/
    UserService userService = new UserService();   
 
    public UserController() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		ObjectMapper mapper = new ObjectMapper();
		PrintWriter out = response.getWriter();
		try {
			
			User user = userService.getUserById(request.getParameter("id"));
			if( user != null) {
				mapper.writeValue(out, user);
			}else {
				out.print("User Not Found");
			}
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			out.print(e.getLocalizedMessage());
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		ObjectMapper mapper = new ObjectMapper();
		
		User user = mapper.readValue(request.getInputStream(), User.class);
			if(user.getFirstName() != null && user.getLastName() != null &&
			   user.getPassword() != null && user.getRoles() != null && user.getEmail() != null ) {
				user.setId(UUID.randomUUID().toString());
				Boolean isSuccessful = userService.createUser(user);
				if(isSuccessful) {
					out.print("User Created Successfully");
				}else {
					out.print("Failed to create user!");

				}
			}
	}

}
