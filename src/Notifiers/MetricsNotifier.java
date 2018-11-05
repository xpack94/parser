package Notifiers;

import java.util.Observable;

import Common.ClassDao;

public class MetricsNotifier extends Observable{

	private String selectedClass;

	public String getSelectedClass() {
		return selectedClass;
	}

	public void setSelectedClass(String selectedClass) {
		
		this.selectedClass = selectedClass;
		this.setChanged();
		this.notifyObservers(this.selectedClass);
	}
	
	
	
	
}
