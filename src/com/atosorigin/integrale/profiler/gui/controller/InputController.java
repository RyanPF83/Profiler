package com.atosorigin.integrale.profiler.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.atosorigin.integrale.profiler.gui.controller.fileio.FileExtension;
import com.atosorigin.integrale.profiler.gui.controller.fileio.JavaFilter;
import com.atosorigin.integrale.profiler.gui.model.InputModel;
import com.atosorigin.integrale.profiler.gui.view.InputView;
import com.atosorigin.integrale.profiler.gui.view.ProfilerView;

public class InputController implements ActionListener {
	
	private InputView view;
	private InputModel model;
	
	private JFileChooser fc;
	private File selectedFile;
	
	public InputController(InputView view, InputModel model) {
	
		this.view = view;
		this.model = model;
		
		// Set up File Chooser for the Browse button
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		
		fc.addChoosableFileFilter(new JavaFilter());
		fc.setAcceptAllFileFilterUsed(false);
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		// Get the source of the event
		JButton source = (JButton) event.getSource();
		
		if (source.getText().equals(InputView.BROWSE)) {
			
			System.out.println("Browse for file");

			int returnVal = fc.showDialog(view.getParent(), "Select");
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				selectedFile = fc.getSelectedFile();
				String filepath = selectedFile.getAbsolutePath();

				if (filepath.contains(".class")) {
					
					System.out.println("Class file selected to trace and profile");

					view.setTextFieldText(model.setFileAsClass(filepath));

				} else if (filepath.contains(".java")) {

					System.out.println("Java file selected to trace and profile");
					
					view.setTextFieldText(model.setFileAsJava(selectedFile));

				} else {

					System.out.println("Text, CSV or TRC file selected to profile");
					
					view.setTextFieldText(model.setFileAsText(filepath));

				}
			}

		} else if (source.getText().equals(InputView.PROFILE)) {
			
			TreeController tc = ProfilerView.getProfilerView().getTreeView().getController();
			//tc.setNodesFromFile("");
			tc.setWorkingPane();
			
			System.out.println("Profiling...");
			
			String fileExtension = view.getTextFieldText().substring(view.getTextFieldText().length()-4, view.getTextFieldText().length());

			if (view.getTextFieldText().equals(InputView.TFTEXT)) {

				System.out.println("No file selected to profile");
				
				JOptionPane.showMessageDialog(view.getParent(), "No file selected", "File error", JOptionPane.ERROR_MESSAGE);

			} else if (fileExtension.equals(FileExtension.trc) || fileExtension.equals(FileExtension.txt) || fileExtension.equals(FileExtension.csv)) {
				
				System.out.println("File has extension: " + fileExtension);
				
				String filepath = view.getTextFieldText();
				tc.setNodesFromFile(filepath);
				
			} else {
				
												
				String args = view.getArgsFieldText();
				
				if (args.startsWith("Enter arguments")) {
					args = "";
				}
				
				String excludes = view.getExAreaText();

				callTrace(view.getTextFieldText(), args, excludes);
				
			}

		}

	}
	
	public void callTrace(String text, String args, String ex) {
		
		class TraceThread implements Runnable {
			String runText;
            String runArgs;
            String runEx;
            TraceThread(String t, String s, String e) { runText = t; runArgs = s; runEx = e; }
            public void run() {
            	model.callTrace(runText, runArgs, runEx);
            	TreeController tc = ProfilerView.getProfilerView().getTreeView().getController();
            	
            	//TODO Add a new text field box in UI to customise the location of the generated trace file
            	tc.setNodesFromFile("./res/out/trace.trc");
            }
        }
        Thread t = new Thread(new TraceThread(text, args, ex));
        t.start();
		
	}

}
