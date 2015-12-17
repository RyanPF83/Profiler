package com.atosorigin.integrale.profiler.gui.controller.fileio;

import java.io.File;

import javax.swing.filechooser.*;

public class JavaFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		
		if (f.isDirectory()) {
	        return true;
	    }
		
		String extension = FileExtension.getExtension(f);
        if (extension != null) {
            if (extension.equals(FileExtension.clazz) ||
                extension.equals(FileExtension.java) ||
                extension.equals(FileExtension.trc) ||
                extension.equals(FileExtension.txt) ||
                extension.equals(FileExtension.csv)) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
	}
	
	@Override
	public String getDescription() {
		return "Class, java, trc, txt and csv files";
	}

}
