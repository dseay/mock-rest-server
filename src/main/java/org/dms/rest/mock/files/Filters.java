package org.dms.rest.mock.files;

import java.io.File;
import java.io.FileFilter;

public enum Filters implements FileFilter {

	ReadableDirectories {
		@Override public boolean accept(File file) {
			return file.isDirectory() && file.canRead();
		}
	};

}
