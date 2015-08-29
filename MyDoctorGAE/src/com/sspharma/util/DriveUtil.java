package com.sspharma.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
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
}
