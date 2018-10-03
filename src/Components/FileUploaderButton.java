package Components;

import java.awt.Insets;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import controller.FrameFactory;


import ActionListeners.UploadFileListener;
import Common.Features;
import Common.UmlParser;
import Notifiers.ClassNotifier;
import Notifiers.InputFileNotifier;



public class FileUploaderButton extends JButton implements Observer{
	
	//contient toutes les caracteristique de cette composante
	private Features features=new Features();
	private static String FILE_TITLE="Charger Fichier";
	private ClassNotifier classNotifier;
	private InputFileNotifier inputFileNotifier;

	//le action listener du boutton qui va lui etre reliér
	private UploadFileListener uploadFileListener=new UploadFileListener(this);
	private UmlParser umlParser;
	
	public FileUploaderButton(Features features){
		super(FILE_TITLE);
		this.features=features;
		this.addClickEvent();
	}
	public FileUploaderButton(){
		super(FILE_TITLE);
		this.addClickEvent();
	}
	
	public FileUploaderButton(String UploaderTitle,Features features) {
		super(UploaderTitle);
		this.features=features;
		this.addClickEvent();
	}
	
	
	private void addClickEvent(){
		//ajout de l'action on click sur le button 
		this.addActionListener(this.uploadFileListener);
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
	//la methode qui prends le fichier choisis en entrée et lit le fichier sous forme de string
	public void setReadFile(File readFile){
		this.umlParser=new UmlParser(readFile);
		try {
			String fileName=readFile.getName();
			this.inputFileNotifier.setFileName(fileName);
			if(!fileName.substring(fileName.indexOf(".")+1, fileName.length()).equals("ucd")){
				// le format choisit n'est pas .ucd
				JOptionPane.showMessageDialog(FrameFactory.getFrame(), "les fichiers doivent etre du format .ucd");
				return;
			}
			String dataToParse= new String(this.umlParser.readFile(), "UTF-8");
			this.umlParser.setClassNotifier(this.getClassNotifier());
			//maintenant en parse le fichier lu 
			this.umlParser.parseFile(dataToParse);
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public ClassNotifier getClassNotifier() {
		return classNotifier;
	}
	public void setClassNotifier(ClassNotifier classNotifier) {
		this.classNotifier = classNotifier;
	}
	public void update(Observable o, Object arg) {
		
	}
	public InputFileNotifier getInputFileNotifier() {
		return inputFileNotifier;
	}
	public void setInputFileNotifier(InputFileNotifier inputFileNotifier) {
		this.inputFileNotifier = inputFileNotifier;
	}
	
	
}
