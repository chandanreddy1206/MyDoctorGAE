package com.sspharma;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;
import com.sspharma.bean.Appointment;
import com.sspharma.bean.Doctor;
import com.sspharma.bean.User;
import com.sspharma.bean.VoiceMessage;

public class OfyRegistration implements ServletContextListener 
{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ObjectifyService.register(User.class);
		ObjectifyService.register(Doctor.class);
		ObjectifyService.register(VoiceMessage.class);
		ObjectifyService.register(Appointment.class);
	}

}
