package Components;


import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import controller.FrameFactory;
import Common.Features;
import Common.Metrics;
import Common.UmlParser;
import Notifiers.ClassNotifier;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import Components.ButtonTrigger;


/**
 * classe qui correspond au boutons de l'interface graphique dans notre cas c'est le bouton qui permet de 
 * charger le fichier ainsi que le bouton qui genere le fichier csv 
 * le listener de l'evenement clique pour les deux bouton est gerer par la sous classe UploadFileListener de celle ci
 * 
 * **/

public class ButtonTrigger extends JButton {
	
	//contient toutes les caracteristique de cette composante
	private Features features;
	private ClassNotifier classNotifier;
	private final String buttonTitle;
	private ClassesList associatedList;
	private String selectedClass;
	private FileUploaderInput inputTextFiled;
	//le action listener du boutton qui va lui etre reliér
	private UploadFileListener uploadFileListener=new UploadFileListener(this);
	private UmlParser umlParser;
	
	public ButtonTrigger(Features features){
		super();
		this.buttonTitle="";
		this.features=features;
		this.addClickEvent();
	}
	public ButtonTrigger(String buttonTitle){
		super(buttonTitle);
		this.buttonTitle=buttonTitle;
		this.addClickEvent();
	}
	
	public ButtonTrigger(String buttontitle,Features features) {
		super(buttontitle);
		this.features=features;
		this.buttonTitle=buttontitle;
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
	
	/**
	 * @param readFile qui correspond au fichier chargé
	 * cette methode permet de faire la verification du type du fichier chargée si ce dernier et de type
	 * ucd alors elle fait appel a la methode @see parseFile de la classe @see  umlParser qui fait le parsing
	 * et si le fichier n'est pas du bon type alors elle affiche une erreur
	 * **/

	//la methode qui prends le fichier choisis en entrée et lit le fichier sous forme de string
	public void setReadFile(File readFile){
	
		this.umlParser=new UmlParser(readFile);
		String fileName=readFile.getName();
		this.inputTextFiled.setFileName(fileName);
		
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

	
	
	
	

	public Features getFeatures() {
		return features;
	}
	public void setFeatures(Features features) {
		this.features = features;
	}





	public ClassesList getAssociatedList() {
		return associatedList;
	}
	public void setAssociatedList(ClassesList associatedList) {
		this.associatedList = associatedList;
	}




	

	public FileUploaderInput getInputTextFiled() {
		return inputTextFiled;
	}
	public void setInputTextFiled(FileUploaderInput inputTextFiled) {
		this.inputTextFiled = inputTextFiled;
	}



	/**
	 * classe qui gére les evenements de cliques sur les boutons
	 * 
	 * **/
	private class UploadFileListener implements ActionListener{
		private ButtonTrigger button;
		
		
		public UploadFileListener(ButtonTrigger fileUploaderButton){
			this.button=fileUploaderButton;
		}
		
		
		/***
		 * @param e de type ActionEvent qui correspond a l'evenement du clique
		 * 
		 * cette methode gere les cliques sur les deux butons de l'interface (bouton charger fichier et generer csv)
		 * pour le bouton charger fichier elle permet l'affichage du JFileChooser dans lequel l'utulisateur
		 * charge le fichier ensuite se dernier est donné a la methode setReadFile de la classe @ see ButtonTrigger
		 * 
		 * pour le boutton generer csv cette methode appele la methode generateCsv de la classe @see metrics
		 * */
		//la methode qui s'occupe d'afficher le fileChooser et retourne le fichier selectionné
		public void actionPerformed(ActionEvent e) {
			//le boutton qui charge les fichier ucd est cliqué
			
			if(buttonTitle.equals("charger fichier")){
				JFileChooser fileChooser =new JFileChooser();
				int returnVal = fileChooser.showOpenDialog( (Component) e.getSource());
				    if (returnVal == JFileChooser.APPROVE_OPTION){ ;
				        File file = fileChooser.getSelectedFile();
				        try {
				          this.button.setReadFile(file);
				        } catch (Exception ex) {
				        	JOptionPane.showMessageDialog(FrameFactory.getFrame(), "probleme d'acces au fichier"+file.getAbsolutePath(),
				        			"Erreur",JOptionPane.ERROR_MESSAGE);
				        }
				    } 
				    else {
				        System.out.println("access au fichier annulé.");
				    }       
				}else if(buttonTitle.equals("Generer fichier csv")){
					//le bouton des metriques est cliqué 
					if(this.button.associatedList!=null){
						//on charge toutes les metriques
						Metrics metrics=new Metrics();
						metrics.generateCsv();
						
					}
				}
			}
			

	}







	
	
}
