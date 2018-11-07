package Components;

import java.awt.Insets;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import Common.AggregationDao;
import Common.ClassDao;
import Common.DataApi;
import Common.Features;
import Common.Metrics;
import Common.RelationDao;
import Common.RelationType;
import Notifiers.DetailsNotifier;



/**
 * classe qui correspond a la composante des details dans l'interface graphique 
 * cette classe implemente l'interface Observer et redefinie la methode update de cette derniere 
 * elle permet de faire l'affichage des details de chaque autres elements des autres composante
 * **/
public class DetailsTextField extends JTextArea implements Observer {
	Features features =new Features();
	
	public DetailsTextField(String title){
		super();
		this.setLineWrap(true);
		this.setBorder(new TitledBorder(title));
		
	}
	public DetailsTextField(String title,Features features){
		super();
		this.setLineWrap(true);
		this.setBorder(new TitledBorder(title));
		this.features=features;
	}
	
	public Features getFeatures(){
		return this.features;
	}
	public void setFeatures(Features features){
		this.features=features;
	}
	
	/**
	 * cette methode est appler  et notifier par la classe  @see DetailsNotifier 
	 * a chaque changement de valeur de cette derniere et donc cette methode et executer a chaque clique sur un des 
	 * elements des composantes des sous classe et des relations/aggregations ainsi que les elements des composantes
	 * des attributs et des methodes
	 * elle permet d'afficher les details relatif a chaque element cliqué on fonction de la composante
	 * 
	 * **/
	public void update(Observable o, Object arg) {
		//extraire la valeur 
		String selectedValue=((DetailsNotifier)o).getSelectedValue();
		String updatedType=((DetailsNotifier)o).getUpdatedType();
		String details="";
		if(selectedValue.contains(("(R)"))){
			//afficher les details de la relations
			String relationName=selectedValue.substring(selectedValue.indexOf(")")+1,selectedValue.length()).trim();
			RelationDao relation=DataApi.relations.get(relationName);
			if(relation!=null){
				details=relation.getRelationDetails();
			}
			
		}else if(selectedValue.contains("(A)")){
			//afficher les details de l'aggregation
			String containerClass=((DetailsNotifier)o).getClassContainerName();
			String selectedAggregationPart=((DetailsNotifier)o).getSelectedValue();
			String selectedPart=selectedAggregationPart.substring(selectedAggregationPart.indexOf("_")+1,selectedAggregationPart.length());
			AggregationDao selectedAggregation=DataApi.aggregations.get(containerClass) != null?DataApi.aggregations.get(containerClass):null;
			details=selectedAggregation.getAggregationDetails();
			
		}else if(updatedType.equals("Attributs")){
			System.out.println("attrs");
		}else if(updatedType.equals("Methodes")){
			System.out.println("methodess");
		}else if(updatedType.equals("Sous Classes")){
			if(DataApi.classes.get(selectedValue.trim()) !=null){
				details+=DataApi.classes.get(selectedValue.trim()).getClassDescription();
			}
			
			
		}else if(updatedType.equals("metrics") && selectedValue!=null){
			String selectedClass=((DetailsNotifier)o).getSelectedClass();
			Metrics metrics=new Metrics(selectedClass);
			details=metrics.getDefinitions().get(selectedValue.substring(0,selectedValue.lastIndexOf("=")));
		}
		this.setText("");
		this.append(details);
		
	}
	

	
}
