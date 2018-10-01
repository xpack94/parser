package Common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Notifiers.ClassNotifier;


public class UmlParser {
	
	private File fileToParse;
	private ClassNotifier classNotifier;
	
	public UmlParser(File fileToParse){
		this.fileToParse=fileToParse;
	}

	
	public byte[] readFile(){
		if(this.fileToParse !=null){
			FileReader fileReader;
			try {
				fileReader = new FileReader(this.fileToParse);
				BufferedReader bufferedReader =new BufferedReader(fileReader);
				FileInputStream fis = new FileInputStream(this.fileToParse);
				byte[] data = new byte[(int) this.fileToParse.length()];
				try {
					fis.read(data);
					fis.close();
					return data;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return new byte[1];
	}
	
	public void parseFile(String file){
		List<String> fileContentToBeParsed = new ArrayList<String>(Arrays.asList(file.split(";")));
		
		for(int i=0;i<fileContentToBeParsed.size();i++){
			//on boucle sur chaque element et on appele la methode qui gere chaque sous element
			this.filterEachSubContent(fileContentToBeParsed.get(i).trim());
		}
	}
	
	private void filterEachSubContent(String subContent){
		//on split sur les break lines de chaque element 
		List<String> subFileContent = new ArrayList<String>(
				Arrays.asList(subContent.split("\\r?\\n")));
		
		for(int i=0;i<subFileContent.size();i++){
			//on eleve tout les espaces
			subFileContent.set(i, subFileContent.get(i).trim());
			if(subFileContent.get(i).contains("CLASS")){
				this.handleClass(subFileContent.subList(i, subFileContent.size()));
				break;
			}else if(subFileContent.get(i).contains("GENERALIZATION")){
				this.handleGeneralizations(subFileContent.subList(i, subFileContent.size()));
				break;
			}else if(subFileContent.get(i).contains("RELATION")){
				this.handleRelation(subFileContent.subList(i, subFileContent.size()));
				break;
			}else if(subFileContent.get(i).contains("AGGREGATION")){
				break;
			}		
		}
	}
	
	private void handleClass(List<String> classDefinition){
		//on se debarasse de tout les espaces
		classDefinition=this.removeWhiteSpaces(classDefinition);
		String ClassName="";
		List<String> attributes=new ArrayList<String>();
		List<String> methodes=new ArrayList<String>();
		//maintenant on dispose d'un array list contenant la description d'une class
		for(int i=0;i<classDefinition.size();i++){
			if(classDefinition.get(i).contains("CLASS")){
				ClassName=classDefinition.get(i).substring(i+6,classDefinition.get(i).length());
			}else if(classDefinition.get(i).contains("ATTRIBUTES")){
				int methodesIndex=classDefinition.indexOf("OPERATIONS");
				attributes=classDefinition.subList(i+1, methodesIndex);
			}else if(classDefinition.get(i).contains("OPERATIONS")){
				 methodes=classDefinition.subList(i+1, classDefinition.size());
			}
			
		}
		
		//maintenant puisque on a extrait les attributs et les methode de la class
		//on peut les populer dans leurs classes respectives
		List<AttributeDao> classAttributes=this.populateAttributes(attributes);
		List<MethodeDao>classMethodes= (ArrayList<MethodeDao>)this.populateMethodes(methodes);
		
		//l'ajout de la class avec sa definition dans le hashMap qui contient toutes les classes
		DataApi.classes.put(ClassName, new ClassDao(ClassName, classAttributes, classMethodes));
		//ajout de la class dans le notificateur
		//ce dernier va notifier le Jlist pour qu'il se met a jours a chaque ajout de class
		this.classNotifier.setClassContainer(DataApi.classes.get(ClassName));
		}
	
	//methode qui cree une list de type AttributeDao et qui le popule avec les attribute données 
	private List<AttributeDao> populateAttributes(List<String> attributes){
		List<AttributeDao> attributesList=new ArrayList<AttributeDao>();
		if(attributes.size()>0){
			for(String attribute:attributes){
				//extraction du nom de l'attribut 
				int clIndex=attribute.indexOf(":");
				String attributeName=attribute.substring(0,clIndex).replace(",","");
				String attributeType=attribute.substring(clIndex+1,attribute.length()).replace(",","");
				attributesList.add(new AttributeDao(attributeName, attributeType));
			}
		}
		
		return attributesList;
		
	}
	
	private List<MethodeDao> populateMethodes(List<String> methodes){
		List<MethodeDao> methodesList=new ArrayList<MethodeDao>();
		if(methodes.size()>0){
			for(String methode:methodes){
				int clIndex=methode.lastIndexOf(":");
				int parametersBegIndex=methode.indexOf("(");
				int parametersEndIndex=methode.indexOf(")");
				String methodeName=methode.substring(0,parametersBegIndex);
				String methodeType=methode.substring(clIndex+1,methode.length()).replace(",", ""); 
				String parametersDescription=methode.substring(parametersBegIndex+1,parametersEndIndex);
				List<AttributeDao> methodeParameters=new ArrayList<AttributeDao>();
				if(parametersDescription.length()>0){
					List<String> parameters=Arrays.asList(parametersDescription.trim().split(","));
				    methodeParameters=this.populateAttributes(parameters);
				}
				methodesList.add(new MethodeDao(methodeName, (ArrayList<AttributeDao>) methodeParameters, methodeType));
			}
		}
		
		return methodesList;
		
			
	}
	//methode qui s'occupe de la gestion des super class
	//fait l'ajout a la liste des sous classe du parent 
	private void handleGeneralizations(List<String> generalizations){
		generalizations=this.removeWhiteSpaces(generalizations);
		String generalizationClassName="";
		
		for(String generalizationContent:generalizations){
			if(generalizationContent.contains("GENERALIZATION")){
				int generalizationNameIndex=generalizationContent.indexOf("GENERALIZATION");
				generalizationClassName= generalizationContent.substring(generalizationNameIndex+15,generalizationContent.length());
			}else if(generalizationContent.contains("SUBCLASSES")){
				int subClassesIndex=generalizationContent.indexOf("SUBCLASSES");
				List<String> subClassesContainer=Arrays.asList(
						generalizationContent.substring(subClassesIndex+11,generalizationContent.length()).split(","));
				//boucler sur chaque sous classe et l'ajouter a la liste des sous classe du parent
				for (String sub:subClassesContainer){
					ClassDao parentClass=DataApi.classes.get(generalizationClassName);
					parentClass.setSubClassToParent(DataApi.classes.get(sub.trim()));
				}
				
			}
		}
		
	}
	
	//methode qui prend une description d'une relation et qui popule le hashMap des relations 
	// ou le nom de la relation est la clé
	private void handleRelation(List<String> relation){
		relation=this.removeWhiteSpaces(relation);
		RelationDao relationDescription = null;
		String relationName="";
		for(String rel:relation){
			if(rel.contains("RELATION")){
				relationName=rel.substring(rel.indexOf("RELATION")+9,rel.length()).trim();
				relationDescription=new RelationDao(relationName);
			}else if(rel.contains("CLASS")){
				List<String>  relationBetween= new ArrayList<String>(Arrays.asList(rel.substring(rel.indexOf("CLASS")+6,rel.length()).split(" ")));
				if(relationDescription!=null && relationBetween.size()>1){
					//l'index 0 represente la class en relation 
					//l'index 1 represente le type de la relation
					ClassDao relatedClass=DataApi.classes.get(relationBetween.get(0)); 
					relationDescription.setClassRelation(new RelationType(relationBetween.get(1).replace(",", ""),relatedClass));
					//ajout de la relation dans le hashMap de toutes le relation avec le nom en clé
					DataApi.relations.put(relationName, relationDescription);
				}
				
			}
		}
		
	}
	
	private List<String> removeWhiteSpaces(List<String> classDefinition){
			for(int i=0;i<classDefinition.size();i++){
				classDefinition.set(i, classDefinition.get(i).trim());
			}
			return classDefinition;
	}
	public void setFile(File file){
		this.fileToParse=file;
	}


	public File getFileToParse() {
		return fileToParse;
	}


	public void setFileToParse(File fileToParse) {
		this.fileToParse = fileToParse;
	}


	public ClassNotifier getClassNotifier() {
		return classNotifier;
	}


	public void setClassNotifier(ClassNotifier classNotifier) {
		this.classNotifier = classNotifier;
	}
	
	
}
