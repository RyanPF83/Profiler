package com.atosorigin.integrale.profiler.gui.controller;

import java.util.List;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.atosorigin.integrale.profiler.gui.controller.fileio.FileIn;
import com.atosorigin.integrale.profiler.gui.view.ProfilerView;
import com.atosorigin.integrale.profiler.gui.view.TreeView;

public class TreeController implements TreeSelectionListener {
	
	private List<String[]> methods = null;
	private String[] csv = null;
	
	private TreeView view;
	
	private DefaultMutableTreeNode newRoot;
	
	public TreeController(TreeView view, DefaultTreeModel treeModel) {
		
		this.view = view;
	
	}

	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setNodesFromFile(String filepath) {

		System.out.println("Creating method tree from trace file");
		
		TreeView newTreeView = null;
		
		
	    FileIn fi = new FileIn(filepath);
	    List<String[]> methods = fi.read();
	    
	    newTreeView = new TreeView("<Class>.<Method>(<Argument Types>)", "/img/bullet.png");
	    newRoot = newTreeView.getRoot();
	    createTree(methods, newRoot);
	    
	    view.getTree().setModel(new DefaultTreeModel(newRoot));
	    
	    ProfilerView.getProfilerView().setTreeView(newTreeView);
		
	}
	
	public void setWorkingPane() {
		
		JScrollPane jsp = view.getPane();
		JLabel imageLabel = new JLabel();
		ImageIcon image = new ImageIcon(getClass().getResource("/img/loading.gif"));
		imageLabel.setIcon(image);
		
		jsp = new JScrollPane(imageLabel);
		
		view.setPane(jsp);
		view.validate();
		view.repaint();
		
	}
	
	private void createTree(DefaultMutableTreeNode rootNode) {
    	
		int i = 0;
		
		Stack<Long> startTimes = new Stack<Long>();
		
		DefaultMutableTreeNode parent = rootNode;
		
		while (i < methods.size()) {
			
			csv = methods.get(i);
			
			if (csv[0].equals("enter")) {
				
				Long startTime = Long.parseLong(csv[3]);
				startTimes.push(startTime);
				String argTypes = csv[2];
				argTypes = argTypes.replace("java.lang.", "").replace("java.util.", "");
				argTypes = argTypes.substring(1, argTypes.length()-1);
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(csv[1] + "( " + argTypes + " )");
				parent.add(child);
				parent = child;
				i++;
				
			} else {
				
				Long endTime = Long.parseLong(csv[3]);
				Long startTime = startTimes.pop();
				Long span = (endTime - startTime) / 1000000;
				String methodDetails = (String)parent.getUserObject();
				parent.setUserObject(methodDetails + "   completed after " + span + " ms");
				parent = (DefaultMutableTreeNode)parent.getParent();
				i++;
			}
			
			
		}
    	
    }

	private void createTree(List<String[]> methods, DefaultMutableTreeNode parentNode) {
		
		this.methods = methods;
		createTree(parentNode);
		
	}

}
