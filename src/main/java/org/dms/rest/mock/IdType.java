package org.dms.rest.mock;

import java.util.concurrent.atomic.AtomicLong;

public enum IdType {
  /**
   * Will generate id's a sequence number
   */
  SEQUENCE {
    @Override public Object generate() {
      return counter.getAndIncrement();
	}
  },
  /**
   * Will generate id's as UUID's
   */
  UUID {
    @Override public Object generate() {
      return java.util.UUID.randomUUID().toString();
    }
  };  
  
  private static AtomicLong counter = new AtomicLong(System.currentTimeMillis());
  
  public abstract Object generate();
 
}
