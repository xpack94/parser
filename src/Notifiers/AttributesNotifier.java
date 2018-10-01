package Notifiers;


import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import Common.AttributeDao;
public class AttributesNotifier extends Observable{

	private List<AttributeDao> atrributes=new ArrayList<AttributeDao>();
	
	public AttributesNotifier(){
		super();
	}

	public List<AttributeDao> getAtrributes() {
		return atrributes;
	}

	public void setAtrributes(List<AttributeDao> atrributes) {
		this.atrributes = atrributes;
		this.setChanged(); //l'etat de la classe a été changée 
		this.notifyObservers(); // notifier les observateurs de cette classe du changement
	}
	
	

}

