package Controler;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

import Components.FileUploaderButton;
import Components.FileUploaderInput;

public class MainControler {


	public static void main(String[] args) {
		 JFrame frame=FrameFactory.getFrame();
		 final PanelContoler panel=new PanelContoler(new GridBagLayout());
		 FileUploaderButton fileUploaderButton= new FileUploaderButton();
		 FileUploaderInput fileUploaderInput = new FileUploaderInput("test");
		 panel.addComponent(fileUploaderButton, fileUploaderButton.getComponentFeatures());
		 panel.addComponent(fileUploaderInput, fileUploaderInput.getComponentFeatures());
		 
		  javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	 JFrame frame = new JFrame("GridBagLayoutDemo");
	                 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	          
	                 //Set up the content pane.
	                 frame.add(panel);
	          
	                 //Display the window.
	                 frame.pack();
	                 frame.setVisible(true);
	            }
	        });
		 

	}

}
