package Components;

import java.awt.Insets;

import javax.swing.JButton;

import Common.Features;



public class FileUploaderButton extends JButton {
	
	//contient toutes les caracteristique de cette composante
	private Features features=new Features();
	private static String FILE_TITLE="Charger Fichier";
	
	public FileUploaderButton(Features features){
		super(FILE_TITLE);
		this.features=features;
	}
	public FileUploaderButton(){
		super(FILE_TITLE);
	}
	
	public FileUploaderButton(String UploaderTitle,Features features) {
		super(UploaderTitle);
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
