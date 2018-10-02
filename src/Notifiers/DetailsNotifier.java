package Notifiers;

import java.util.Observable;

import Common.ClassDao;

public class DetailsNotifier extends Observable{

	String selectedValue;
	
	public DetailsNotifier(){
		super();
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
		this.setChanged(); //l'etat de la classe a changée
		this.notifyObservers(); //notifier les observateurs du changement d'etat
	}
	
	
	
}
