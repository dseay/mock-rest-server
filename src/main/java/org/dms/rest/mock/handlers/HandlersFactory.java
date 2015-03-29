package org.dms.rest.mock.handlers;

import org.dms.rest.mock.IdType;

public enum HandlersFactory {

	PUT {
		@Override 
		RestHandler build(String idProperty, IdType idType) {
			return new PutHandler();
		}
	},
	GET {
		@Override
		RestHandler build(String idProperty, IdType idType) {
			return new GetHandler();
		}
	},
	DELETE {
		@Override
		RestHandler build(String idProperty, IdType idType) {
			return new DeleteHandler();
		}
	},
	POST {
		@Override
		RestHandler build(String idProperty, IdType idType) {
			return new PostHandler(idProperty, idType);
		}
	};
		
	abstract RestHandler build(String idProperty, IdType idType);
	
	/**
	 * Will return a handler for the given method or null if it is not supported.
	 */
	public static RestHandler lookup(String method, String idProperty, IdType idType) {
		for (HandlersFactory h : HandlersFactory.values()) {
			if (h.name().equalsIgnoreCase(method)) {
				return h.build(idProperty, idType);
			}
		}
		return null;
	}
}
