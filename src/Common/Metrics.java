package Common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
		String result="";
		System.out.println("metric to calculate is "+relatedClass);
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
		
		return result;
	}
	

	
	private String calculateNod(String relatedClass2) {
		// TODO Auto-generated method stub
		return null;
	}
	private String calculateNoc(String relatedClass2) {
		// TODO Auto-generated method stub
		return null;
	}
	private String calculateCld(String relatedClass2) {
		// TODO Auto-generated method stub
		return null;
	}
	private String calculateDit(String relatedClass2) {
		// TODO Auto-generated method stub
		return null;
	}
	private String calculateCac(String relatedClass2) {
		// TODO Auto-generated method stub
		return null;
	}
	private String calculateEtc(String relatedClass2) {
		// TODO Auto-generated method stub
		return null;
	}
	private String calculateItc(String relatedClass2) {
		// TODO Auto-generated method stub
		return null;
	}
	private String calculateNoa(String relatedClass2) {
		// TODO Auto-generated method stub
		return null;
	}
	private String calculateAna(String relatedClass){
		
		return "";
	}
	
	private String calculateNom(String relatedClass){
	 return null;	
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
