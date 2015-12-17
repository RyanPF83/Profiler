package com.atosorigin.integrale.profiler.gui.controller.fileio;

import java.io.File;

public class FileExtension {
	
	public final static String clazz = ".class";
    public final static String java = ".java";
    public final static String trc = ".trc";
    public final static String txt = ".txt";
    public final static String csv = ".csv";

    /*
     * Get the extension of a file.
     */  
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.')-1;

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

}
