package Notifiers;
import Common.ClassDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
public class SubClassesNotifier extends Observable{

	private List<ClassDao> subClasses=new ArrayList<ClassDao>();
	
	public SubClassesNotifier(){
		super();
	}

	public List<ClassDao> getSubClasses() {
		return subClasses;
	}

	public void setSubClasses(List<ClassDao> subClasses) {
		this.subClasses = subClasses;
		this.setChanged(); // l'etat de la classe a changé 
		this.notifyObservers() ;//notifier les observateurs de cette classe que l'etat a changé
	}
	
	
	
	
}
