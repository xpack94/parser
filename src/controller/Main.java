package controller;

import java.awt.GridBagLayout;

import javax.swing.JFrame;

public class Main {

	
	public static void main(String[] args) {
		
		final MainController mainController=new MainController(new PanelContoller(new GridBagLayout()));
		 javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	JFrame frame=FrameFactory.getFrame();
	            	frame.add(mainController.getPanel());
	            	frame.setVisible(true);
	            }
	        });
		 

	}
}
