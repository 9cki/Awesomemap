package gui;

import graph.QuadTree;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class CreateGUI {

	JFrame frame; //The frame
	public double width; //The width of the frame
	public double height; //The height of the frame
	private static CreateGUI instance = null; //Used for the Singleton pattern
	
	private CreateGUI(QuadTree qt) {
		createFrame(qt);
	}
	
	public double getWidth() { //Returns the width of the frame
		Dimension dim = frame.getSize();
		return dim.width;
	}
	
	public double getHeight() { //Returns the height of the frame
		Dimension dim = frame.getSize();
		return dim.height;
	}
	
	//Creates the frame
	public void createFrame(QuadTree qt) {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();
		
		frame = new JFrame("AWESOME MAP");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		
		Container contentPane = frame.getContentPane();

		double tempWidth = width/1.2;
		double tempHeight = height/1.2;
		
		//Adds the map as a JComponent
		JComponent dg = new DrawGraph(qt, (int) tempWidth, (int) tempHeight);
		contentPane.add(dg);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
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
}
