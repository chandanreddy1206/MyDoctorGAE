package com.sspharma.api;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.googlecode.objectify.Key;
import com.sspharma.bean.Doctor;
import com.sspharma.bean.User;
import com.sspharma.bean.VoiceMessage;
import com.sspharma.constants.EnvConstants;
import com.sspharma.util.DriveUtil;

@Api(name = "api", version = "v1", namespace = @ApiNamespace(ownerDomain = "sspharma.com", ownerName = "sspharma.com", packagePath = "") )
@ApiClass(resource = "voicemessages")
public class VoiceMessageApi {

	HttpTransport trasport = new UrlFetchTransport();
	JsonFactory jsonFactory = new JacksonFactory();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyy");

	@ApiMethod(name = "voicemessages.listDoctors")
	public CollectionResponse<VoiceMessage> listDoctors() {
		List<VoiceMessage> voiceMessages = ofy().load().type(VoiceMessage.class).list();
		return new CollectionResponse.Builder<VoiceMessage>().setItems(voiceMessages).build();
	}

	@ApiMethod(name = "voicemessages.insertVoiceMessage")
	public VoiceMessage insertVoiceMessage(VoiceMessage voiceMessage) throws IOException {
		Doctor doctor = ofy().load().type(Doctor.class).id(voiceMessage.getDoctorEmail()).now();
		User user = ofy().load().type(User.class).id(voiceMessage.getPatientEmail()).now();
		String fileTitle = voiceMessage.getTitle() +" "+ dateFormat.format(new Date());
		String fileId =	DriveUtil.createFile(fileTitle, doctor.getDriveFolderId(),user.getDriveFolderId());
		voiceMessage.setDriveFileId(fileId);
		Key<VoiceMessage> key = ofy().save().entity(voiceMessage).now();
		return ofy().load().key(key).now();
	}

	@ApiMethod(name = "voicemessages.getVoiceMessage")
	public VoiceMessage getVoiceMessage(@Named("id") String id) {
		return ofy().load().type(VoiceMessage.class).id(id).now();
	}
}