package controller;

import java.awt.GridBagLayout;

import javax.swing.JFrame;

public class FrameFactory extends JFrame{

	private static JFrame FRAME =new FrameFactory("test");
	private FrameFactory(String title){
		super(title);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setUndecorated(false);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public static JFrame getFrame(){
		return FRAME;
	}
	
	
}
