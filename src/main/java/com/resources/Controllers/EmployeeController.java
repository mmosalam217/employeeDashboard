package com.resources.Controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resources.CustomExceptions.EmployeeNotFoundException;
import com.resources.store.Services.EmployeeService;
import com.resources.store.models.Employee;

/**
 * Servlet implementation class EmployeeController
 */
@WebServlet("/api/v1/employee")
public class EmployeeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private EmployeeService employeeService = new EmployeeService();
    
    public EmployeeController() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Employee emp;
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		try {
			emp = employeeService.getEmployeeById(request.getParameter("id"));
		
			mapper.writeValue(out, emp);
		} catch (EmployeeNotFoundException e) {
			e.printStackTrace();
			out.print(e.getLocalizedMessage());
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		//request.getReader().lines().collect(Collectors.joining(System.lineSeparator()
		Employee emp = mapper.readValue(request.getInputStream(), Employee.class);
		if(emp.getName() != null && emp.getDepartment() != null
			&& emp.getAge() != 0 && emp.getSalary() != 0 && emp.getTitle() != null) {
			emp.setId(UUID.randomUUID().toString());
			Boolean success = employeeService.createEmployee(emp);
			if(success) {
				out.print("Employee added successfully.");
				out.close();
			}else {
				out.print("Could Not add Employee");
				out.close();
			}
		}else {
			out.print("Body Dosen\'t contain a valid employee properties");
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Boolean isUpdated = employeeService.updateEmployee(request.getParameter("id"), request.getInputStream());
		if(isUpdated) {
			out.print("Employee updated Successfully");
		}else {
			out.print("Failed to update employee");
		}

	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Boolean isDeleted = employeeService.deleteEmployee(request.getParameter("id"));
		if(isDeleted) {
			out.print("Employee Deleted Successfully.");
		}else {
			out.print("Failed to Delete Employee.");

		}
	}

}
