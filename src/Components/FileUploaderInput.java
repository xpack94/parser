package Components;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextField;

import Common.Features;
import Notifiers.InputFileNotifier;

public class FileUploaderInput extends JTextField implements Observer{

	private Features features=new Features();
	private  boolean IS_ENABLED=false;
	
	public FileUploaderInput(Features features){
		super();
		this.features=features;
		this.setEnabled(this.IS_ENABLED);
	}
	public FileUploaderInput(String text){
		super(text);
		this.setEnabled(this.IS_ENABLED);
	}
	
	public FileUploaderInput(String text,Features features){
		super(text);
		this.features=features;
		this.setEnabled(this.IS_ENABLED);
	}
		
	public Features getComponentFeatures(){
		return this.features;
	}
	public void setComponentFeatures(Features features){
		this.features=features;
	}
	public void update(Observable o, Object arg) {
		this.setText( ((InputFileNotifier ) o).getFileName());
		
	}
	
	
}

