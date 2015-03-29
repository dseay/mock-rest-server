package org.dms.rest.mock.handlers;

import java.io.File;
import java.util.Map;

import org.dms.rest.mock.RestResponse;
import org.dms.rest.mock.files.FileService;

public class DeleteHandler implements RestHandler {

	@Override
	public RestResponse process(File file, String path, Object body, Map<String, String> params) {
		if (file.exists()) {
			FileService.delete(file);
			return new RestResponse(HTTP_OK);
		} else {
			return new RestResponse(HTTP_NOT_FOUND);
		}
	}

}
