package com.sspharma.api;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.googlecode.objectify.Key;
import com.sspharma.bean.Doctor;
import com.sspharma.constants.EnvConstants;
import com.sspharma.util.DriveUtil;

@Api(name = "api", version = "v1", namespace = @ApiNamespace(ownerDomain = "sspharma.com", ownerName = "sspharma.com", packagePath = "") )
@ApiClass(resource = "doctors")
public class DoctorApi {
	
	@ApiMethod(name = "doctors.listDoctors")
	public CollectionResponse<Doctor> listDoctors() {
		
		List<Doctor> doctors = ofy().load().type(Doctor.class).list();
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
}
