package controller;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;

import Common.Features;
import Components.ClassesList;
import Components.FileUploaderButton;
import Components.FileUploaderInput;
import Components.ScrollPane;

public class MainController {


	public static void main(String[] args) {
		
		 final PanelContoller panel=new PanelContoller(new GridBagLayout());
		 FileUploaderButton fileUploaderButton= new FileUploaderButton();
		 FileUploaderInput fileUploaderInput = new FileUploaderInput("test");
		 DefaultListModel listModel = new DefaultListModel();
		 listModel.addElement("joueur");
		 listModel.addElement("equipe");
		 listModel.addElement("participant");
		 listModel.addElement("entraineur");
		 listModel.addElement("stade");
		 //creation d'un sub panel a l'interieur du grand panel 
		 //se panel contiendera les 4 compansantes au milieu 
		 PanelContoller innerPanel = new PanelContoller(new GridBagLayout());
		 
		 //adding the features of all four component that are inside the inner panl
		 Features componentOne =new Features(0,0,1,1,GridBagConstraints.RELATIVE,GridBagConstraints.CENTER,
				 new Insets(0, 0, 50, 15),500,100,0,0);
		 
		 Features componentTwo =new Features(1,0,1,1,GridBagConstraints.RELATIVE,GridBagConstraints.CENTER,
				 new Insets(0, 0, 50, 0),500,100,0,0);
		 
		 Features componentThree =new Features(0,1,1,1,GridBagConstraints.RELATIVE,GridBagConstraints.CENTER,
				 new Insets(0, 0, 0, 15),500,100,0,0);

		 Features componentFour =new Features(1,1,1,1,GridBagConstraints.RELATIVE,GridBagConstraints.CENTER,
				 new Insets(0, 0, 0, 0),500,100,1,0);
		 
		 //instanciating the four components
		 ScrollPane scrollPaneOne=new ScrollPane("Attributs",componentOne);
		 ScrollPane scrollPaneTwo=new ScrollPane("Methodes",componentTwo);
		 ScrollPane scrollPaneThree=new ScrollPane("Sous Classes",componentThree);
		 ScrollPane scrollPaneFour=new ScrollPane("Associations/Aggregations",componentFour);
		 
		 //adding the components to the inner panel
		 innerPanel.addComponent(scrollPaneOne,scrollPaneOne.getFeatures() );
		 innerPanel.addComponent(scrollPaneTwo,scrollPaneTwo.getFeatures() );
		 innerPanel.addComponent(scrollPaneThree,scrollPaneThree.getFeatures() );
		 innerPanel.addComponent(scrollPaneFour,scrollPaneFour.getFeatures() );
		 
		 //adding innerPanel features
		 Features innerPanelFeatures =new Features(1,1,2,2,GridBagConstraints.RELATIVE,GridBagConstraints.CENTER,
				 new Insets(15, 0, 0, 15),0,0,0,0);
		 innerPanel.setFeatures(innerPanelFeatures);
		
		 panel.add(innerPanel,innerPanel.getFeatures());
		 
		 
		 ClassesList classes=new ClassesList(listModel, "classes");
		 panel.addComponent(fileUploaderButton, fileUploaderButton.getComponentFeatures());
		 panel.addComponent(fileUploaderInput, fileUploaderInput.getComponentFeatures());
		 panel.addComponent(classes, classes.getFeatures());
		  javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	JFrame frame=FrameFactory.getFrame();
	            	frame.add(panel);
	            	frame.setVisible(true);
	            }
	        });
		 

	}

}
