package Components;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextField;

import Common.Features;

public class FileUploaderInput extends JTextField{

	private Features features=new Features();
	
	public FileUploaderInput(Features features){
		super();
		this.features=features;
	}
	public FileUploaderInput(String text){
		super(text);
	}
	
	public FileUploaderInput(String text,Features features){
		super(text);
		this.features=features;
	}
	
	//fonction decrivant tout les caracteristiques que le composante va avoir
	private void setFeaturesOfComponent(int gridx,int gridy,int gridWidth,int gridHeight,int fill,int anchor
			,Insets insets,int ipadx,int ipady,float weightx,float weighty){
		this.features.gridx=gridx;
		this.features.gridy=gridy;
		this.features.gridwidth=gridWidth;
		this.features.gridheight=gridHeight;
		this.features.fill=fill;
		this.features.anchor=anchor;
		this.features.insets=insets;
		this.features.ipady=ipady;
		this.features.ipadx=ipadx;
		this.features.weightx=weightx;
		this.features.weighty=weighty;
	}
	
	public Features getComponentFeatures(){
		return this.features;
	}
	public void setComponentFeatures(Features features){
		this.features=features;
	}
	
	
}

