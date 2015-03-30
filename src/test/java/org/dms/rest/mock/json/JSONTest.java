package org.dms.rest.mock.json;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test
public class JSONTest {

	private static final String JSON_JOE_OBJ = "{\"name\": \"Joe\"}";
	
	@SuppressWarnings("unchecked")
	public void testJsonArrayToList() {
		String json = "["+JSON_JOE_OBJ+"]";
		Object dto = JSON.from(json, Object.class);
		assertNotNull(dto);
		assertTrue(dto instanceof List);
		assertEquals(((List<?>)dto).size(), 1);
		assertTrue(((List<?>)dto).get(0) instanceof Map);
		assertJoe(((List<Map<String,String>>)dto).get(0));
	}
	
	@SuppressWarnings("unchecked")
	public void testJsonObjectToMap() {
		Object dto = JSON.from(JSON_JOE_OBJ, Object.class);
		assertNotNull(dto);
		assertTrue(dto instanceof Map);
		assertEquals(((Map<?,?>)dto).size(), 1);
		assertJoe((Map<String, String>)dto);
	}
	
	public void testFromJsonToJson() {
		Object dto = JSON.from(JSON_JOE_OBJ, Object.class);
		String json = JSON.to(dto);
		assertNotNull(json);
		assertFalse(json.isEmpty());
        assertTrue(json.contains("Joe"));
	}
	
	private void assertJoe(Map<String, String> dto) {
		assertEquals(dto.get("name"), "Joe");
	}
	
}
