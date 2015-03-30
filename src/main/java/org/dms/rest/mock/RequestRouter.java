package org.dms.rest.mock;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dms.rest.mock.handlers.HandlersFactory;
import org.dms.rest.mock.handlers.RestHandler;
import org.dms.rest.mock.json.JSON;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public class RequestRouter implements HttpHandler {
	
	private final String idProperty;
	private final IdType idType;
	private final File root;
	
	public RequestRouter(File root, String idProperty, IdType idType) {
		if (root == null || idProperty == null || idType == null)
			throw new NullPointerException();
		
		this.root = root;
		this.idProperty = idProperty;
		this.idType = idType;
	}

	@Override
	public void handle(HttpExchange t) throws IOException {
		try {
			String method = t.getRequestMethod();
			URI uri = t.getRequestURI();
			int port = t.getLocalAddress().getPort();
			String path = "http://" + t.getRemoteAddress().getHostName() + ":" + port + uri.getPath();
			System.out.println("< " + method + " " + path);
			File file = new File(root, uri.getPath());
			RestHandler handler = HandlersFactory.lookup(method, idProperty, idType);
			Object body = readBody(t);
			if (handler == null) {
				sendResponse(t, new RestResponse(405)); // method not allowed
			} else {
				sendResponse(t, handler.process(file, path, body, getQueryParams(t)));
			}
		} catch (Throwable e) {
			e.printStackTrace();
			sendResponse(t, new RestResponse(500));
		}
	}
	
	private Object readBody(HttpExchange t) {
		InputStreamReader reader = null;
		try {
			StringBuilder result = new StringBuilder(1024);
			reader =  new InputStreamReader(t.getRequestBody(),"utf-8");
			char[] buffer = new char[1024];
			int read = reader.read(buffer);
			while (read >= 0) {
				if (read > 0) {
			  	  result.append(buffer, 0, read);
				  read = reader.read(buffer);
				}
			}
			reader.close();
			return parse(result.toString());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private Object parse(String json) {
		if (json == null || json.isEmpty()){
			return null;
		} else {
			return JSON.from(json, Object.class);
		}
	}
	
	private void sendResponse(HttpExchange xfer, RestResponse response)
	{
		OutputStream os = null;
		try {
			byte[] bytes = response.getBody().getBytes("UTF-8");
			System.out.println("> " + response.getStatus() + " ("+bytes.length+")");
			Headers headers = xfer.getResponseHeaders();
			if (bytes.length > 0) {
				headers.add("Content-Type", JSON.MEDIA_TYPE);			
			}
			
			for (Entry<String, String> entry : response.getHeaders().entrySet()) {
				headers.add(entry.getKey(), entry.getValue());
			}
			
			xfer.sendResponseHeaders(response.getStatus(), bytes.length);
			os = xfer.getResponseBody();
			os.write(bytes);
			os.flush();				
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	private Map<String,String> getQueryParams(HttpExchange t) {
		Map<String,String> params = new HashMap<String, String>();
		for (Entry<String, List<String>> entry : t.getRequestHeaders().entrySet()) {
			params.put(entry.getKey(), entry.getValue().get(0));
		}
		return params;
	}

}
