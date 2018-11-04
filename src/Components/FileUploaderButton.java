package Components;

import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import controller.FrameFactory;

import Common.Features;
import Common.UmlParser;
import Notifiers.ClassNotifier;
import Notifiers.InputFileNotifier;



import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import controller.FrameFactory;

import Components.FileUploaderButton;



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
		UploadFileListener listener=new UploadFileListener(this);
		this.addActionListener(listener);
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
		String fileName=readFile.getName();
		this.inputFileNotifier.setFileName(fileName);
		
		if(!fileName.substring(fileName.indexOf(".")+1, fileName.length()).equals("ucd")){
			// le format choisit n'est pas .ucd
			JOptionPane.showMessageDialog(FrameFactory.getFrame(), "les fichiers doivent etre du format .ucd");
			return;
		}
		Scanner s=null;
		this.umlParser.setClassNotifier(this.getClassNotifier());
		//maintenant en parse le fichier lu 
		this.umlParser.parseFile(readFile);
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
	
	
	
	

	private class UploadFileListener implements ActionListener{
		private FileUploaderButton fileUploaderButton;
		
		
		public UploadFileListener(FileUploaderButton fileUploaderButton){
			this.fileUploaderButton=fileUploaderButton;
		}
		
		//la methode qui s'occupe d'afficher le fileChooser et retourne le fichier selectionné
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser =new JFileChooser();
			int returnVal = fileChooser.showOpenDialog( (Component) e.getSource());
			    if (returnVal == JFileChooser.APPROVE_OPTION){ ;
			        File file = fileChooser.getSelectedFile();
			        try {
			          this.fileUploaderButton.setReadFile(file);
			        } catch (Exception ex) {
			        	JOptionPane.showMessageDialog(FrameFactory.getFrame(), "probleme d'acces au fichier"+file.getAbsolutePath(),
			        			"Erreur",JOptionPane.ERROR_MESSAGE);
			        }
			    } 
			    else {
			        System.out.println("access au fichier annulé.");
			    }       
			}   

	}

	
	
}
