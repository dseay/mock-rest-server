package org.dms.rest.mock.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Provides File reading/writing
 */
public class FileService {
	
	
	/**
	 * If passed a file will delete the file, if a directory will delete all files in that directory
	 */
	  public static void delete(File file) {
		if (file == null)
		  throw new NullPointerException();
		  
		System.out.println("| deleting " + file.getAbsolutePath());
		if (!file.exists()) {
			throw new IllegalArgumentException("File does not exist; " + file.getAbsolutePath());
		}

	    if (file.isFile()) {
	      if (!file.delete()) {
	    	  throw new RuntimeException("unable to delete file; " + file.getAbsolutePath());
	      }
	    } else if (file.isDirectory()) {
	      File[] files = file.listFiles();
	      for (File f : files) {
	  		  System.out.println("| deleting " + f.getAbsolutePath());
		      if (!f.delete()) {
		    	  throw new RuntimeException("unable to delete file; " + f.getAbsolutePath());
		      }
	      }
	    }
      }
	  	  
	  
	  /**
	   * Read file converting to string using UTF-8
	   */
	  public static String read(File file) {
		  StringBuilder buffer = new StringBuilder();
          read(file, buffer);
          return buffer.toString();
	  }
	  
	  /**
	   * Read file into buffer
	   */
	  public static void read(File file, StringBuilder buffer) {
		  if (file == null)
			  throw new NullPointerException();
		  
		  System.out.println("| reading " + file.getAbsolutePath());
		  
		  if (!file.exists() || !file.isFile())
			  throw new IllegalArgumentException("Not a file; " + file.getAbsolutePath());
		  
		  BufferedReader reader = null;
	    try
	    {
	      reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
	     String line = reader.readLine();
	     while (line != null)
	     {
	       buffer.append(line).append('\n');
	       line = reader.readLine();
	     }
	    }
	    catch (IOException e)
	    {
	      throw new RuntimeException(e);
	    }
	    finally
	    {
	      if (reader != null)
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	  }
	  
	  /**
	   * Write json to file clobbering whatever was there creating if file did not exist
	   */
	  public static void write(File file, String json) {
		  System.out.println("| writing (" + json.length() + ")" + file.getAbsolutePath());
		  OutputStream writer = null;
		    try
		    {
		      if (file.isDirectory())
		    	  throw new IllegalArgumentException("can't write to directory; " + file.getAbsolutePath());
		      
		      if (file.isFile() && file.exists()) {
		    	  delete(file);
		      }

		      file.getParentFile().mkdirs(); // will create any directories if needed
		      writer = new FileOutputStream(file);
		      writer.write(json.getBytes("UTF-8"));
		      writer.flush();
		    }
		    catch (IOException e)
		    {
		      throw new RuntimeException(e);
		    }
		    finally
		    {
		      if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
	  }	  

}
