package Notifiers;

import java.util.Observable;

public class InputFileNotifier extends Observable {

	private String fileName;
	public InputFileNotifier(){
		super();
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
		this.setChanged(); //l'etat de l'input a changé
		this.notifyObservers() ; //notifier les observateurs du changement d'etat 
	}
	
	
	
}
