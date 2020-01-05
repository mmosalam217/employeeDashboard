package com.resources.IOContainer.core;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.resources.App;

@WebListener
public class IOContextListener implements ServletContextListener {

	public IOContextListener() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void contextInitialized(ServletContextEvent arg) {
	 System.out.println("Building IOContainerContext...");
	 Container.run(App.class);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg) {
	 
	}
}
