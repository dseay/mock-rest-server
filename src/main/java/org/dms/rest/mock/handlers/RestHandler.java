package org.dms.rest.mock.handlers;

import java.io.File;
import java.util.Map;

import org.dms.rest.mock.RestResponse;

public interface RestHandler {

  public static final int HTTP_OK = 200;
  public static final int HTTP_CREATED = 201;
  public static final int HTTP_NOT_FOUND = 404;
  public static final int HTTP_BAD_REQUEST = 400;
	
	
	
  public RestResponse process(File file, String path, Object body, Map<String,String> params);
  
}
