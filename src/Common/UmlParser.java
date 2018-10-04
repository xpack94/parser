package Common;

import java.awt.JobAttributes;
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import controller.FrameFactory;

import Notifiers.ClassNotifier;


public class UmlParser {
	
	private File fileToParse;
	private ClassNotifier classNotifier;
	private boolean ERROR_ENCOUNTERED=false;
	
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
		//initialisation du Notifier du  ClassList
		this.classNotifier.setShouldRemoveClass(false); 
		List<String> fileContentToBeParsed = new ArrayList<String>(Arrays.asList(file.split(";")));
		
		for(int i=0;i<fileContentToBeParsed.size();i++){
			if(this.ERROR_ENCOUNTERED){
				//si une erreur a été détecté lors du parsing on arrete 
				this.ERROR_ENCOUNTERED=false; //on le remet a jours
				
				//true veut dire qu'on doit supprimer toutes les classes du ClassList
				//a cause d'une erreur lors du parsing
				this.classNotifier.setShouldRemoveClass(true);
				this.reset(); //remet a jours le DataApi
				break;
			}
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
				this.handleAggregations(subFileContent.subList(i, subFileContent.size()));
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
		//on peut les peupler dans leurs classes respectives
		List<AttributeDao> classAttributes=this.populateAttributes(attributes);
		List<MethodeDao>classMethodes= (ArrayList<MethodeDao>)this.populateMethodes(methodes);
		
		if(this.checkDuplicates(classAttributes)){
			JOptionPane.showMessageDialog(FrameFactory.getFrame(), "duplication d'attribut dans la classe "+ClassName
					,"Message D'erreur",JOptionPane.ERROR_MESSAGE);
			this.ERROR_ENCOUNTERED=true;
			return;
			
		}
		
		if(DataApi.classes.get(ClassName)!=null){
			JOptionPane.showMessageDialog(FrameFactory.getFrame(), "duplication de la classe "+ClassName,"Message D'erreur",JOptionPane.ERROR_MESSAGE);
			this.ERROR_ENCOUNTERED=true;
			return;
		}
		//l'ajout de la class avec sa definition dans le hashMap qui contient toutes les classes
		DataApi.classes.put(ClassName, new ClassDao(ClassName, classAttributes, classMethodes));
		//ajout de la class dans le notificateur
		//ce dernier va notifier le ClassList pour qu'il se met a jours a chaque ajout de class
		this.classNotifier.setClassContainer(DataApi.classes.get(ClassName));
		}
	
