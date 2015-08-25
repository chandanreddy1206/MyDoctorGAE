package com.sspharma.api;

import java.util.ArrayList;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.sspharma.bean.Doctor;

@Api(name = "doctoApi", version = "v1", namespace = @ApiNamespace(ownerDomain="sspharma.com",ownerName="sspharma.com",packagePath=""))
public class DoctorApi 
{
	@ApiMethod(name = "listDoctors")
	public CollectionResponse<Doctor> listDoctors()
	{
		List<Doctor> doctors = new ArrayList<>();
		doctors.add(new Doctor());
		
		return new CollectionResponse.Builder<Doctor>().setItems(doctors).build();
		
	}
}
