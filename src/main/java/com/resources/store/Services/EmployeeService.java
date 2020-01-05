package com.resources.store.Services;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.resources.CustomExceptions.EmployeeNotFoundException;
import com.resources.store.ConnectionAdapter;
import com.resources.store.models.Employee;

public class EmployeeService {

	public EmployeeService() {
		// TODO Auto-generated constructor stub
	}
	
	
	// Abstract mapping the ResultSet into an Employee...
	public Employee getEmployeeFromResultSet(ResultSet rs){
		Employee emp = new Employee();

		try {
			if(!rs.isBeforeFirst()) {
				return null;
			}else {
				while(rs.next()) {
					emp.setId(rs.getString(1));
					emp.setName(rs.getString(2));
					emp.setAge(rs.getInt(3));
					emp.setDepartment(rs.getString(4));
					emp.setSalary(rs.getInt(5));
					emp.setTitle(rs.getString(6));
				}	
			}
			
				

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return emp;
	}
	
	
	// Abstract mapping the Employee into a Statement...
	public PreparedStatement createEmployeeStatement(PreparedStatement st, Employee emp) {
		try {
			st.setString(1, emp.getId());
			st.setString(2, emp.getName());
			st.setInt(3, emp.getAge());
			st.setString(4, emp.getDepartment());
			st.setInt(5, emp.getSalary());
			st.setString(6, emp.getTitle());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return st;
	}
	
	// GET Single Employee by id...
	public Employee getEmployeeById(String id) throws EmployeeNotFoundException{
		ConnectionAdapter adapter = new ConnectionAdapter();
		Connection conn = (Connection) adapter.initConnection();
		String SQL = "SELECT * FROM employee WHERE id = ?";
		Employee emp = null;
		try {
			PreparedStatement st = (PreparedStatement) conn.prepareStatement(SQL);
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			 emp = getEmployeeFromResultSet(rs);
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(emp == null) {
			throw new EmployeeNotFoundException("Employee Not Found");
		}
		return emp;

	}
	
	// Create Single Employee...
	public Boolean createEmployee(Employee emp) {
		Connection conn = null;
		ConnectionAdapter adapter = new ConnectionAdapter();
		conn =  (Connection) adapter.initConnection();
		String SQL = "INSERT INTO employee VALUES(?,?,?,?,?,?)";
		Boolean isSuccessful = null;
		try {
			PreparedStatement st = (PreparedStatement) conn.prepareStatement(SQL);
			createEmployeeStatement(st, emp);
			int result = st.executeUpdate();
			if(result == 0) {
				isSuccessful = false;
			}else {
				isSuccessful = true;
			}
			if(st != null) {
				st.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		return isSuccessful;

	}
	
	public Boolean updateEmployee(String id, ServletInputStream updates) {
		Boolean isSuccessful = false;
		ConnectionAdapter adapter = new ConnectionAdapter();
		Connection conn = (Connection) adapter.initConnection();
			try {
					Statement st = (Statement) conn.createStatement();
					String SQL = "UPDATE employee SET ";
					String queryBuilder = save(updates);
					String q = SQL + queryBuilder + " " + "WHERE id='" + id + "'";
					int updated = st.executeUpdate(q);
					if(updated > 0) {
						return true;
					}else {
						return false;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					try {
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
		
		return isSuccessful;
	}
	
	// construct dynamic part of update query...
	public String save(ServletInputStream updates) {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<String> query = new ArrayList<String>();
		
		try {
			// Map request body to a HashMap...
			@SuppressWarnings("unchecked")
			Map<String, Object> updateEmployee =  mapper.readValue(updates, HashMap.class);
			// check if the map contains an property of the entity class..
			Field [] entityFields = Employee.class.getDeclaredFields();
			for(Field field: entityFields) {
// if the property is found map them into a string representation of a column which should be updated..
				if(updateEmployee.get(field.getName()) != null) {
					StringBuilder queryConstructor = new StringBuilder();
					queryConstructor.append(field.getName());
					queryConstructor.append("=");
					queryConstructor.append(updateEmployee.get(field.getName()));
					query.add(queryConstructor.toString());

				}
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String finalQuery = String.join(", ", query);
		return finalQuery;
	}
	
	// Delete Employee by ID....
	public Boolean deleteEmployee(String id) {
		Boolean isDeleted = false;
		if(id != null || id != "") {
			ConnectionAdapter adapter = new ConnectionAdapter();
			Connection conn = (Connection) adapter.initConnection();
			String SQL = "DELETE FROM employee WHERE id = ?";
			try {
				PreparedStatement st = (PreparedStatement) conn.prepareStatement(SQL);
				st.setString(1, id);
				int result = st.executeUpdate();
				isDeleted = result > 0 ? true : false;
				if(st != null) st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}
		}
		
		return isDeleted;
	}
	
	public ArrayList<Employee> getEmployees(){
		ArrayList<Employee> employees = new ArrayList<Employee>();
		Connection conn = null;
		ConnectionAdapter adapter = new ConnectionAdapter();
		conn = (Connection) adapter.initConnection();
		try {
			java.sql.Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM employee");
			while(rs.next()) {
				employees.add(getEmployeeFromResultSet(rs));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return employees;
	}

}
