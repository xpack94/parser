package Components;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import Common.Features;

public class ScrollPane extends JScrollPane implements Observer {

	private String borderTitle;
	private Object observedClass;
	private Features features;
	private final int WIDTH=150;
	private final int HEIGHT=150;
	//prend en parametre une classe observée 
	public ScrollPane(String borderTitle,Features features){
			super();
			this.borderTitle=borderTitle;
			this.features=features;
			this.setBorder(new TitledBorder(this.borderTitle));
			
	}	
	
	public void setObservedClass(Object o){
		this.observedClass=o;
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
			
	//la fonction qui va s'executer a chaque fois que les elements de la classe observée changent 
	public void update(Observable o, Object arg) {
		// sera implementer une fois que les classes observées seront ajoutés
		
	}
	
	
	public Features getFeatures() {
		return this.features;
	}
	public void setFeatures(Features features) {
		this.features = features;
	}
	public String getBorderTitle() {
		return this.borderTitle;
	}
	public void setBorderTitle(String borderTitle) {
		this.borderTitle = borderTitle;
	}
}
