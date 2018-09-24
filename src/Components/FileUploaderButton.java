package Components;

import javax.swing.JButton;

import Common.Features;



public class FileUploaderButton extends JButton {
	
	//contient toutes les caracteristique de cette composante
	private Features features=new Features();
	private static String FILE_TITLE="Charger Fichier";
	
	public FileUploaderButton(){
		super(FILE_TITLE);
		this.setFeaturesOfComponent();
	}
	
	public FileUploaderButton(String UploaderTitle) {
		super(UploaderTitle);
		this.setFeaturesOfComponent();
	}
	//fonction decrivant tout les caracteristiques que le composante va avoir
	private void setFeaturesOfComponent(){
		this.features.gridx=0;
		this.features.gridy=0;
	}
	
	public Features getComponentFeatures(){
		return this.features;
	}
	public void setComponentFeatures(Features features){
		this.features=features;
	}
}
