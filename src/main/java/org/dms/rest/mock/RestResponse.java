package org.dms.rest.mock;

import java.util.Collections;
import java.util.Map;

public class RestResponse {

	private final int status;
	private final String body;
	private final Map<String,String> headers;
	
	public RestResponse(int status) {
		this(status, null, null);
	}
	
	public RestResponse(int status, String body) {
		this(status, body, null);
	}
	
	public RestResponse(int status, Map<String, String> headers) {
		this(status, null, headers);
	}
	
	public RestResponse(int status, String body, Map<String, String> headers) {
		this.status = status;
		this.body = body == null ? "": body;
		if (headers == null) {
			this.headers = Collections.emptyMap();
		} else {
	  		this.headers = headers;
		}
	}

	public int getStatus() {
		return status;
	}

	public String getBody() {
		return body;
	}

	public Map<String, String> getHeaders() {
	   return headers;
	}
	
}
