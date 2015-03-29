package org.dms.rest.mock.handlers;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dms.rest.mock.IdType;
import org.dms.rest.mock.RestResponse;
import org.dms.rest.mock.files.FileService;
import org.dms.rest.mock.json.JSON;

public class PostHandler implements RestHandler {
	
	private final String idProperty;
	private final IdType idType;
	
	public PostHandler(String idProperty, IdType idType) {
		this.idProperty = idProperty;
		this.idType = idType;
	}

	@Override
	public RestResponse process(File file, String path, Object body, Map<String, String> params) {
		if (body == null) {
			return new RestResponse(HTTP_BAD_REQUEST, "No content in post");
		}
		if (!file.exists()) {
			return new RestResponse(HTTP_BAD_REQUEST, "Directory does not exit, must create it first.");
		}
		if (file.isFile()) {
			return new RestResponse(HTTP_BAD_REQUEST, "Must specify a directory not an existing file");
		}
		
		if (body instanceof Map) {  
			@SuppressWarnings("unchecked")
			Map<Object,Object> entity = (Map<Object,Object>)body;
			Object id = writeEntity(file, entity);
			return new RestResponse(HTTP_CREATED, buildLocationHeader(path, String.valueOf(id)));			
		} else if (body instanceof List) {
			@SuppressWarnings("unchecked")
			List<Map<Object,Object>> entities = (List<Map<Object,Object>>)body;
			for (Map<Object,Object> entity : entities) {
				writeEntity(file, entity);
			}
			return new RestResponse(HTTP_CREATED);
		} else {
			throw new RuntimeException("Invalid body type; " + body.getClass().getName());
		}		
	}
	
	private Map<String,String> buildLocationHeader(String path, String id) {
		Map<String,String> headers = new HashMap<String, String>(1);
		headers.put("Location", path + "/" + id);
		return headers;
	}
	
	/**
	 * @return id for entity
	 */
	private Object writeEntity(File entityRoot, Map<Object,Object> entity) {
		Object id = idType.generate();
		entity.put(idProperty, id);
		FileService.write(new File(entityRoot, String.valueOf(id)), JSON.to(entity));
		return id;
	}

}