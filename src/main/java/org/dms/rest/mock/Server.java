package org.dms.rest.mock;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dms.rest.mock.files.Filters;

import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class Server {
	
	private static final List<String> HELP_ARGS = Arrays.asList("--help", "--?", "?", "-?", "-help");

	private final int port;
	private final File root;
	private final String idProperty;
	private final IdType idType;
	
	private HttpServer server;
	
	public Server(int port, File root, String idProperty, IdType idType) {
		if (root == null || idProperty == null || idType == null)
			throw new NullPointerException();
		
		if (!root.exists() || !root.isDirectory() || !root.canRead())
			throw new IllegalArgumentException("root must be a readable directory; " + root.getAbsolutePath());

		this.port = port;
		this.root = root;
		this.idProperty = idProperty;
		this.idType = idType;
	}
	
	public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        RequestRouter handler = new RequestRouter(root, idProperty, idType);
        List<String> paths = getPaths();
        if (paths.size() == 0)
        	throw new IllegalArgumentException("must be at least one sub directory under " + root.getAbsolutePath());
        
        for( String path : paths) {
            server.createContext(path, handler);
        }
        server.setExecutor(null); // creates a default executor
        server.start();		
		System.out.println("mock server started on port " + port + " serving from " + root.getAbsolutePath());
	}
	
	private List<String> getPaths() {
		List<String> paths = new ArrayList<String>();
		File[] files = root.listFiles(Filters.ReadableDirectories);
		String path;
		for (File file : files) {
			path = file.getAbsolutePath().substring(root.getAbsolutePath().length()).replace('\\', '/');
			System.out.println("adding path: " + path + "/*");
			paths.add(path);
		}
		return paths;
	}
	
	private void shutdown() {
		try {
			System.out.println("shutting down mock server ...");
//			server.stop(15);  TODO hanging?
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			System.out.println("mock server stopped");
		}
	}
	
	public static void main(String[] args) {
		try {
			if (args.length > 0 && HELP_ARGS.contains(args[0].toLowerCase())) {
				printHelp();
			}
			
			int port = args.length > 0 ? getPort(args[0]) : 8080;
			File root = args.length > 1 ? getRoot(args[1]) : new File(".");
			String idProp = args.length > 2 ? getIdProp(args[2]) : "id";
			IdType idType = args.length > 3 ? getIdType(args[3]) : IdType.UUID;

			final Server s = new Server(port, root, idProp, idType);
	        Runtime.getRuntime().addShutdownHook(new Thread("ServerShutdownHook") {
	          public void run() {
	            s.shutdown();
	          }
	        });
			System.out.println("starting mock server on port " + port);
			s.start();
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			printHelp();
			System.exit(-1);
		} catch (Exception e) {
			printHelp();
			e.printStackTrace();
			System.exit(-2);
		}
	}
	
	public static IdType getIdType(String arg) {
		try {
			return IdType.valueOf(arg.toUpperCase());
		} catch (Exception e) {
			StringBuilder buff = new StringBuilder();
			for (IdType t : IdType.values()) {
				if (buff.length() > 0) {
					buff.append(", ");
				}
				buff.append(t.name());
			}
			throw new IllegalArgumentException("invalid idType; '"+arg+"' must be one of " + buff.toString());
		}
	}
	
	public static String getIdProp(String arg) {
		if (arg == null || arg.isEmpty())
			throw new IllegalArgumentException("invalid idProperty, can't be empty");

		if (!arg.matches("[a-z0-9\\-\\_]+"))
			throw new IllegalArgumentException("invalid idProperty; must be valid JSON property");

		return arg;
	}
	
	public static File getRoot(String arg) {
		File file = new File(arg);
		if (!file.exists())
			throw new IllegalArgumentException("invalid root, does not exist; " + file.getAbsolutePath());

		if (!file.isDirectory())
			throw new IllegalArgumentException("invalid root, not a directory; " + file.getAbsolutePath());
				
		if (!file.canRead())
			throw new IllegalArgumentException("invalid root, can't read; " + file.getAbsolutePath());
		
		return file;
	}
	
	public static int getPort(String arg) {
		try {
			int port = Integer.parseInt(arg);
			if (port < 1 || port > 65534)
				throw new IllegalArgumentException("invalid port range; '" + port + "'");
			return port;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("invalid port; '" + arg + "'");
		}
	}
	
	public static void printHelp() {
		System.out.println("java -jar mock-rest-server.jar <port:8080> <root:.> <idProperty:id> <idType:uuid>");
	}
	
}
