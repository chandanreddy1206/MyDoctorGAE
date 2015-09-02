package com.sspharma.api;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;
import com.sspharma.bean.Doctor;
import com.sspharma.constants.EnvConstants;
import com.sspharma.util.DriveUtil;

@Api(name = "api", version = "v1", namespace = @ApiNamespace(ownerDomain = "sspharma.com", ownerName = "sspharma.com", packagePath = "") )
@ApiClass(resource = "doctors")
public class DoctorApi {
	/**
	 * 
	 * @param q Query parameter.<br>
	 * 			Examples : 1. category=gp<br>
	 * 				       2. category=gynecologist<br>
	 * @return list of <code>VoiceMessage</code>s
	 */
	@ApiMethod(name = "doctors.listDoctors")
	public CollectionResponse<Doctor> listDoctors(@Named("q") @Nullable String q) {
		
		List<Doctor> doctors = null;
		if(q == null)
		{
			doctors = ofy().load().type(Doctor.class).list();
		}
		else
		{
			String[] conditions = q.split(",");
			LoadType<Doctor> loadType = ofy().load().type(Doctor.class);
			Query<Doctor> query=null;
			for(String condition : conditions)
			{
				String[] conditionKeyValue = condition.split("=");
				if(conditionKeyValue[0].equalsIgnoreCase("category"))
				{
					if(query == null)
						query = loadType.filter("category", conditionKeyValue[1]);
					else
						query = query.filter("category", conditionKeyValue[1]);
				}
			}
			if(query != null)
			{
				doctors = query.list();
			}
			else
			{
				doctors = loadType.list();
			}
			
		}
		return new CollectionResponse.Builder<Doctor>().setItems(doctors).build();
	}

	@ApiMethod(name = "doctors.insertDoctor")
	public Doctor insertDoctor(Doctor doctor) throws ConflictException, IOException {
		
		if (ofy().load().type(Doctor.class).id(doctor.getEmail()).now() != null) {
			throw new ConflictException("Doctor with Email '" + doctor.getEmail() + "' already exists");
		}
		
		String fileId = DriveUtil.createFolder(EnvConstants.PROJECT_TITLE + " : " + doctor.getName());
		DriveUtil.shareFileOrFolder(fileId, doctor.getEmail());
		doctor.setDriveFolderId(fileId);
		
		Key<Doctor> key = ofy().save().entity(doctor).now();
		return ofy().load().key(key).now();
	}

	@ApiMethod(name = "doctors.getDoctor")
	public Doctor getDoctor(@Named("email") String email) {
		return ofy().load().type(Doctor.class).id(email).now();
	}

	@ApiMethod(name = "doctors.updateDoctor")
	public Doctor updateDoctor(Doctor doctor) {
		Key<Doctor> key = ofy().save().entity(doctor).now();
		return ofy().load().key(key).now();
	}
	@ApiMethod(name = "doctors.categories.listCategories",path="doctors/categories")
	public CollectionResponse<String> getAllCategories()
	{
		List<String> categories = new ArrayList<>();
		List<Doctor> doctors = ofy().load().type(Doctor.class).project("category").distinct(true).list();
		System.out.println(doctors);
		for(Doctor doctor : doctors)
		{
			categories.add(doctor.getCategory());
		}
		return new CollectionResponse.Builder<String>().setItems(categories).build();
	}
	@ApiMethod(name = "doctors.removeDoctor")
	public void removeDoctor(@Named("id")String id)
	{
		ofy().delete().type(Doctor.class).id(id).now();
	}
}
