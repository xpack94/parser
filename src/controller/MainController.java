package controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;

import Common.Features;
import Components.ClassesList;
import Components.DetailsTextField;
import Components.FileUploaderButton;
import Components.FileUploaderInput;
import Components.ScrollPane;
import Notifiers.AttributesNotifier;
import Notifiers.ClassNotifier;
import Notifiers.MethodeNotifier;

public class MainController {


	public static void main(String[] args) {
		
		 final PanelContoller panel=new PanelContoller(new GridBagLayout());
		 FileUploaderButton fileUploaderButton= new FileUploaderButton();
		 FileUploaderInput fileUploaderInput = new FileUploaderInput("test");
		 DetailsTextField detailsField=new DetailsTextField("Details");
		 DefaultListModel listModel = new DefaultListModel();
		 ClassesList classes=new ClassesList(listModel, "classes");
		 
		 
		 //ajout des caracteristiques de chaque composante ....
		 
		 //caracteristiques du button 
		 Features fileUploaderButtonFeatures=new Features(0,0,1,1,GridBagConstraints.RELATIVE,GridBagConstraints.CENTER,
				 new Insets(0,0,0,0),0,0,0,0);
		 
		 //caracteristiques de l'input
		 Features fileUploaderInputFeatures=new Features(1,0,3,1,GridBagConstraints.HORIZONTAL,GridBagConstraints.CENTER,
				 new Insets(0,0,0,0),0,10,1,0);
		 
		 //caracteristiques du container des classes
		 Features classListFeatures=new Features(0,1,2,3,GridBagConstraints.VERTICAL,GridBagConstraints.CENTER,
				 new Insets(0,0,0,15),-205,0,0,0);
		 
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
		 
		 
		 //l'ajout des caracteristiques de chaque composante
		 fileUploaderButton.setComponentFeatures(fileUploaderButtonFeatures);
		 fileUploaderInput.setComponentFeatures(fileUploaderInputFeatures);
		 classes.setFeatures(classListFeatures);
		 detailsField.setFeatures(DetailsFeatures);
		 
		 //instanciation des quatres composante du milieu 
		 ScrollPane scrollPaneOne=new ScrollPane("Attributs",componentOne);	
		 ScrollPane scrollPaneTwo=new ScrollPane("Methodes",componentTwo);
		 ScrollPane scrollPaneThree=new ScrollPane("Sous Classes",componentThree);
		 ScrollPane scrollPaneFour=new ScrollPane("Associations/Aggregations",componentFour);
		 
		 //initialiser les dimentions de chaque composante
		 fileUploaderInput.setPreferredSize(new Dimension(250,15));
		 scrollPaneOne.setPreferredSize(new Dimension(150,150));
		 scrollPaneTwo.setPreferredSize(new Dimension(150,150));
		 scrollPaneThree.setPreferredSize(new Dimension(150,150));
		 scrollPaneFour.setPreferredSize(new Dimension(150,150)); 
		 detailsField.setPreferredSize(new Dimension(300,150));
		 
		 //l'ajout des composatne au panel
		 panel.addComponent(fileUploaderButton, fileUploaderButton.getComponentFeatures());
		 panel.addComponent(fileUploaderInput, fileUploaderInput.getComponentFeatures());
		 panel.addComponent(classes, classes.getFeatures());
		 panel.addComponent(scrollPaneOne,scrollPaneOne.getFeatures() );
		 panel.addComponent(scrollPaneTwo,scrollPaneTwo.getFeatures() );
		 panel.addComponent(scrollPaneThree,scrollPaneThree.getFeatures() );
		 panel.addComponent(scrollPaneFour,scrollPaneFour.getFeatures() );
		 panel.addComponent(detailsField, detailsField.getFeatures());
		 
		 //creation des observateurs
		 ClassNotifier classNotifier=new ClassNotifier();
		 AttributesNotifier attributesNotifier=new AttributesNotifier();
		 MethodeNotifier methodesNotifier=new MethodeNotifier();
		 
		 //l'ajout des observateurs de chaque composante 
		 fileUploaderButton.setClassNotifier(classNotifier);
		 classes.setClassNotifier(classNotifier);
		 classNotifier.addObserver(classes);
		 classes.setAttributesNotifier(attributesNotifier);
		 classes.setMethodesNotifier(methodesNotifier);
		 attributesNotifier.addObserver(scrollPaneOne);
		 methodesNotifier.addObserver(scrollPaneTwo);
		 
		 
		 javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	JFrame frame=FrameFactory.getFrame();
	            	frame.add(panel);
	            	frame.setVisible(true);
	            }
	        });
		 

	}

}
