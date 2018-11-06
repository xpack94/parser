package controller;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;


import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import Common.Features;
import Components.ClassesList;
import Components.DetailsTextField;
import Components.ButtonTrigger;
import Components.FileUploaderInput;
import Components.ScrollPane;
import Notifiers.ClassNotifier;
import Notifiers.DetailsNotifier;
import Notifiers.InputFileNotifier;
import Notifiers.MetricsNotifier;



public class MainController {

	private  PanelContoller panel;
	
	public MainController(PanelContoller panel){
		this.panel=panel;
		this.init();
	}
	
	private void init(){
		 //final PanelContoller panel=new PanelContoller(new GridBagLayout());
		 ButtonTrigger fileUploaderButton= new ButtonTrigger("charger fichier");
		 FileUploaderInput fileUploaderInput = new FileUploaderInput("entrer un fichier ");
		 DetailsTextField detailsField=new DetailsTextField("Details");
		 DefaultListModel listModel = new DefaultListModel();
		 ClassesList classes=new ClassesList(listModel, "classes");
		 ButtonTrigger metricsLoader=new ButtonTrigger("Generer fichier csv");
		 DefaultListModel metrics = new DefaultListModel();
		 ClassesList metricsList=new ClassesList(metrics,"metriques");
		 JPanel attrPanel = new JPanel();
		 JPanel methodesPanel = new JPanel();
		 JPanel subClassesPanel = new JPanel();
		 JPanel aggrAsoPanel = new JPanel();
		 
		 
		 
		 
		 
		 //ajout des caracteristiques de chaque composante ....
		 
		 //caracteristiques du button 
		 Features fileUploaderButtonFeatures=new Features(0,0,1,1,GridBagConstraints.RELATIVE,GridBagConstraints.CENTER,
				 new Insets(0,0,0,0),0,0,0,0);
		 
		 //caracteristiques de l'input
		 Features fileUploaderInputFeatures=new Features(1,0,3,1,GridBagConstraints.HORIZONTAL,GridBagConstraints.CENTER,
				 new Insets(0,15,0,0),0,10,1,0);
		 
		 //caracteristiques du container des classes
		 Features classListFeatures=new Features(0,1,2,3,GridBagConstraints.VERTICAL,GridBagConstraints.CENTER,
				 new Insets(17,0,0,15),-205,0,0,0);
		 
		 //caracteristiques des quatre scrollPanel su milieu
		 
		 Features componentOne =new Features(2,1,1,1,GridBagConstraints.RELATIVE,GridBagConstraints.CENTER,
				 new Insets(15, 0, 15, 15),0,0,0,0);
		 
		 Features componentTwo =new Features(3,1,1,1,GridBagConstraints.RELATIVE,GridBagConstraints.CENTER,
				 new Insets(15, 0, 15, 0),0,0,0,0);
		 
		 
		 Features componentThree =new Features(2,2,1,1,GridBagConstraints.RELATIVE,GridBagConstraints.CENTER,
				 new Insets(0, 0, 15, 15),0,0,0,0);

		 Features componentFour =new Features(3,2,1,1,GridBagConstraints.RELATIVE,GridBagConstraints.CENTER,
				 new Insets(0, 0, 15, 0),0,0,0,0);
		 
		 //caracterisriques du textField  des details
		 Features DetailsFeatures =new Features(2,3,2,1,GridBagConstraints.HORIZONTAL,GridBagConstraints.CENTER,
				 new Insets(0, 0, 15, 0),0,0,1,0);
		
		 //caracteristiques du bouton des metrriques
		 Features metricsButtonFeatures =new Features(4,0,1,1,GridBagConstraints.RELATIVE,GridBagConstraints.CENTER,
				 new Insets(0, 15, 0, 0),0,0,0,0);
		 
		 //caracteristiques des metriques
		 Features metricsFeatures =new Features(4,1,1,3,GridBagConstraints.VERTICAL,GridBagConstraints.CENTER,
				 new Insets(15, 15, 0, 0),0,0,0,0);
		 
		 //l'ajout des caracteristiques de chaque composante
		 fileUploaderButton.setComponentFeatures(fileUploaderButtonFeatures);
		 fileUploaderInput.setComponentFeatures(fileUploaderInputFeatures);
		 classes.setFeatures(classListFeatures);
		 detailsField.setFeatures(DetailsFeatures);
		 metricsLoader.setFeatures(metricsButtonFeatures);
		 metricsList.setFeatures(metricsFeatures);
		 
		 //instanciation des quatres composante du milieu 
		 ScrollPane scrollPaneOne=new ScrollPane("Attributs",componentOne);	
		 ScrollPane scrollPaneTwo=new ScrollPane("Methodes",componentTwo);
		 ScrollPane scrollPaneThree=new ScrollPane("Sous Classes",componentThree);
		 ScrollPane scrollPaneFour=new ScrollPane("Associations/Aggregations",componentFour);
		 
		 //initialiser les dimentions de chaque composante
		 fileUploaderInput.setPreferredSize(new Dimension(200,15));
		 scrollPaneOne.setPreferredSize(new Dimension(200,200));
		 scrollPaneTwo.setPreferredSize(new Dimension(200,200));
		 scrollPaneThree.setPreferredSize(new Dimension(200,200));
		 scrollPaneFour.setPreferredSize(new Dimension(200,200)); 
		 detailsField.setPreferredSize(new Dimension(300,150));
		 metricsLoader.setPreferredSize(new Dimension(180,25));
		 metricsList.setPreferredSize(new Dimension(155,400));
		 
		 //l'ajout des composantes au panel
		 panel.addComponent(fileUploaderButton, fileUploaderButton.getComponentFeatures());
		 panel.addComponent(fileUploaderInput, fileUploaderInput.getComponentFeatures());
		 panel.addComponent(classes, classes.getFeatures());
		 panel.addComponent(scrollPaneOne,scrollPaneOne.getFeatures() );
		 panel.addComponent(scrollPaneTwo,scrollPaneTwo.getFeatures() );
		 panel.addComponent(scrollPaneThree,scrollPaneThree.getFeatures() );
		 panel.addComponent(scrollPaneFour,scrollPaneFour.getFeatures() );
		 panel.addComponent(detailsField, detailsField.getFeatures());
		 panel.addComponent(metricsLoader, metricsLoader.getFeatures());
		 panel.addComponent(metricsList, metricsList.getFeatures());
		 
		 //creation des observateurs
		 ClassNotifier classNotifier=new ClassNotifier();
		 DetailsNotifier detailsNotifier=new DetailsNotifier();
		 InputFileNotifier inputFileNotifier=new InputFileNotifier();
		 MetricsNotifier metricsNotifier=new MetricsNotifier();
		 
		 //l'ajout des observateurs de chaque composante 
		 fileUploaderButton.setClassNotifier(classNotifier);
		 classes.setClassNotifier(classNotifier);
		 classNotifier.addObserver(classes);
		 classes.setDetailsNotifier(detailsNotifier);
		 scrollPaneOne.setDetailsListener(detailsNotifier);
		 scrollPaneTwo.setDetailsListener(detailsNotifier);
		 scrollPaneThree.setDetailsListener(detailsNotifier);
		 scrollPaneFour.setDetailsListener(detailsNotifier);
		 detailsNotifier.addObserver(detailsField);
		 fileUploaderButton.setInputFileNotifier(inputFileNotifier);
		 inputFileNotifier.addObserver(fileUploaderInput);
		 metricsList.setDetailsNotifier(detailsNotifier);
		 metricsLoader.setAssociatedList(metricsList);
		 classes.setMetricsNotifier(metricsNotifier);
		 metricsNotifier.addObserver(metricsList); 
		 classes.setScrollPaneForAttributes(scrollPaneOne);
		 classes.setScrollPaneForMethodes(scrollPaneTwo);
		 classes.setScrollPaneForSubClasses(scrollPaneThree);
		 classes.setScrollPaneForRelations(scrollPaneFour);
		 
		 
	}

	public PanelContoller getPanel() {
		return panel;
	}

	public void setPanel(PanelContoller panel) {
		this.panel = panel;
	}

	

}
