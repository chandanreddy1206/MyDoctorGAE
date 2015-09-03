package com.sspharma.api;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;
import com.sspharma.bean.Appointment;

@Api(name = "api", version = "v1", namespace = @ApiNamespace(ownerDomain = "sspharma.com", ownerName = "sspharma.com", packagePath = "") )
@ApiClass(resource = "appointments")
public class AppointmentApi {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method.
	 * 
	 * @param q Query parameter.<br>
	 * 			Examples : 1. doctor=abc@gmail.com<br>
	 * 				       2. user=abc@gmail.com<br>
	 * 					   3. doctor=abc@gmail.com,user=cba@gmail.com<br>
	 * 
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted.
	 */
	@ApiMethod(name = "appointments.listAppointment")
	public CollectionResponse<Appointment> listAppointment(@Named("q") @Nullable String q) {

		
		List<Appointment> appointments = null;
		if(q == null)
		{
			appointments = ofy().load().type(Appointment.class).list();
		}
		else
		{
			String[] conditions = q.split(",");
			LoadType<Appointment> loadType = ofy().load().type(Appointment.class);
			Query<Appointment> query=null;
			for(String condition : conditions)
			{
				String[] conditionKeyValue = condition.split("=");
				if(conditionKeyValue[0].equalsIgnoreCase("doctor"))
				{
					if(query == null)
						query = loadType.filter("doctorEmail", conditionKeyValue[1]);
					else
						query = query.filter("doctorEmail", conditionKeyValue[1]);
				}
				else if(conditionKeyValue[0].equalsIgnoreCase("user"))
				{
					
					if(query == null)
						query = loadType.filter("userEmail", conditionKeyValue[1]);
					else
						query = query.filter("userEmail", conditionKeyValue[1]);
				}
			}
			if(query != null)
			{
				appointments = query.list();
			}
			else
			{
				appointments = loadType.list();
			}
			
		}
		return new CollectionResponse.Builder<Appointment>().setItems(appointments).build();
	
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "appointments.getAppointment")
	public Appointment getAppointment(@Named("id") String id) {
		return ofy().load().type(Appointment.class).id(id).now();
	}

	/**
	 * This inserts a new entity into App Engine datastore. 
	 *
	 * @param appointment the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "appointments.insertAppointment")
	public Appointment insertAppointment(Appointment appointment) {
		Key<Appointment> key = ofy().save().entity(appointment).now();
		return ofy().load().key(key).now();
	}

	/**
	 * This method is used for updating an existing entity. 
	 *
	 * @param appointment the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "appointments.updateAppointment")
	public Appointment updateAppointment(Appointment appointment) {
		Key<Appointment> key = ofy().save().entity(appointment).now();
		return ofy().load().key(key).now();
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "appointments.removeAppointment")
	public void removeAppointment(@Named("id") String id) {
		ofy().delete().type(Appointment.class).id(id).now();
	}
	/**
	 * 
	 * @param doctorEmail
	 * @param date date in the format yyyy-MM-dd
	 * @return a Collection of Appointments
	 */
	@ApiMethod(name = "appointments.listAvailableSlots")
	public CollectionResponse<Appointment> listAvailableSlots(@Named("doctor") String doctorEmail,@Named("date")Date date)
	{
		
		return new CollectionResponse.Builder<Appointment>().setItems(new ArrayList<Appointment>()).build();	
	}
}
