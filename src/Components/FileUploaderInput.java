package Components;
import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import Common.Features;

public class FileUploaderInput extends JTextField{

	private Features features=new Features();
	
	public FileUploaderInput(){
		super();
		this.setFeaturesOfComponent();
	}
	
	public FileUploaderInput(String text){
		super(text);
		this.setFeaturesOfComponent();
	}
	
	//fonction decrivant tout les caracteristiques que le composante va avoir
	private void setFeaturesOfComponent(){
		this.features.gridx=1;
		this.features.gridy=0;
		this.features.gridwidth=2;
		this.features.ipady=10;
		this.features.fill=Features.HORIZONTAL;	
		this.features.weightx=0.5;
	}
	
	public Features getComponentFeatures(){
		return this.features;
	}
	public void setComponentFeatures(Features features){
		this.features=features;
	}
	
	
}

