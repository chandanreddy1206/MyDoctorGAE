package com.sspharma.api;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;
import com.sspharma.bean.Doctor;
import com.sspharma.bean.User;
import com.sspharma.bean.VoiceMessage;
import com.sspharma.util.DriveUtil;

@Api(name = "api", version = "v1", namespace = @ApiNamespace(ownerDomain = "sspharma.com", ownerName = "sspharma.com", packagePath = "") )
@ApiClass(resource = "voicemessages")
public class VoiceMessageApi {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyy");

	/**
	 * 
	 * @param q Query parameter.<br>
	 * 			Examples : 1. doctor=abc@gmail.com<br>
	 * 				       2. user=abc@gmail.com<br>
	 * 					   3. doctor=abc@gmail.com,user=cba@gmail.com<br>
	 * @return list of <code>VoiceMessage</code>s
	 */
	@ApiMethod(name = "voicemessages.listVoiceMessages")
	public CollectionResponse<VoiceMessage> listVoiceMessages(@Named("q") @Nullable String q) {
		
		List<VoiceMessage> voiceMessages = null;
		if(q == null)
		{
			voiceMessages = ofy().load().type(VoiceMessage.class).list();
		}
		else
		{
			String[] conditions = q.split(",");
			LoadType<VoiceMessage> loadType = ofy().load().type(VoiceMessage.class);
			Query<VoiceMessage> query=null;
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
						query = loadType.filter("patientEmail", conditionKeyValue[1]);
					else
						query = query.filter("patientEmail", conditionKeyValue[1]);
				}
			}
			if(query != null)
			{
				voiceMessages = query.list();
			}
			else
			{
				voiceMessages = loadType.list();
			}
			
		}
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