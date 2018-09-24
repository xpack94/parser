package Controler;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import Common.Features;

public class PanelContoler extends JPanel {

	
	public PanelContoler() {
		super();
	}
	
	public PanelContoler(GridBagLayout layout) {
		super(layout);
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
	
}
