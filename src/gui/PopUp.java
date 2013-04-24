package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import gui.DrawGraph;

class PopUp extends JPopupMenu {
    JMenuItem reset;
	JMenuItem from;
    JMenuItem to;
    DrawGraph dgInstance;
    public PopUp(DrawGraph dg){
    	dgInstance = dg;
    	reset = new JMenuItem("Reset Map");
        from = new JMenuItem("Show directions from here!");
        to = new JMenuItem("Show directions to here!");
        
        reset.addActionListener(new ResetActionListener());
        
        add(reset);
        addSeparator();
        add(from);
        add(to);
    }
    
    private class ResetActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dgInstance.resetMap();
			
		}
    	
    }
}