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
			this.definitions.put("ANA", " Nombre moyen d’arguments des méthodes locales pour la classe "+relatedClass!=null?relatedClass+".":"");
			this.definitions.put("NOM"
					," Nombre de méthodes locales/héritées de la classe "+relatedClass!=null?relatedClass:""+". Dans le cas où une méthode est héritée et redéfinie" +
							" localement (même nom, même ordre et types des arguments et même type de retour), elle ne compte qu’une fois.");
			this.definitions.put("NOA", " Nombre d’attributs locaux/hérités de la classe "+relatedClass!=null?relatedClass+".":"");
			this.definitions.put("ITC"," Nombre de fois où d’autres classes du diagramme apparaissent comme types des arguments des méthodes de "+relatedClass!=null?relatedClass+".":"");
			this.definitions.put("ETC"," Nombre de fois où "+relatedClass!=null?relatedClass:""+" apparaît comme type des arguments dans les méthodes des autres classes du diagramme.");
			this.definitions.put("CAC",": Nombre d’associations (incluant les agrégations) locales/héritées auxquelles participe une classe "+relatedClass!=null?relatedClass+".":"");
			this.definitions.put("DIT","Taille du chemin le plus long reliant une classe ci à une classe racine dans le graphe d’héritage");
			this.definitions.put("CLD", "Taille du chemin le plus long reliant une classe "+relatedClass!=null?relatedClass:""+"à une classe feuille dans le graphe d’héritage");
			this.definitions.put("NOC","Nombre de sous-classes directes de "+relatedClass!=null?relatedClass+".":"");
			this.definitions.put("NOD", " Nombre de sous-classes directes et indirectes de "+relatedClass!=null?relatedClass+".":"");
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
