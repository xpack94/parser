package Components;

import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;


import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import controller.FrameFactory;

import Common.ClassDao;
import Common.Features;
import Common.Metrics;
import Common.UmlParser;
import Notifiers.ClassNotifier;
import Notifiers.InputFileNotifier;
import Notifiers.MetricsNotifier;



import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import controller.FrameFactory;

import Components.ButtonTrigger;



public class ButtonTrigger extends JButton implements Observer{
	
	//contient toutes les caracteristique de cette composante
	private Features features;
	private ClassNotifier classNotifier;
	private InputFileNotifier inputFileNotifier;
	private final String buttonTitle;
	private ClassesList associatedList;
	private String selectedClass;
	
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

	public InputFileNotifier getInputFileNotifier() {
		return inputFileNotifier;
	}
	public void setInputFileNotifier(InputFileNotifier inputFileNotifier) {
		this.inputFileNotifier = inputFileNotifier;
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

	@Override
	public void update(Observable o, Object arg) {
		this.selectedClass=((MetricsNotifier)o).getSelectedClass();
	}



	private class UploadFileListener implements ActionListener{
		private ButtonTrigger button;
		
		
		public UploadFileListener(ButtonTrigger fileUploaderButton){
			this.button=fileUploaderButton;
		}
		
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
				}else if(buttonTitle.equals("Calculer Metriques")){
					//le bouton des metriques est cliqué 
					if(this.button.associatedList!=null){
						//on charge toutes les metriques
						Metrics metrics=new Metrics();
						DefaultListModel<String> list=new DefaultListModel<String>();
						if(this.button.selectedClass!=null){
							for(String metric:metrics.getMetrics()){
								
								list.addElement(metric+"="+metrics.metricsCalculator(this.button.selectedClass, metric));
							}
							this.button.associatedList.setModel(list);
						}
						
						
					}
				}
			}
			

	}







	
	
}
