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
			private void setFeaturesOfComponent(){
				this.features.gridx=this.features.gridx;
				this.features.gridy=this.features.gridy;
				this.features.gridwidth=this.features.gridwidth;
				this.features.gridheight=this.features.gridheight;
				this.features.fill=this.features.fill;
				this.features.anchor=this.features.anchor;
				this.features.insets=this.features.insets;
				this.features.ipady=this.features.ipady;
				this.features.weightx=this.features.weightx;
				this.features.weighty=this.features.weighty;
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
