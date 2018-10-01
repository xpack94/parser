package Notifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import Common.MethodeDao;

public class MethodeNotifier extends Observable {

	List<MethodeDao> methodes=new ArrayList<MethodeDao>();
	public MethodeNotifier(){
		super();
	}
	public List<MethodeDao> getMethodes() {
		return methodes;
	}
	public void setMethodes(List<MethodeDao> methodes) {
		this.methodes = methodes;
		this.setChanged(); // l'etat de la classe a changé 
		this.notifyObservers(); //notifier les observateur de cette classe que l'etat a été changée
	}
	
	
	
	
	
}
