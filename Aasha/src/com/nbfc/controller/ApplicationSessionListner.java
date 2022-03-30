package com.nbfc.controller;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class ApplicationSessionListner implements HttpSessionListener{

	public void sessionCreated(HttpSessionEvent event) {
		// TODO Auto-generated method stub
		event.getSession().setMaxInactiveInterval(3600);
		
	}

	
	public void sessionDestroyed(HttpSessionEvent event) {
		// TODO Auto-generated method stub
		
	}

}
