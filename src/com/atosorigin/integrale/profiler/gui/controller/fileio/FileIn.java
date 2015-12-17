package com.atosorigin.integrale.profiler.gui.controller.fileio;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileIn {
	
	private String filePath;
	
	public FileIn(String path) {
		
		filePath = path;
		
	}
	
	public List<String[]> read() {
		
		BufferedReader br = null;
		
		String line = "";
		String splitter = ";";
		
		List<String[]> methods = new ArrayList<String[]>();
		
		try {
			
			br = new BufferedReader(new FileReader(filePath));
			
			while ((line = br.readLine()) != null) {
				
				String[] methodDetails = line.split(splitter);
				methods.add(methodDetails);
				
			}
			
		} catch (FileNotFoundException fnfe) {
			
		} catch (IOException ioe) {
			
		} finally {
			
			if (br != null) {
				
				try {
					
					br.close();
					
				} catch (IOException ioe) {
					
					
					
				}
				
			}
			
		}
		
		return methods;
		
	}

}
