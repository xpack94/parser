package Common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.crypto.Data;

public class Metrics {

	private List<String> metrics;
	private HashMap<String, String> definitions;
	private String relatedClass;
	public Metrics(){
		this.metrics=new ArrayList<String>(Arrays.asList("ANA","NOM","NOA","ITC","ETC","CAC","DIT","CLD","NOC","NOD"));
		this.definitions=new HashMap<String, String>();
		this.fillDefinitions();
	}
	public Metrics(String relatedClass){
		this.metrics=new ArrayList<String>(Arrays.asList("ANA","NOM","NOA","ITC","ETC","CAC","DIT","CLD","NOC","NOD"));
		this.definitions=new HashMap<String, String>();
		this.relatedClass=relatedClass;
		this.fillDefinitions();
		
	}
	
	private void fillDefinitions(){
			this.definitions.put("ANA", " Nombre moyen d’arguments des méthodes locales pour la classe "+relatedClass);
			this.definitions.put("NOM"
					," Nombre de méthodes locales/héritées de la classe "+relatedClass+". Dans le cas où une méthode est héritée et redéfinie" +
							" localement (même nom, même ordre et types des arguments et même type de retour), elle ne compte qu’une fois.");
			this.definitions.put("NOA", " Nombre d’attributs locaux/hérités de la classe "+relatedClass);
			this.definitions.put("ITC"," Nombre de fois où d’autres classes du diagramme apparaissent comme types des arguments des méthodes de "+relatedClass);
			this.definitions.put("ETC"," Nombre de fois où "+relatedClass+" apparaît comme type des arguments dans les méthodes des autres classes du diagramme.");
			this.definitions.put("CAC",": Nombre d’associations (incluant les agrégations) locales/héritées auxquelles participe une classe "+relatedClass);
			this.definitions.put("DIT","Taille du chemin le plus long reliant une classe ci à une classe racine dans le graphe d’héritage");
			this.definitions.put("CLD", "Taille du chemin le plus long reliant une classe "+relatedClass+" à une classe feuille dans le graphe d’héritage");
			this.definitions.put("NOC","Nombre de sous-classes directes de "+relatedClass);
			this.definitions.put("NOD", " Nombre de sous-classes directes et indirectes de "+relatedClass);
	}
	
	
	public String metricsCalculator(String relatedClass,String metricToCalculate){
		float result=0;
		switch (metricToCalculate){
			case"ANA":
				result=this.calculateAna(relatedClass);
				break;
			case "NOM":
				result=this.calculateNom(relatedClass);
				break;
			case "NOA":
				result=this.calculateNoa(relatedClass);
				break;
			case "ITC":
				result=this.calculateItc(relatedClass);
				break;
			case "ETC":
				result=this.calculateEtc(relatedClass);
				break;
			case "CAC":
				result=this.calculateCac(relatedClass);
				break;
			case "DIT":
				result=this.calculateDit(relatedClass);
				break;
			case "CLD":
				result=this.calculateCld(relatedClass);
				break;
			case "NOC":
				result=this.calculateNoc(relatedClass);
				break;
			case "NOD":
				result=this.calculateNod(relatedClass);
				break;
		}
		
		return ""+result;
	}
	

	
	private float calculateNod(String relatedClass) {
		ClassDao classToCalculateFor=DataApi.classes.get(relatedClass);
		if(classToCalculateFor!=null){
			//on calcule le nombre de classe directes et indirectes
			for(ClassDao subClass:classToCalculateFor.getSubClasses()){
				if(classToCalculateFor.getSubClasses().size()==0){
					return 0;
				}else{
					return classToCalculateFor.getSubClasses().size()+this.calculateNod(subClass.getName());
				}
			}	
		}
		
		return 0;
	}
	private float calculateNoc(String relatedClass) {
		ClassDao classToCalculateFor=DataApi.classes.get(relatedClass);
		if(classToCalculateFor!=null){
			// on calcule le NOC de la classe choisit
			return classToCalculateFor.getSubClasses().size();
		}
		return 0;
	}
	private float calculateCld(String relatedClass2) {
		// TODO Auto-generated method stub
		return 0;
	}
	private float calculateDit(String relatedClass2) {
		// TODO Auto-generated method stub
		return 0;
	}
	private float calculateCac(String relatedClass) {
		ClassDao classToCalculateFor=DataApi.classes.get(relatedClass);
		int cac=0;
		if(classToCalculateFor!=null){
			//on compte le nombre d'associations que la class fait partie
			if(classToCalculateFor.getRelations()!=null && classToCalculateFor.getRelations().size()>0){
				cac+=classToCalculateFor.getRelations().size();
			}
			
			if(classToCalculateFor.getAggregations()!=null && classToCalculateFor.getAggregations().size()>0){
				cac+=classToCalculateFor.getAggregations().size();
				
			}else if(DataApi.aggregations.get(classToCalculateFor.getName())!=null){
				//cette class est une container d'une aggregation
				cac++;
			}
			
			if(classToCalculateFor.getParentClass()!=null){
				//une classe parent existe donc on fait le meme calcule pour cette derniere
				return cac+this.calculateCac(classToCalculateFor.getParentClass().getName());
			}else{
				//cette classe n'hérite d'aucune autre classe 
				return cac;
			}
		}
		
		
		return cac;
	}
	private float calculateEtc(String relatedClass) {
		ClassDao classToCalculateFor=DataApi.classes.get(relatedClass);
		int etc=0;
		if(classToCalculateFor!=null){
			for(String key:DataApi.classes.keySet()){
				// on s'assure qu'on inclut pas la classe elle meme
				ClassDao classFound=DataApi.classes.get(key);
				if(classFound!=classToCalculateFor){
					for (MethodeDao methode:classFound.getMethodes()){
						for(AttributeDao attr:methode.getParameters()){
							if(attr.getAttributeType().trim().equals(classToCalculateFor.getName())){
								etc++;
							}
						}
						
						
					}
				}
			}
			
		}
		
		return etc;
	}
	
