package Notifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import Common.AggregationDao;

public class AggregationsNotifier extends Observable {

	private List<AggregationDao> aggregations=new ArrayList<AggregationDao>();
	
	public AggregationsNotifier(){
		super();
	}

	public List<AggregationDao> getAggregations() {
		return aggregations;
	}

	public void setAggregations(List<AggregationDao> aggregations) {
		this.aggregations = aggregations;
		this.setChanged(); //l"etat a été changée
		this.notifyObservers(); //notifier les observateur du changement d'etat
	}
	
	
	
	
	
}
