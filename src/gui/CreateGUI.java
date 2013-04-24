package gui;

import graph.QuadTree;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;

public class CreateGUI {

	JFrame frame; //The frame
	public double width; //The width of the frame
	public double height; //The height of the frame
	private static CreateGUI instance = null; //Used for the Singleton pattern
	private buttonActionListener bal = new buttonActionListener();
	private static GuiMethods guiM;
	
	// JPanels
	JPanel leftPanel = new JPanel();
	JPanel logoPanel = new JPanel();
	JPanel searchPanel = new JPanel();
	JPanel controlsPanel = new JPanel(); 
	
	// JLabels
	
	// ImageIcons - They are all initialized in createImageIcon()
	ImageIcon logo;
	ImageIcon searchIcon;
	ImageIcon controlsIcon;
	
	ImageIcon twitter;
	ImageIcon ruteDirections;
	ImageIcon search;
	
	ImageIcon zoomIn;
	ImageIcon zoomOut;
	ImageIcon reset;
	ImageIcon arrowUp;
	ImageIcon arrowDown;
	ImageIcon arrowLeft;
	ImageIcon arrowRight;
	
	
	// JButtons
	JButton twitter_btn;
	JButton ruteDirections_btn;
	JButton search_btn;
	
	JButton zoomIn_btn;
	JButton zoomOut_btn;
	JButton reset_btn;
	JButton arrowUp_btn;
	JButton arrowDown_btn;
	JButton arrowLeft_btn;
	JButton arrowRight_btn;
	
	//JTextFields
	JTextField from;
	JTextField to;
	
	/**
	 * Constructor which calls the createFrame()
	 * @param qt The quadtree containing all nodes
	 */
	private CreateGUI(QuadTree qt) {
		createFrame(qt);
	}
	
	public double getFrameWidth() { //Returns the width of the frame
		Dimension dim = frame.getSize();
		return dim.width;
	}
	
	public double getFrameHeight() { //Returns the height of the frame
		Dimension dim = frame.getSize();
		return dim.height;
	}
	