	//methode qui cree une list de type AttributeDao et qui le peuple avec les attribute données 
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
					if(parentClass!=null){
						parentClass.setSubClassToParent(DataApi.classes.get(sub.trim()));
					}else{
						//la classe parent n'est pas definie 
						JOptionPane.showMessageDialog(FrameFactory.getFrame(), "la classe "+generalizationClassName+" de la generalization n'est pas definie","Message D'erreur",JOptionPane.ERROR_MESSAGE);
						this.ERROR_ENCOUNTERED=true;
						break;
					}
					
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
					if(relatedClass!=null){
						//on verifie d'abord si la multiplicité de la relation est valide
						if(!this.isValidMultiplcity(relationBetween.get(1).replace(",", ""))){
							this.ERROR_ENCOUNTERED=true;
							JOptionPane.showMessageDialog(FrameFactory.getFrame(),
									"la multiplicité de la classe "+relatedClass.getName()+" n'est pas valide");
							return;
						}
						relationDescription.setClassRelation(new RelationType(relationBetween.get(1).replace(",", ""),relatedClass));
						//ajout de la relation dans le hashMap de toutes le relation avec le nom en clé
						DataApi.relations.put(relationName, relationDescription);
						//ajout de la relation dans la classe qui fait partie de la relation
						ClassDao relationBetweenClass=DataApi.classes.get(relationBetween.get(0));
						relatedClass.addRelationToRelations(relationDescription);
					}else{
						System.out.println("tes");
						JOptionPane.showMessageDialog(FrameFactory.getFrame(), "erreur la classe "+relationBetween.get(0)+" n'existe pas","Message D'erreur",JOptionPane.ERROR_MESSAGE);
						this.ERROR_ENCOUNTERED=true;
						return;
					}	
				}
				
			}
		}
		//ajout des details de la relation 
		relationDescription.setRelationDetails(String.join("\r\n", relation));
	}
	//methode qui fait la gestion des aggregation et qui popule le hashMap des aggregations
	//avec le container comme clé 
	private void handleAggregations(List<String> aggregations){
		aggregations=this.removeWhiteSpaces(aggregations);
		List<RelationType> aggregationRelations=new ArrayList<RelationType>();
		String aggType="";
		String aggregationContainerName="",aggregationContainerType="",aggregationPartsName="",aggregationPartsType="";
		for(String aggregation:aggregations){
			if(aggregation.contains("CONTAINER")){
				aggType="CONTAINER";
			}else if(aggregation.contains("PARTS")){
				aggType="PARTS";
			}else if(aggregation.contains("CLASS") && aggType=="CONTAINER"){
				String aggContainerDesc=aggregation.substring(
						aggregation.indexOf("CLASS")+6,aggregation.length());
				 aggregationContainerName=aggContainerDesc.substring(0,aggContainerDesc.indexOf(" "));
				 aggregationContainerType=aggContainerDesc.substring(aggContainerDesc.indexOf(" ")+1,aggContainerDesc.length());
			}else if(aggregation.contains("CLASS") && aggType=="PARTS"){
				String aggPartsDesc=aggregation.substring(
						aggregation.indexOf("CLASS")+6,aggregation.length());
				aggregationPartsName=aggPartsDesc.substring(0,aggPartsDesc.indexOf(" "));
				aggregationPartsType=aggPartsDesc.substring(aggPartsDesc.indexOf(" ")+1,aggPartsDesc.length());
				ClassDao aggregationPartClass=DataApi.classes.get(aggregationPartsName);
				if(aggregationPartClass!=null){
					aggregationRelations.add(new RelationType(aggregationPartsType,aggregationPartClass));
				}else{
					JOptionPane.showMessageDialog(FrameFactory.getFrame(), "erreur la classe "+aggregationPartsName+" n'existe pas","Message D'erreur",JOptionPane.ERROR_MESSAGE);
					this.ERROR_ENCOUNTERED=true;
					return;
				}
				
			}
			
		}
		ClassDao container=DataApi.classes.get(aggregationContainerName);
		AggregationDao aggregation=new AggregationDao(container, aggregationContainerType,aggregationRelations);
		DataApi.aggregations.put(container.getName(), aggregation);
		
		//ajout des details de l'aggregation
		aggregation.setAggregationDetails(String.join("\r\n", aggregations));
		
	}
	
	
	//methode qui verifie si la multiplicité est valide ou non
	//retourne un boolean 
	private boolean isValidMultiplcity(String multiplicity){
		List<String> multiplicities=Arrays.asList("ONE","MANY","ONE_OR_MANY","OPTIONALLY_ONE","UNDEFINED");
		if(multiplicities.indexOf(multiplicity)==-1){
			return false;
		}
		return true;
	}
	
	private boolean checkDuplicates(List<AttributeDao> attributes){
		HashMap<String, Boolean> attrList=new HashMap<String, Boolean>();
		
		for(AttributeDao attr:attributes){
			if(attrList.get(attr.getAttributeName()+" "+attr.getAttributeType())==null){
				attrList.put(attr.getAttributeName()+" "+attr.getAttributeType(), true);
			}else{
				return true;
			}
		}
		return false;
	}
	private boolean checkMethodeDuplicates(List<MethodeDao> methodes){
		return true;
	}
	
	// la methode qui remet a zero les données 
	private void reset(){
		DataApi.classes.clear();
		DataApi.aggregations.clear();
		DataApi.relations.clear();
	}
	private List<String> removeWhiteSpaces(List<String> classDefinition){
		
			for(int i=0;i<classDefinition.size();i++){
				classDefinition.set(i, classDefinition.get(i).trim());
			}
			//supprimer les chaines vides
			classDefinition.removeAll(Collections.singleton(""));
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
