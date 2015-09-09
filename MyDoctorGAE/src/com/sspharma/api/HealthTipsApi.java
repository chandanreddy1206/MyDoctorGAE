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
import com.googlecode.objectify.Key;
import com.sspharma.bean.HealthTip;

@Api(name = "api", version = "v1", namespace = @ApiNamespace(ownerDomain = "sspharma.com", ownerName = "sspharma.com", packagePath = "") )
@ApiClass(resource = "healthtips")
public class HealthTipsApi {
	
	@ApiMethod(name = "healthtips.listhealthTips")
	public CollectionResponse<HealthTip> listhealthTips() {
		List<HealthTip> healthTips = ofy().load().type(HealthTip.class).list();
		return new CollectionResponse.Builder<HealthTip>().setItems(healthTips).build();
	}

	@ApiMethod(name = "healthtips.inserthealthTip")
	public HealthTip inserthealthTip(HealthTip healthTip) throws IOException {
		Key<HealthTip> key = ofy().save().entity(healthTip).now();
		return ofy().load().key(key).now();
	}

	@ApiMethod(name = "healthtips.gethealthTip")
	public HealthTip gethealthTip(@Named("id") Long id) {
		return ofy().load().type(HealthTip.class).id(id).now();
	}

	@ApiMethod(name = "healthtips.updatehealthTip")
	public HealthTip updatehealthTip(HealthTip healthTip) {
		Key<HealthTip> key = ofy().save().entity(healthTip).now();
		return ofy().load().key(key).now();
	}
	@ApiMethod(name = "healthtips.removehealthTip")
	public void removehealthTip(@Named("id") Long id)
	{
		ofy().delete().type(HealthTip.class).id(id).now();
	}
}
