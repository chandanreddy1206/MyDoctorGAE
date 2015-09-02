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
import com.sspharma.bean.User;
import com.sspharma.constants.EnvConstants;
import com.sspharma.util.DriveUtil;

@Api(name = "api", version = "v1", namespace = @ApiNamespace(ownerDomain = "sspharma.com", ownerName = "sspharma.com", packagePath = "") )
@ApiClass(resource = "users")
public class UserApi {
	
	@ApiMethod(name = "users.listUsers")
	public CollectionResponse<User> listUsers() {
		List<User> users = ofy().load().type(User.class).list();
		return new CollectionResponse.Builder<User>().setItems(users).build();
	}

	@ApiMethod(name = "users.insertUser")
	public User insertUser(User user) throws IOException {
		String fileId = DriveUtil.createFolder(EnvConstants.PROJECT_TITLE + " : " + user.getName());
		DriveUtil.shareFileOrFolder(fileId, user.getEmail());
		user.setDriveFolderId(fileId);
		
		Key<User> key = ofy().save().entity(user).now();
		return ofy().load().key(key).now();
	}

	@ApiMethod(name = "users.getUser")
	public User getUser(@Named("email") String email) {
		return ofy().load().type(User.class).id(email).now();
	}

	@ApiMethod(name = "users.updateUser")
	public User updateUser(User user) {
		Key<User> key = ofy().save().entity(user).now();
		return ofy().load().key(key).now();
	}
	@ApiMethod(name = "users.removeUser")
	public void removeUser(@Named("id")String id)
	{
		ofy().delete().type(User.class).id(id).now();
	}
}
