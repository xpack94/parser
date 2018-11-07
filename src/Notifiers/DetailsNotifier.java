package Notifiers;

import java.util.Observable;

import Common.ClassDao;


/***
 * cette classe joue le role de notificateur car elle permet de notifier la classe @see DetailsTextFiel a chaque changement 
 * ou selection des elements des autres composantes 
 * */
public class DetailsNotifier extends Observable{

	String selectedValue;
	String ClassContainerName;
	String updatedType;
	String selectedClass;
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

	/**
	 * @param selectedValue qui correspond a un element d'un composant qui est selectionné
	 * cette methode notifie la classe @see detailsTextFiled pour qu'il affiche les details de l'element selectedValue qui est selectionné
	 * 
	 * **/
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
	public String getSelectedClass(){
		return this.selectedClass;
	}
	
	public void setSelectedClass(String selectedClass){
		this.selectedClass=selectedClass;
	}
	
	
}
