package Notifiers;

import java.util.Observable;

import Common.ClassDao;


/**
 * @version 1.0
 * 
 * classe sui joue le role de classe notificatrice et qui permet de notifier le composant @see ClassList
 *  apres chaque appel a la methode setContainer 
 *  cette classe extends la class Observable et utilise donc le pattern de l'observateur
 * */
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
