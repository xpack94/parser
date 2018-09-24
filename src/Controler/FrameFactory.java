package Controler;

import javax.swing.JFrame;

public class FrameFactory extends JFrame{

	private static JFrame FRAME =new JFrame();
	
	private FrameFactory(){
		super();
		if(FRAME!=null){
			this.FRAME.setVisible(true);
			this.FRAME.setSize(400, 250);
			this.FRAME.setResizable(false);
			this.FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.FRAME.pack();
		}
	}
	
	public static JFrame getFrame(){
		return FRAME;
	}
	
	
}
