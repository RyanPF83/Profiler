package com.atosorigin.integrale.profiler.gui.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.atosorigin.integrale.profiler.gui.view.InputView;
import com.atosorigin.integrale.profiler.gui.controller.InputController;
import com.atosorigin.integrale.profiler.gui.model.InputModel;

public class ProfilerView extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -651848171173037276L;
	
	private static ProfilerView pv;
	private TreeView tv;
	private InputView iv;
	private JPanel jp;
	private GridBagLayout gbl = new GridBagLayout();
	private GridBagConstraints constraints = new GridBagConstraints();

	
	private ProfilerView() {
		
		super("Method Tracer");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jp = new JPanel();
		
		jp.setLayout(gbl);
		
		JComponent e0 = empty();
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(e0, constraints);
		jp.add(e0);
		
		iv = new InputView();
		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(iv, constraints);
		iv.addController(new InputController(iv, new InputModel()));
		jp.add(iv);
		
		JComponent e1 = empty();
		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(e1, constraints);
		jp.add(e1);
		
		JSeparator js = new JSeparator(SwingConstants.HORIZONTAL);
		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(js, constraints);
		jp.add(js);
		
		JComponent e2 = empty();
		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(e2, constraints);
		jp.add(e2);
		
		tv = new TreeView("Select a file to trace or profile", "/img/bullet.png");
		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(tv, constraints);
		jp.add(tv);
		
		JComponent e3 = empty();
		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(e3, constraints);
		jp.add(e3);
		
		JComponent e4 = empty();
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 8;
		constraints.weightx = 0;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.anchor = GridBagConstraints.WEST;
		gbl.setConstraints(e4, constraints);
		jp.add(e4);
		
		JComponent e5 = empty();
		constraints = new GridBagConstraints();
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 8;
		constraints.weightx = 0;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(e5, constraints);
		jp.add(e5);
		
		add(jp);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	public static void runit() {
		
		if (pv == null) {
			pv = new ProfilerView();
		}
		
	}

	
	public static ProfilerView getProfilerView() {
		
		ProfilerView retPV = null;
		if (pv != null) {
			retPV = pv;
		}
		return retPV;
		
	}
	
	public TreeView getTreeView() {
		
		return tv;
		
	}
	
	public void setTreeView(TreeView newTv) {
		
		jp.remove(tv);
    	tv = newTv;
    	constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(tv, constraints);
		jp.add(tv);
	    tv.getTree().expandRow(0);
		jp.validate();
		jp.repaint();
		
	}
		
	private JComponent empty() {
        JPanel panel = new JPanel();
        return panel;
    }
	

}
