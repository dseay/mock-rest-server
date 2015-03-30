package org.dms.rest.mock;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test
public class RestResponseTest {

	public void testStatusOnly() {
		RestResponse r = new RestResponse(200);
		assertEquals(r.getStatus(), 200);
		assertNotNull(r.getHeaders());
		assertTrue(r.getHeaders().isEmpty());
		assertNotNull(r.getBody());
		assertTrue(r.getBody().isEmpty());
	}
	
	public void testStatusAndBody() {
		String json = "{\"name\": \"Joe\"}";
		RestResponse r = new RestResponse(200, json);
		assertEquals(r.getStatus(), 200);
		assertEquals(r.getBody(), json);
	}
	
	public void testStatusAndHeaders() {
		String name = "Location";
		String value = "http://test.com/api/users/123";
		RestResponse r = new RestResponse(201, buildHeaders(name, value));
		assertEquals(r.getStatus(), 201);
        assertNotNull(r.getHeaders());
        assertEquals(r.getHeaders().get(name), value);
	}
	
	private Map<String,String> buildHeaders(String name, String value) {
		Map<String,String> headers = new HashMap<String,String>();
		headers.put(name, value);
		return headers;
	}
	
}
