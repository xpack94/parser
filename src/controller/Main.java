package controller;

import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.JFrame;

import Common.ClassDao;
import Common.DataApi;
import Common.Metrics;
import Common.UmlParser;
import Notifiers.ClassNotifier;

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