	//Creates the frame
	public void createFrame(QuadTree qt) {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();
		
		frame = new JFrame("ITU.maps");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		
		Container contentPane = frame.getContentPane();
		
		createImageIcons();
		
		JPanel controls = new JPanel();
		controls.setBorder(BorderFactory.createLineBorder(Color.black));
		controls.setBackground(Color.white);
		
		JPanel search = new JPanel();
		search.setBorder(BorderFactory.createLineBorder(Color.black));
		search.setBackground(Color.white);
		
		double tempWidth = width/1.2;
		double tempHeight = height/1.2;
		
		//Adds the map as a JComponent
		JComponent dg = DrawGraph.getInstance(qt, (int) tempWidth, (int) tempHeight);
		dg.setBorder(BorderFactory.createLineBorder(Color.black));
		
		addLogoPanel();
		createButtons();
		addSearchPanel();
		addControlsPanel();
		
		leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5)); // top, left, bottom, right
		//leftPanel.setPreferredSize(new Dimension(200, frame.getHeight()));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		leftPanel.setBackground(Color.darkGray);
		
		leftPanel.add(logoPanel);
		leftPanel.add(searchPanel);
		leftPanel.add(controlsPanel);
		
		JScrollPane scrollPane = new JScrollPane(leftPanel);
		scrollPane.setPreferredSize(new Dimension(230,550));
		contentPane.setLayout(new BorderLayout());
		contentPane.add(dg, BorderLayout.CENTER);
		contentPane.add(scrollPane, BorderLayout.WEST);
		
		frame.repaint();
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	
	public void addLogoPanel(){
		logoPanel.setBackground(Color.darkGray);
		logoPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white));
		
		logoPanel.setPreferredSize(new Dimension(200, 80));
		logoPanel.setMaximumSize(new Dimension(200, 80));
		logoPanel.setMinimumSize(new Dimension(200, 80));
		
		// Add stuff to the panel
		logoPanel.add(new JLabel(logo));
	}
	
	public void addSearchPanel(){
		searchPanel.setBackground(Color.darkGray);
		searchPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white));
		
		searchPanel.setPreferredSize(new Dimension(200, 250));
		searchPanel.setMaximumSize(new Dimension(200, 250));
		searchPanel.setMinimumSize(new Dimension(200, 250));
		
		JPanel buttons = new JPanel();
		buttons.setBackground(Color.darkGray);
		buttons.setLayout(new FlowLayout());
		
		buttons.add(twitter_btn);
		buttons.add(ruteDirections_btn);
		buttons.add(search_btn);
		
		Reader r = new Reader();
		ArrayList<String> list = new ArrayList<String>();
		list = r.getList();
		
		from = new AutoTextField(list);
		to = new AutoTextField(list);
		
		searchPanel.add(new JLabel(searchIcon));
		searchPanel.add(from);
		searchPanel.add(to);
		searchPanel.add(buttons);
	}
	
	public void addControlsPanel(){
		controlsPanel.setBackground(Color.darkGray);
		controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.PAGE_AXIS));
		
		controlsPanel.setPreferredSize(new Dimension(200, 300));
		controlsPanel.setMaximumSize(new Dimension(200, 300));
		controlsPanel.setMinimumSize(new Dimension(200, 300));
		
		JPanel controlsTitle = new JPanel();
		controlsTitle.setBackground(Color.darkGray);
		JPanel controls = new JPanel();
		controls.setBackground(Color.darkGray);
		controlsPanel.add(controlsTitle);
		controlsPanel.add(controls);
		
		controls.setLayout(new GridLayout(4, 3));
		controls.add(new JLabel());
		controls.add(arrowUp_btn);
		controls.add(new JLabel());
		controls.add(arrowLeft_btn);
		controls.add(reset_btn);
		controls.add(arrowRight_btn);
		controls.add(new JLabel());
		controls.add(arrowDown_btn);
		controls.add(new JLabel());
		controls.add(zoomOut_btn);
		controls.add(new JLabel());
		controls.add(zoomIn_btn);
		
		controlsTitle.add(new JLabel(controlsIcon));
	}
	
	public void createImageIcons(){
		logo = createImageIcon("img/logo.png", "logo");
		searchIcon = createImageIcon("img/searchIcon.png", "searchIcon");
		controlsIcon = createImageIcon("img/controlsIcon.png", "controlsIcon");
		
		twitter = createImageIcon("img/twitter.png", "twitter");
		ruteDirections = createImageIcon("img/ruteDirections.png", "ruteDirections");
		search = createImageIcon("img/search.png", "search");
		
		zoomIn = createImageIcon("img/zoomin.png", "zoomIn");
		zoomOut = createImageIcon("img/zoomout.png", "zoomOut");
		reset = createImageIcon("img/reset.png", "reset");
		arrowUp = createImageIcon("img/arrow1up.png", "arrowUp");
		arrowDown = createImageIcon("img/arrow1down.png", "arrowDown");
		arrowLeft = createImageIcon("img/arrow1left.png", "arrowLeft");
		arrowRight = createImageIcon("img/arrow1Right.png", "arrowRight");
	}
	
	public void createButtons(){
		guiM = new GuiMethods();
		twitter_btn = button(twitter_btn, twitter);
		ruteDirections_btn = button(ruteDirections_btn, ruteDirections);
		search_btn = button(search_btn, search);
		
		zoomIn_btn = button(zoomIn_btn, zoomIn);
		zoomOut_btn = button(zoomOut_btn, zoomOut);
		reset_btn = button(reset_btn, reset);
		arrowUp_btn = button(arrowUp_btn, arrowUp);
		arrowDown_btn = button(arrowDown_btn, arrowDown);
		arrowLeft_btn = button(arrowLeft_btn, arrowLeft);
		arrowRight_btn = button(arrowRight_btn, arrowRight);
		
	}
	
	/**
	 * Manipulates with the JButton given as a parameter. 
	 * It adds the ImageIcon to the button, removes the border, 
	 * changes the size and adds the actionListener for event-handling.
	 * @param JButton btn The button we manipulate
	 * @param ImageIcon i The image icon that belongs to the button
	 * @return JButton btn Returns the manipulated button
	 */
	public JButton button(JButton btn, ImageIcon i){
		btn = new JButton(i);
		btn.setBorderPainted(false);
		btn.setBackground(null);
		btn.setPreferredSize(new Dimension(50,50));
		btn.addActionListener(bal);
		return btn;
	}
	
	public String getTextFromTextField(JTextField t){
		return t.getText();
	}
	
	public static CreateGUI returnInstance() { //Used for the Singleton pattern
		return instance;
	}
	
	public static CreateGUI getInstance(QuadTree qt) { //Used for the Singleton pattern
	      if(instance == null) {
	         instance = new CreateGUI(qt);
	      }
	      return instance;
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path,
	                                           String description) {
	    java.net.URL imgURL = getClass().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL, description);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}
	
	private class buttonActionListener implements ActionListener {
	
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == twitter_btn){
				System.out.println("Do the twitterthing!");
			} else if(e.getSource() == ruteDirections_btn){
				System.out.println("RuteDirections");
			} else if(e.getSource() == search_btn){
				System.out.println("Search from " + getTextFromTextField(from) + " to " + getTextFromTextField(to));
			} else if(e.getSource() == zoomIn_btn){
				System.out.println("Zoom in++++");
			} else if(e.getSource() == zoomOut_btn){
				guiM.zoomOut();
			} else if(e.getSource() == reset_btn){
				System.out.println("Reset the map!");
			} else if(e.getSource() == arrowUp_btn){
				System.out.println("Pan upwards");
			} else if(e.getSource() == arrowDown_btn){
				System.out.println("Pan downwards");
			} else if(e.getSource() == arrowLeft_btn){
				System.out.println("Pan Left!");
			} else if(e.getSource() == arrowRight_btn){
				System.out.println("Pan right");
			}
			
		}
	}
}
