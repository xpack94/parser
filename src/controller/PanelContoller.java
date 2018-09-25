package controller;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import Common.Features;

public class PanelContoller extends JPanel {

	private Features features;
	
	public PanelContoller() {
		super();
	}
	
	public PanelContoller(GridBagLayout layout) {
		super(layout);
	}
	public PanelContoller(Features features){
		this.features=features;
		this.setPreferredSize(new Dimension(400,400));
	}
	
	
	//fonction qui permet d'ajouter une composante dans le panel 
	public void addComponent(Component component,Features features) {
		
		//verifier que le panel utilise le gridBagLayout
		if(this.getLayout() instanceof GridBagLayout){
			
			GridBagConstraints gbc = new GridBagConstraints(features.gridx,features.gridy,features.gridwidth,
					features.gridheight,features.weightx,features.weighty,features.anchor,features.fill,
					features.insets,features.ipadx,features.ipady);
				  	this.add(component, gbc);
		}
		
	  }
	
	public Features getFeatures() {
		return this.features;
	}
	public void setFeatures(Features features) {
		this.features = features;
	}
	
	
}
