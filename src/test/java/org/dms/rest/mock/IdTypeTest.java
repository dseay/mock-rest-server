package org.dms.rest.mock;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

@Test
public class IdTypeTest {

	public void testUUID() {
		Object id = IdType.UUID.generate();
		assertNotNull(id);
		assertTrue(id instanceof String);
		assertFalse(id.toString().isEmpty());
	}
	
	public void testSequence() {
		Object id = IdType.SEQUENCE.generate();
		assertNotNull(id);
		assertTrue(id instanceof Long);
		assertTrue(((Long)id) > 0);
	}
	
}
