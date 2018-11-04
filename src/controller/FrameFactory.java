package controller;

import java.awt.GridBagLayout;

import javax.swing.JFrame;

public class FrameFactory extends JFrame{

	private static JFrame FRAME =new FrameFactory("test");
	private FrameFactory(String title){
		super(title);
		
		this.setUndecorated(false);
		this.setResizable(true);
		this.setSize(1000, 750);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public static JFrame getFrame(){
		return FRAME;
	}
	
	
}
