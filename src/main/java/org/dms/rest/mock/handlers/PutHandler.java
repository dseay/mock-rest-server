package org.dms.rest.mock.handlers;

import java.io.File;
import java.util.Map;

import org.dms.rest.mock.RestResponse;
import org.dms.rest.mock.files.FileService;
import org.dms.rest.mock.json.JSON;

public class PutHandler implements RestHandler {

	@Override
	public RestResponse process(File file, String path, Object body, Map<String, String> params) {
		if (body == null) {
			return new RestResponse(HTTP_BAD_REQUEST, "No content in post");
		}
		if (file.exists()) {
			if (file.isDirectory()) {
				return new RestResponse(HTTP_BAD_REQUEST, "PUT to directory not supported");
			} else {  
				if (body instanceof Map) {
					FileService.write(file, JSON.to(body));
					return new RestResponse(HTTP_OK);					
				} else {
					return new RestResponse(HTTP_BAD_REQUEST, "Not an object");
				}
			}
		} else {
			if (file.isDirectory()) {
				return new RestResponse(HTTP_BAD_REQUEST, "PUT to directory that does not exist, create it first.");
			} else {
				return new RestResponse(HTTP_NOT_FOUND, "File did not exist.");
			}
		}
	}

}