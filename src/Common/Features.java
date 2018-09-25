package Common;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class Features extends GridBagConstraints {
	
	//cette classe est utiliser permet de donner toutes les caracteristiques d'une composante qui l'instancie
	public Features(){
		super();
	}
	
	public Features(int gridX,int gridY,int gridWidth,int gridHeight,int fill,int anchor,Insets insets,
			int ipadX,int ipadY,float weightX,float weightY){
		
		this.gridx=gridX;
		this.gridy=gridY;
		this.weightx=weightx;
		this.weighty=weighty;
		this.ipadx=ipadX;
		this.ipady=ipadY;
		this.anchor=anchor;
		this.fill=fill;
		this.insets=insets;
		this.gridwidth=gridWidth;
		this.gridheight=gridHeight;
	}

	
	
}
