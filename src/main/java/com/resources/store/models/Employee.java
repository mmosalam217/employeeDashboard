package com.resources.store.models;

//import java.util.UUID;

public class Employee {
private String  id;
private String name;
private int age;
private String department;
private String title;
private int salary;
public Employee(String id, String name, int age, String department, String title, int salary) {
	super();
	this.id = id;
	this.name = name;
	this.age = age;
	this.department = department;
	this.title = title;
	this.salary = salary;
}

public Employee()
{
	
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public int getAge() {
	return age;
}

public void setAge(int age) {
	this.age = age;
}

public String getDepartment() {
	return department;
}

public void setDepartment(String department) {
	this.department = department;
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

public int getSalary() {
	return salary;
}

public void setSalary(int salary) {
	this.salary = salary;
}



}