	private float calculateItc(String relatedClass) {
		List<String> primitiveTypes=new ArrayList<String>(Arrays.asList("integer","int","float","double","char","byte","short",
				"long","string","boolean","bool"));
		ClassDao classToCalculateFor=DataApi.classes.get(relatedClass);
		int itc=0;
		if(classToCalculateFor!=null){
			for(MethodeDao methode:classToCalculateFor.getMethodes()){
				for(AttributeDao attr:methode.getParameters()){
					if(primitiveTypes.indexOf(attr.getAttributeType().toLowerCase())==-1){
						//le type trouvé n'est pas un type primitive
						if(DataApi.classes.get(attr.getAttributeType().trim())!=null){
							//le type correspond a un type d'une classe du diagrame
							itc++;
						}
					}
				}
				
			}
		}
		
		return itc;
	}
	private float calculateNoa(String relatedClass) {
		ClassDao classToCalculateFor=DataApi.classes.get(relatedClass);
		if(classToCalculateFor!=null){
			if(classToCalculateFor.getParentClass()!=null){
				return classToCalculateFor.getAttributes().size()+this.calculateNoa(classToCalculateFor.getParentClass().getName());
				
			}else{
				return classToCalculateFor.getAttributes().size();
			}
			
		}
		return 0;
	}
	
	
	private float calculateAna(String relatedClass){
		ClassDao classToCalculateFor=DataApi.classes.get(relatedClass);
		int numberOfAttrs=0;
		if(classToCalculateFor!=null){
			
			for(MethodeDao methode:classToCalculateFor.getMethodes()){
				numberOfAttrs+=methode.getParameters().size();
			}
		}
		return numberOfAttrs;
	}
	
	private float calculateNom(String relatedClass){
	 return 0;	
	}
	
	

	public List<String> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<String> metrics) {
		this.metrics = metrics;
	}

	public HashMap<String, String> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(HashMap<String, String> definitions) {
		this.definitions = definitions;
	}	
	
	
	
	
}
