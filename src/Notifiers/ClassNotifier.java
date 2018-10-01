package Notifiers;

import java.util.Observable;

import Common.ClassDao;

public class ClassNotifier extends Observable {

	private ClassDao classContainer;
	
	public ClassNotifier(){
		super();
	}

	public ClassDao getClassContainer() {
		return classContainer;
	}

	public void setClassContainer(ClassDao classContainer) {
		this.classContainer = classContainer;
		this.setChanged();
		//on notifie l'observateur du changement 
		this.notifyObservers();
		
	}
	
	
	
}
