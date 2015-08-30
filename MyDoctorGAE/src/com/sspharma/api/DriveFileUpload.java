package com.sspharma.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.sspharma.util.DriveUtil;

public class DriveFileUpload extends HttpServlet {
	@Override
	public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final FileItemFactory fileItemFactory = new DiskFileItemFactory();
		final ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
		try {
			FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(req);
			FileItemStream fileItemStream = null;
			while (fileItemIterator.hasNext()) {
				fileItemStream = (FileItemStream) fileItemIterator.next();
				if (!fileItemStream.isFormField()) {
					InputStream inputStream = fileItemStream.openStream();
					System.out.println("Content type : " + fileItemStream.getContentType());
					File body = new File();
					body.setMimeType(fileItemStream.getContentType());
					Drive driveService = DriveUtil.createService();
					File file = driveService.files()
							.insert(body, new InputStreamContent(fileItemStream.getContentType(),
									new ByteArrayInputStream(IOUtils.toByteArray(inputStream))))
							.execute();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}