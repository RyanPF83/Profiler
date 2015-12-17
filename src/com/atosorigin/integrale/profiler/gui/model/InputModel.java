package com.atosorigin.integrale.profiler.gui.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

import com.atosorigin.integrale.profiler.tracer.Trace;

public class InputModel {

	public void callTrace(String textFieldText, String argsFieldText, String exAreaText) {

		System.out.println("Calling trace on code...");
		
		List<String> arguments = new ArrayList<String>();
		arguments.add(textFieldText);
		
		List<String> exclusions = new ArrayList<String>();

		if (!argsFieldText.equals("")) {
			
			System.out.println("Running trace on " + textFieldText + " with arguments " + argsFieldText);

			if (argsFieldText.contains(",")) {
				argsFieldText = argsFieldText.replaceAll("\\s", "");
				arguments.addAll(Arrays.asList(argsFieldText.split(",")));
			} else {
				
				System.out.println("Running trace on " + textFieldText);
				
				arguments.addAll(Arrays.asList(argsFieldText.split("\\s")));
			}				
		}

		String[] args = arguments.toArray(new String[arguments.size()]);
		
		if (!exAreaText.equals("")) {
			
			System.out.println("Excluding from trace any methods from the following packages: " + exAreaText);
			if (exAreaText.contains(", ")) {
				exAreaText = exAreaText.replaceAll("\\s", "");
			}
			exclusions.addAll(Arrays.asList(exAreaText.split(",")));
			for (String s : exclusions) {
				s = s.trim();
			}
		}
		
		String[] ex = exclusions.toArray(new String[exclusions.size()]);
		
		Trace.setExclusions(ex);
		Trace.main(args);

	}

	public String setFileAsText(String filepath) {

		return filepath;
		
	}

	public String setFileAsJava(File file) {

		String s = "";
		
		try {
   		 BufferedReader reader = new BufferedReader(new FileReader(file));
   		 String line = null;
   		 while ((line = reader.readLine()) != null) {
   			 if (line.contains("package")) {
	    			 s = line.substring(8).replace(";", "") + "." + file.getName().replace(".java", "");
	    			 break;
	    		 }
   		 }
   		 reader.close();
   		 
   	 } catch (FileNotFoundException fnfe){
   		 System.err.println("404!");
   	 } catch (IOException e) {
   		 System.err.println("Error reading file");
   	 }
		
		return s;
		
	}

	public String setFileAsClass(String filepath) {

		ClassParser cp = null;
		JavaClass jc = null;
		try {
			cp = new ClassParser(filepath);
			jc = cp.parse();
		} catch (ClassFormatException cfe) {
			System.err.println("Class not formatted correctly");
		} catch (IOException ioe) {
			System.err.println("Class does not exist");
		}

		return jc.getClassName();

	}

}
