package org.dms.rest.mock.json;

import java.lang.reflect.Modifier;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSON {
  public static final String MEDIA_TYPE = "application/json";
  private static final Gson GSON = build();

  private static Gson build() {
    GsonBuilder builder = new GsonBuilder();
    builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    builder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
    builder.excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE);
    builder.setVersion(1.0);
    builder.setPrettyPrinting();
    return builder.create();
  }
  
  /**
   * Return object represented as a JSON string
   */
   public static String to(Object src) {
     return GSON.toJson(src);
   }
   
   /**
    * Return object represented by the JSON string {json}
    * 
    * @param json JSON string representing an object of type {clazz}
    * @param clazz the type of object the JSON string represents
    */
   public static <T> T from(String json, Class<T> clazz) {
     return GSON.fromJson(json, clazz);
   }
}
