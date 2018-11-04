package Notifiers;

import java.util.Observable;

import Common.ClassDao;

public class DetailsNotifier extends Observable{

	String selectedValue;
	String ClassContainerName;
	String updatedType;
	public DetailsNotifier(){
		super();
	}
	
	public String getSelectedValue() {
		return selectedValue;
	}
	
	public String getUpdatedType() {
		return updatedType;
	}


	public void setUpdatedType(String updatedType) {
		this.updatedType = updatedType;
	}


	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
		this.setChanged(); //l'etat de la classe a changée
		this.notifyObservers(); //notifier les observateurs du changement d'etat
	}

	public String getClassContainerName() {
		return ClassContainerName;
	}

	public void setClassContainerName(String classContainerName) {
		ClassContainerName = classContainerName;
	}
	
	
	
}
