package com.resources.store.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {

	public User() {
		// TODO Auto-generated constructor stub
	}
	
	private String id;
	private String firstName;
	private String lastName;
	private String displayName;
	private String email;
	private String password;
	private List<String> rolesAsList = new ArrayList<String>();
	private String roles;
	
	public User(String id, String firstName, String lastName, String displayName, String email, String password,
			String roles) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.displayName = displayName;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDisplayName() {
		this.displayName = this.firstName + " " + this.lastName;
		return this.displayName;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getRolesAsList() {
		return rolesAsList;
	}
	
	public void addRole(String role) {
		this.rolesAsList.add(role);
	}
	
	public void setRolesAsList() {
		String[] rolesAsString = this.roles.split(",");
		this.rolesAsList = Arrays.asList(rolesAsString);
		
	}
	public String getRoles() {
		return roles;
	}
	
	public void setRoles(String roles) {
		this.roles = roles;
	}

	
	

}
