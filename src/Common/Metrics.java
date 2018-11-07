package Common;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.crypto.Data;

import controller.FrameFactory;

/**
 * @version 1.0
 * 
 * cette classe contient la definition de tout les metriques ainsi que les methode qui permettent de les calculer 
 * elle contient aussi une methode qui permet la creation d'un fichier csv pour visualiser tout les metriques de toutes 
 * les classe du fichier 
 * 
 * **/
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
	/**
	 * @return void 
	 * methode qui definie chaque metrique et la sauvegarde dans un hashMap
	 * */
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
	
	/**
	 * @param relatedClass de type string qui correspond a la classe pour laquelle on veux calculer une metrique
	 * @param metricToCalculate de type string qui correspond a la metrique qu'on veut calculer
	 * @return String qui correspond au resultat de la metrique calculée
	 *  
	 * cette methode permet de faire l'appel a la bonne methode pour faire le calcule de la metrique voulue
	 * 
	 * **/
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
	

	/**
	 * @param relatedClass qui correspond a la  classe pour laquelle on veut calculer la metrique Ana 
	 * @return float qui correspond au resultat du calcule 
	 * 
	 * cette methode fait le calcule de la metrique ana 
	 * 
	 * 
	 * */
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
	
	/**
	 *@param relatedClass qui correspond a la  classe pour laquelle on veut calculer la metrique Noc
	 * @return float qui correspond au resultat du calcule  
	 * **/
	private float calculateNoc(String relatedClass) {
		ClassDao classToCalculateFor=DataApi.classes.get(relatedClass);
		if(classToCalculateFor!=null){
			// on calcule le NOC de la classe choisit
			return classToCalculateFor.getSubClasses().size();
		}
		return 0;
	}
	/**
	 * @param relatedClass qui correspond a la  classe pour laquelle on veut calculer la metrique Cid 
	 * @return float qui correspond au resultat du calcule 
	 * cette methode fait le calcule de la metrique cid
	 * **/
	private float calculateCld(String relatedClass) {
		return this.cidCalculator(relatedClass,0,0);
	}
	
	private int cidCalculator(String relatedClass,int count,int max){
		ClassDao classToCalculateFor=DataApi.classes.get(relatedClass);
		int cid=count;
		int maximum=max;
		if(classToCalculateFor!=null){
			for(ClassDao subClass:classToCalculateFor.getSubClasses()){
				cid=cidCalculator(subClass.getName(),count+1,max);
				maximum=cid>maximum?cid:maximum;
			}
		}
		
		return count>maximum?count:maximum;
	}
	
	/**
	 * @param relatedClass qui correspond a la  classe pour laquelle on veut calculer la metrique dit 
	 * @return float qui correspond au resultat du calcule 
	 * 
	 * cette methode fait le calcule de la metrique Dit 
	 * 
	 * **/
	private float calculateDit(String relatedClass) {
		ClassDao classToCalculateFor=DataApi.classes.get(relatedClass);
		int dit=0;
		if(classToCalculateFor!=null){
			while(classToCalculateFor.getParentClass()!=null){
				dit++;
				classToCalculateFor=classToCalculateFor.getParentClass();
			}
		}
		return dit;
	}
	
	
	/**
	 * @param relatedClass qui correspond a la  classe pour laquelle on veut calculer la metrique Cac 
	 * @return float qui correspond au resultat du calcule 
	 * 
	 * cette methode fait le calcule de la metrique Cac 
	 * 
	 * **/
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
	
	/***
	 * @param relatedClass qui correspond a la  classe pour laquelle on veut calculer la metrique Etc 
	 * @return float qui correspond au resultat du calcule 
	 * 
	 * cette methode fait le calcule de la metrique Etc 
	 * 
	 * */
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
	/**
	 * @param relatedClass qui correspond a la  classe pour laquelle on veut calculer la metrique Itc 
	 * @return float qui correspond au resultat du calcule 
	 * 
	 * cette methode fait le calcule de la metrique Itc 
	 * 
	 * 
	 * **/
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
	
	/**
	 * @param relatedClass qui correspond a la  classe pour laquelle on veut calculer la metrique Noa 
	 * @return float qui correspond au resultat du calcule 
	 * 
	 * cette methode fait le calcule de la metrique Noa 
	 * 
	 * 
	 * **/
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
	
	/**
	 * @param relatedClass qui correspond a la  classe pour laquelle on veut calculer la metrique Ana 
	 * @return float qui correspond au resultat du calcule 
	 * 
	 * cette methode fait le calcule de la metrique ana 
	 * 
	 * 
	 * **/
	public float calculateAna(String relatedClass){
		ClassDao classToCalculateFor=DataApi.classes.get(relatedClass);
		int numberOfAttrs=0;
		if(classToCalculateFor!=null){
			System.out.println(relatedClass);
			for(MethodeDao methode:classToCalculateFor.getMethodes()){
				numberOfAttrs+=methode.getParameters().size();
			}
		}
		float finalAna=0;
		if(numberOfAttrs>0){
			finalAna=(float)numberOfAttrs/classToCalculateFor.getMethodes().size();
		}
		
		return  Float.parseFloat(new DecimalFormat("##.##").format(finalAna));
	}
	/**
	 * @param relatedClass qui correspond a la  classe pour laquelle on veut calculer la metrique Nom 
	 * @return float qui correspond au resultat du calcule 
	 * 
	 * cette methode fait le calcule de la metrique Nom 
	 * 
	 * **/
	private float calculateNom(String relatedClass){
		ClassDao classToCalculateFor=DataApi.classes.get(relatedClass);
		int nom=0;
		if(classToCalculateFor!=null){
			nom+=classToCalculateFor.getMethodes().size();
			while(classToCalculateFor.getParentClass()!=null){
				List<MethodeDao> methodes=classToCalculateFor.getMethodes();
				classToCalculateFor=classToCalculateFor.getParentClass();
				int numberOfOverridenMethodes=this.doesOverride(classToCalculateFor.getMethodes(),methodes);
				nom+=classToCalculateFor.getMethodes().size()-numberOfOverridenMethodes;
			}
		}
		
		
	 return nom;	
	}
	
	/**
	 * @param methodes: une liste d'elements  du type MethodeDto
	 * @param currentMethodes: une liste d'elements du type MethodeDto
	 * @return int qui correspond au nombre de methode qui sont redifinie localement dans une sous classe  
	 * 
	 * */
	private int doesOverride(List<MethodeDao> methodes,List<MethodeDao> currentMethodes){
		int numberOfOverridenMethoes=0;
		List<MethodeDao> methodeToUse=null;
		List<MethodeDao> otherMethodes=null;
		if(methodes.size()>=currentMethodes.size()){
			methodeToUse=currentMethodes;
			otherMethodes=methodes;
		}else{
			methodeToUse=methodes;
			otherMethodes=currentMethodes;
		}
		for(int i =0;i<methodeToUse.size();i++){
			if(otherMethodes.get(i).getMethodeName().equals(methodeToUse.get(i).getMethodeName())
					&& currentMethodes.get(i).getReturnType().equals(methodes.get(i).getReturnType())){
				List<AttributeDao> methodeParams=methodeToUse.get(i).getParameters();
				List<AttributeDao> currentMethodeParams=otherMethodes.get(i).getParameters();
				if(methodeParams.size()==currentMethodeParams.size()){
					int j=0;
					for(j=0;j<methodeParams.size();j++){
						//on verifie si il ont le meme ordre et types des arguments
						if(!methodeParams.get(j).getAttributeName().equals(currentMethodeParams.get(j).getAttributeName()) ||
								!methodeParams.get(j).getAttributeType().equals(currentMethodeParams.get(j).getAttributeType())){
							break;
						}
					}
					if(j==methodeParams.size()){
						numberOfOverridenMethoes++;
					}
				}
				
			}
		}//
		return numberOfOverridenMethoes;
	}
	
	/**
	 * methode qui permet de generer le fichier csv qui donne le resulats de toutes les metriques pour chaque classe
	 * fait l'ajout de la premiere ligne qui contient les nom de toutes les classes separer par des virgules
	 * fait appel a la methode @see  buildCsv
	 * 
	 * **/
	public void generateCsv(){
		if(DataApi.classes.size()>0){
			try {
				
				PrintWriter writer = new PrintWriter("metrics.csv", "UTF-8");
				List<ClassDao> classes=new ArrayList<ClassDao>();
				String row="       ,";
				for(String c:DataApi.classes.keySet()){
					classes.add(DataApi.classes.get(c));
					row+=DataApi.classes.get(c).getName()+",";
				}
				row=row.substring(0,row.length()-1); // on supprime la derniere virgule
				row+="\r\n";
				row+=this.buildCsv(classes);
				writer.print(row);
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			// aucune classe trouvée
			JOptionPane.showMessageDialog(FrameFactory.getFrame(), "aucune classe trouvée");
		}
		
		
		
	}
	/**
	 * 
	 * @param List<ClassDao> contenant toutes les classe du fichier 
	 * boucle sur toutes les metriques a calculer qui se trouve dans l'attribut metrics 
	 * boucle sur  les classes données et  appel la methode @see metricsCalculator qui calcule 
	 * la metrique en question pour chaque classe 
	 * 
	 * 
	 * **/
	private String buildCsv(List<ClassDao> classes){
		String row="";
		for(String metricToCalculate:this.metrics){
			row+=metricToCalculate+",";
			for(ClassDao relatedClass:classes){
				row+=this.metricsCalculator(relatedClass.getName(), metricToCalculate)+",";
			}
			row=row.substring(0,row.length()-1); // on supprime la derniere virgule
			row+="\r\n";
		}
		return row;
		
		
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
