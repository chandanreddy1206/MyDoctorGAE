package com.sspharma.api;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.gson.Gson;
import com.googlecode.objectify.Key;
import com.sspharma.bean.Doctor;
import com.sspharma.bean.User;
import com.sspharma.bean.VoiceMessage;
import com.sspharma.util.DriveUtil;

public class InsertVoiceMessage extends HttpServlet {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyy");

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final FileItemFactory fileItemFactory = new DiskFileItemFactory();
		final ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);

		String patientEmail = "";
		String doctorEmail = "";
		String title = "";
		byte[] voiceMsgData = null;
		
		
		
		try {
			FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(req);
			FileItemStream fileItemStream = null;
			while (fileItemIterator.hasNext()) {
				fileItemStream = (FileItemStream) fileItemIterator.next();
				if (!fileItemStream.isFormField()) {
					InputStream inputStream = fileItemStream.openStream();
					System.out.println("Content type : " + fileItemStream.getContentType());
					voiceMsgData = IOUtils.toByteArray(inputStream);
				}
				else if(fileItemStream.getFieldName().equalsIgnoreCase("doctorEmail"))
				{
					doctorEmail = Streams.asString(fileItemStream
							.openStream());
				}
				else if(fileItemStream.getFieldName().equalsIgnoreCase("patientEmail"))
				{
					patientEmail = Streams.asString(fileItemStream
							.openStream());
				}
				else if(fileItemStream.getFieldName().equalsIgnoreCase("title"))
				{
					title = Streams.asString(fileItemStream
							.openStream());
				}
			}
			Doctor doctor = ofy().load().type(Doctor.class).id(doctorEmail).now();
			User user = ofy().load().type(User.class).id(patientEmail).now();
			
			List<ParentReference> parents = new ArrayList<>();

			ParentReference parentDocter = new ParentReference();
			parentDocter.setId(user.getDriveFolderId());
			parents.add(parentDocter);


			ParentReference parentUser = new ParentReference();
			parentUser.setId(doctor.getDriveFolderId());
			parents.add(parentUser);

			String fileTitle = title + " " + dateFormat.format(new Date());
			File body = new File();
			body.setTitle(fileTitle);
			body.setMimeType(fileItemStream.getContentType());
			body.setParents(parents);
			Drive driveService = DriveUtil.createService();
			File file = driveService.files()
					.insert(body, new InputStreamContent(fileItemStream.getContentType(),
							new ByteArrayInputStream(voiceMsgData)))
					.execute();

			VoiceMessage voiceMessage = new VoiceMessage();
			voiceMessage.setDriveFileId(file.getId());
			voiceMessage.setDoctorEmail(doctorEmail);
			voiceMessage.setPatientEmail(patientEmail);
			voiceMessage.setTitle(title);
			Key<VoiceMessage> key = ofy().save().entity(voiceMessage).now();
			resp.getWriter().print(new Gson().toJson(ofy().load().key(key).now()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}