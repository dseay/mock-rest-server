package org.dms.rest.mock.handlers;

import java.io.File;
import java.util.Map;

import org.dms.rest.mock.RestResponse;
import org.dms.rest.mock.files.FileService;

public class GetHandler implements RestHandler {

	private static final String LIST_PREFIX = "[\n";
	private static final String ENTITY_SEPERATOR = "\n,";
	private static final String LIST_POSTFIX = "]";
	
	@Override
	public RestResponse process(File file, String path, Object body, Map<String, String> params) {
		if (file.exists()) {
			if (file.isFile()) {
				return new RestResponse(HTTP_OK, FileService.read(file));
			} else {
				StringBuilder buff = new StringBuilder();
				buff.append(LIST_PREFIX);
				File[] files = file.listFiles();
				for (File f : files) {
					if (f.isFile()) {
						if (buff.length() > LIST_PREFIX.length()) {
							buff.append(ENTITY_SEPERATOR);
						}
						FileService.read(f, buff);
					}
				}
				buff.append(LIST_POSTFIX);
				return new RestResponse(HTTP_OK, buff.toString());
			}
		} else {
			return new RestResponse(HTTP_NOT_FOUND);
		}
	}
}