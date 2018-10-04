package Notifiers;

import java.util.Observable;

import Common.ClassDao;

public class ClassNotifier extends Observable {

	private ClassDao classContainer;
	private boolean ShouldRemoveClass;
	
	public ClassNotifier(){
		super();
	}

	public ClassDao getClassContainer() {
		return classContainer;
	}

	public void setClassContainer(ClassDao classContainer) {
		this.classContainer = classContainer;
		//cela va le marqué comme etant changé 
		this.setChanged();
		//on notifie l'observateur du changement 
		this.notifyObservers();
		
	}

	public boolean isShouldRemoveClass() {
		return ShouldRemoveClass;
	}

	public void setShouldRemoveClass(boolean shouldRemoveClass) {
		ShouldRemoveClass = shouldRemoveClass;
		if(this.ShouldRemoveClass){
			this.setChanged();
			this.notifyObservers();
		}
		
	}
	
	
	
}
