package com.sspharma.bean;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
@Entity
public class Appointment implements Serializable 
{
	@Id
	private String appointmentId;
	@Index
	private String userEmail;
	@Index
	private String doctorEmail;
	@Index
	private Date time;
	
	
	public Appointment() {
	}


	public Appointment(String appointmentId, String userEmail, String doctorEmail, Date time) {
		super();
		this.appointmentId = appointmentId;
		this.userEmail = userEmail;
		this.doctorEmail = doctorEmail;
		this.time = time;
	}


	public Appointment(String userEmail, String doctorEmail, Date time) {
		super();
		this.userEmail = userEmail;
		this.doctorEmail = doctorEmail;
		this.time = time;
	}

	public String getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getUserEmail() {
		return userEmail;
	}


	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}


	public String getDoctorEmail() {
		return doctorEmail;
	}


	public void setDoctorEmail(String doctorEmail) {
		this.doctorEmail = doctorEmail;
	}


	public Date getTime() {
		return time;
	}


	public void setTime(Date time) {
		this.time = time;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Appointment [appointmentId=").append(appointmentId).append(", userEmail=").append(userEmail)
				.append(", doctorEmail=").append(doctorEmail).append(", time=").append(time).append("]");
		return builder.toString();
	}
	@Override
	public boolean equals(Object object) {
		// TODO Auto-generated method stub
		if (object instanceof Appointment) {
			Appointment user = (Appointment) object;
			if (user.getAppointmentId().equalsIgnoreCase(this.appointmentId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return appointmentId.hashCode();
	}
}
