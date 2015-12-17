package com.atosorigin.integrale.profiler.gui.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.atosorigin.integrale.profiler.gui.controller.TreeController;

public class TreeView extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3601195387309325404L;
	
	private DefaultTreeModel treeModel;
	private JTree tree;
	private JScrollPane pane;
	private TreeController tc;
	private DefaultMutableTreeNode root;
	public static final String METHOD_ENTRY = "enter";
	public static final String METHOD_EXIT = "exit";
	private GridBagLayout gbl = new GridBagLayout();
	private GridBagConstraints constraints = new GridBagConstraints();
		
	public TreeView(String rootString, String imageName) {
		
		root = new DefaultMutableTreeNode(rootString);
		treeModel = new DefaultTreeModel(root);
		tree = new JTree(treeModel);
		tc = new TreeController(this, treeModel);
		tree.getSelectionModel().addTreeSelectionListener(tc);
	    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    setVisible(true);
		ImageIcon leafIcon = createImageIcon(imageName);
        if (leafIcon != null) {
            DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
            renderer.setLeafIcon(leafIcon);
            renderer.setClosedIcon(leafIcon);
            renderer.setOpenIcon(leafIcon);
            leafIcon.setImageObserver(renderer);
            tree.setCellRenderer(renderer);
        } else {
            System.err.println("Leaf icon missing; using default.");
        }
        pane = new JScrollPane(tree);
        setLayout(gbl);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.WEST;
		gbl.setConstraints(pane, constraints);
		add(pane);
        
        setVisible(true);
        repaint();
		
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ProfilerView.class.getResource(path);
//        System.err.println("====== " + path + " ======");
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    public JTree getTree() {
    	
    	return tree;
    	
    }
        
    public TreeController getController() {
    	
    	return tc;
    	
    }
    
    public DefaultMutableTreeNode getRoot() {
    	
    	return root;
    	
    }
    
    public JScrollPane getPane() {
    	
    	return pane;
    	
    }
    
    public void setPane(JScrollPane newPane) {
    	
    	remove(pane);
    	pane = newPane;
    	gbl.setConstraints(pane, constraints);
    	add(pane);
    	
    }

}
