package com.sspharma.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.model.Permission;
import com.sspharma.constants.EnvConstants;

public class DriveUtil {

	private static HttpTransport trasport = new UrlFetchTransport();
	private static JsonFactory jsonFactory = new JacksonFactory();
	private static Drive driveService;

	public static Drive createService() {
		if (driveService == null) {
			GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(jsonFactory)
					.setTransport(trasport).setClientSecrets(EnvConstants.CLIENT_ID, EnvConstants.CLIENT_SECRET)
					.build();
			credential.setRefreshToken(EnvConstants.REFRESH_TOKEN);
			driveService = new Drive.Builder(trasport, jsonFactory, credential).build();
		}
		return driveService;
	}

	public static String createFolder(String name) throws IOException {
		File file = new File();
		file.setTitle(name);
		file.setMimeType("application/vnd.google-apps.folder");

		List<ParentReference> parents = new ArrayList<>();
		ParentReference projectRoot = new ParentReference();
		projectRoot.setId(EnvConstants.PROJECT_ROOT_FILE_ID);
		parents.add(projectRoot);
		file.setParents(parents);
		if(driveService == null)
			createService();
		file = driveService.files().insert(file).execute();
		return file.getId();
	}

	public static void shareFileOrFolder(String id, String email) throws IOException {
		Permission permission = new Permission();
		permission.setRole("reader");
		permission.setType("user");
		permission.setValue(email);
		if(driveService == null)
			createService();
		driveService.permissions().insert(id, permission).execute();
	}

	public static String createFile(String title, String... parentIds) throws IOException {
		File file = new File();
		file.setTitle(title);
		List<ParentReference> parents = new ArrayList<>();

		for (String parentId : parentIds) {
			ParentReference projectRoot = new ParentReference();
			projectRoot.setId(parentId);
			parents.add(projectRoot);
		}
		file.setParents(parents);
		if(driveService == null)
			createService();
		file = driveService.files().insert(file).execute();
		return file.getId();
	}

	public static void uploadFileFromRequest(HttpServletRequest req, String fileId){
		final FileItemFactory fileItemFactory = new DiskFileItemFactory();
		final ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
		System.out.println("inside upload");
		try {
			FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(req);
			FileItemStream fileItemStream = null;
			while (fileItemIterator.hasNext()) {
				fileItemStream = (FileItemStream) fileItemIterator.next();
				System.out.println("iteration "+fileItemStream.getName() );
				if (!fileItemStream.isFormField()) {
					System.out.println("got the file");
					InputStream inputStream = fileItemStream.openStream();
					System.out.println("Content type : " + fileItemStream.getContentType());
					File body = new File();
					body.setId(fileId);
					body.setMimeType(fileItemStream.getContentType());
					Drive driveService = DriveUtil.createService();
					driveService.files()
							.insert(body, new InputStreamContent(fileItemStream.getContentType(),
									new ByteArrayInputStream(IOUtils.toByteArray(inputStream))))
							.execute();
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

}
