package com.atosorigin.integrale.profiler.gui.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.atosorigin.integrale.profiler.gui.controller.InputController;

public class InputView extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8459227677673037215L;
	
	public static final String TFTEXT = "Select a text trace file to profile, or a Class to trace and profile";
	public static final String AFTEXT = "Enter arguments for the class to trace here";
	
	private JLabel tl;
	private JTextField tf;
	private JLabel al;
	private JTextField af;
	private JButton browse;
	public static final String BROWSE = "Browse...";
	private JButton profile;
	public static final String PROFILE = "Profile";
	private JTextArea ta;

		
	public InputView() {
		
		// Set up the layout for the pane
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		
		JLabel ex = new JLabel("Method Exclusions: ");
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 35;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.WEST;
		gbl.setConstraints(ex, constraints);
		add(ex);
		
		ta = new JTextArea("java.*, javax.*, sun.*, com.sun.*");
		JScrollPane jsp = new JScrollPane(ta);
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 35;
		constraints.gridheight = 3;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(jsp, constraints);
		add(jsp);
		
		JComponent e1 = empty();
		constraints = new GridBagConstraints();
		constraints.gridx = 35;
		constraints.gridy = 0;
		constraints.gridwidth = 5;
		constraints.gridheight = 2;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(e1, constraints);
		add(e1);
		
		tl = new JLabel("File/Class: ");
		constraints = new GridBagConstraints();
		constraints.gridx = 40;
		constraints.gridy = 0;
		constraints.gridwidth = 10;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(tl, constraints);
		add(tl);
		
		tf = new JTextField(TFTEXT);
		constraints = new GridBagConstraints();
		constraints.gridx = 50;
		constraints.gridy = 0;
		constraints.gridwidth = 40;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(tf, constraints);
		add(tf);
		
		browse = new JButton(BROWSE);
		browse.setSize(10, 5);
		constraints = new GridBagConstraints();
		constraints.gridx = 90;
		constraints.gridy = 0;
		constraints.gridwidth = 10;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(browse, constraints);
		add(browse);
		
		JComponent e2 = empty();
		constraints = new GridBagConstraints();
		constraints.gridx = 40;
		constraints.gridy = 1;
		constraints.gridwidth = 60;
		constraints.gridheight = 2;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(e2, constraints);
		add(e2);
		
		al = new JLabel("Arguments: ");
		constraints = new GridBagConstraints();
		constraints.gridx = 40;
		constraints.gridy = 3;
		constraints.gridwidth = 10;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(al, constraints);
		add(al);
		
		af = new JTextField(AFTEXT);
		constraints = new GridBagConstraints();
		constraints.gridx = 50;
		constraints.gridy = 3;
		constraints.gridwidth = 40;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(af, constraints);
		add(af);
		
		profile = new JButton(PROFILE);
		constraints = new GridBagConstraints();
		constraints.gridx = 90;
		constraints.gridy = 3;
		constraints.gridwidth = 10;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(profile, constraints);
		add(profile);

		setVisible(true);
		
	}
	
	public void addController(InputController controller){
		browse.addActionListener(controller);
		profile.addActionListener(controller);
	}
	
	public String getTextFieldText() {
		
		return tf.getText();
		
	}
	
	public String getArgsFieldText() {
		
		return af.getText();
		
	}

	public void setTextFieldText(String text) {

		tf.setText(text);
		
	}

	private JComponent empty() {
        JPanel panel = new JPanel();
        return panel;
    }

	public String getExAreaText() {

		return ta.getText();
		
	}
		
}
