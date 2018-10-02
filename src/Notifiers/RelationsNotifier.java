package Notifiers;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import Common.ClassDao;
import Common.RelationDao;
public class RelationsNotifier extends Observable{

	List<RelationDao> relations=new ArrayList<RelationDao>();
	
	
	public RelationsNotifier(){
		super();
	}


	public List<RelationDao> getRelations() {
		return relations;
	}


	public void setRelations(List<RelationDao> relations,ClassDao chosenClass) {
		this.relations = relations;
		this.setChanged(); //l'etat de la classe a changé 
		this.notifyObservers(chosenClass); //notifier les observateurs du changement d'etat
	}
	
	
	
}
