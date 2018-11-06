package Common;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import controller.FrameFactory;

import Notifiers.ClassNotifier;


public class UmlParser {
	
	private File fileToParse;
	private ClassNotifier classNotifier;
	private boolean ERROR_ENCOUNTERED=false;
	
	private String GENERALIZATION="GENERALIZATION+\\s+(\\w*)+\\s+(SUBCLASSES[^;]*)";
	private String CLASSES="\\s+(CLASS){1}\\s+(\\w*)+\\s+(ATTRIBUTES[^;]*)";
	private String ATTRIBUTES="(?<=ATTRIBUTES)([^;]*)(?=OPERATIONS)";
	private String METHODES="(?<=OPERATIONS)([^;]*)";
	private String RELATIONS="RELATION+\\s+(\\w*)+\\s+(ROLES[^;]*)";
	private String SUBCLASSES="SUBCLASSES.*";
	private String GENERALIZATIONHEADER="GENERALIZATION.*";
	private String AGGREGATIONS="AGGREGATION+\\s+([^;])*";
	
	public UmlParser(File fileToParse){
		this.fileToParse=fileToParse;
	}
	
	public void parseFile(File file){
		
		Scanner s = null;
		try {
			 s=new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//initialisation du Notifier du  ClassList
		this.classNotifier.setShouldRemoveClass(true); 
		this.reset(); // mettre a zero tout les HashMaps contenant les données
		if(this.ERROR_ENCOUNTERED){
			//si une erreur a été détecté lors du parsing on arrete 
			this.ERROR_ENCOUNTERED=false; //on le remet a jours
			
			//true veut dire qu'on doit supprimer toutes les classes du ClassList
			//a cause d'une erreur lors du parsing
			this.classNotifier.setShouldRemoveClass(true);
			this.reset(); //remet a jours le DataApi
			return;
		}
		if(s!=null){
			this.handleClasses(s);
			this.handleGeneralizations(s);
			this.handleRelation(s);
			this.handleAggregations(s);
		}
		
	}
	
	private void handleClasses(Scanner scanner){
		try{
			scanner.findWithinHorizon(this.CLASSES, 0);
			MatchResult results=scanner.match();
			while(results.groupCount()>0){	
				this.handleOneClass(results.group(0));
				scanner.findWithinHorizon(this.CLASSES, 0);
				results=scanner.match();
			}
		}
		catch(Exception e){
		}
	
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
	
	private void handleOneClass(String oneClass){
				// on matche les attributs de la classe
				Pattern patternAttrs=Pattern.compile(this.ATTRIBUTES);
				Matcher matcherAttrs=patternAttrs.matcher(oneClass);
				List<AttributeDao> classAttributes=null;
				if(matcherAttrs.find()){
					// des attribut existe pour cette classe 
					// alors on doit les créer
					classAttributes=this.populateAttributes(this.removeWhiteSpaces(new ArrayList<String>(Arrays.asList(matcherAttrs.group().split(",")))));
				}
				
				// on matche les methode de la classe
				Pattern patternMethodes=Pattern.compile(this.METHODES);
				Matcher MatcherMethodes=patternMethodes.matcher(oneClass);
				List<MethodeDao> classMethodes=null;
				if(MatcherMethodes.find()){
					// des methodes existe pour cette classe 
					// alors on doit les créer
					
					classMethodes=this.populateMethodes(this.removeWhiteSpaces(new ArrayList<String>(Arrays.asList(MatcherMethodes.group().split("\\r\\n")))));
				}
				List<String > classDefinition=new ArrayList<String>(Arrays.asList(oneClass.split("\\r\\n")));	
				//on se debarasse de tout les espaces
				classDefinition=this.removeWhiteSpaces(classDefinition);
				String ClassName="";
				//maintenant on dispose d'un array list contenant la description d'une class
				ClassName=classDefinition.get(0).substring(6,classDefinition.get(0).length());
				
				//on verifie si il existe d'attribut dupliqué dans la meme classe
				if(classAttributes!=null  && this.checkDuplicates(classAttributes)){
					JOptionPane.showMessageDialog(FrameFactory.getFrame(), "duplication d'attribut dans la classe "+ClassName
							,"Message D'erreur",JOptionPane.ERROR_MESSAGE);
					this.ERROR_ENCOUNTERED=true;
					return;	
				}
				//on verifie si il existe des duplications de methodes dans la meme classe
				if(this.checkMethodeDuplicates(classMethodes)){
					JOptionPane.showMessageDialog(FrameFactory.getFrame(), "duplication de methodes avec les memes parametres dans la classe "+ClassName,
							"Messager D'erreur",JOptionPane.ERROR_MESSAGE);
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
				DataApi.classes.get(ClassName).setClassDescription(oneClass.trim());
				//ajout de la class dans le notificateur
				//ce dernier va notifier le ClassList pour qu'il se met a jours a chaque ajout de class
				this.classNotifier.setClassContainer(DataApi.classes.get(ClassName));
			
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
	private void handleGeneralizations(Scanner scanner){
		MatchResult results=null;
		try{
			scanner.findWithinHorizon(this.GENERALIZATION, 0);
			results=scanner.match();
		
			String generalizationClassName="";
			while(results!=null &&  results.groupCount()>0){
				Pattern pattern=Pattern.compile(this.GENERALIZATIONHEADER);
				Matcher matcher=pattern.matcher(results.group());
				if(matcher.find()){
					//get the parent class
					generalizationClassName=matcher.group().substring(15,matcher.group().length());
					
				}else{
					System.out.println("erreur en parsant les generalizations");
				}
				Pattern patternDefinition=Pattern.compile(this.SUBCLASSES);
				Matcher matcherDefnition=patternDefinition.matcher(results.group());
				if(matcherDefnition.find()){
					//boucler sur chaque sous classe et l'ajouter a la liste des sous classe du parent
					List<String>subClassesDef=new ArrayList<String>(Arrays.asList((matcherDefnition.group().substring(11, matcherDefnition.group().length()).split(","))));
					for (String sub:this.removeWhiteSpaces(subClassesDef)) {
	
						ClassDao parentClass=DataApi.classes.get(generalizationClassName);
						if(parentClass!=null){
							parentClass.setSubClassToParent(DataApi.classes.get(sub.trim()));
							ClassDao subClass=DataApi.classes.get(sub.trim());
							if(subClass!=null){
								//on ajoute a la sous class son parent
								subClass.setParentClass(parentClass);
							}
							
						}else{
							//la classe parent n'est pas definie 
							JOptionPane.showMessageDialog(FrameFactory.getFrame(), "la classe "+generalizationClassName+" de la generalization n'est pas definie","Message D'erreur",JOptionPane.ERROR_MESSAGE);
							this.ERROR_ENCOUNTERED=true;
							break;
						}		
					}
				}
				
				scanner.findWithinHorizon(this.GENERALIZATION, 0);
				results=scanner.match();

			}
		}catch(Exception e){
			
		}
		

		
	}
	
	//methode qui prend une description d'une relation et qui popule le hashMap des relations 
	// ou le nom de la relation est la clé
	private void handleRelation(Scanner scanner){
		MatchResult results=null;
		RelationDao relationDescription = null;
		List<String> relation=new ArrayList<String>();
		try{
			String w=scanner.findWithinHorizon(this.RELATIONS, 0);
			results=scanner.match();
			
		
		while(results.groupCount()>0){
					relation=new ArrayList<String>(Arrays.asList(results.group().split("\\r\\n")));
					relation=this.removeWhiteSpaces(relation);
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
									JOptionPane.showMessageDialog(FrameFactory.getFrame(), "erreur la classe "+relationBetween.get(0)+" n'existe pas","Message D'erreur",JOptionPane.ERROR_MESSAGE);
									this.ERROR_ENCOUNTERED=true;
									return;
								}	
							}
							
						}
					}
					//ajout des details de la relation 
					relationDescription.setRelationDetails(String.join("\r\n", relation));
					scanner.findWithinHorizon(this.RELATIONS, 0);
					results=scanner.match();
					
		}
		}catch(Exception e){
			
		}
		
		
	}
	//methode qui fait la gestion des aggregation et qui popule le hashMap des aggregations
	//avec le container comme clé 
	private void handleAggregations(Scanner scanner){
		
		try{
			scanner.findWithinHorizon(this.AGGREGATIONS, 0);
			MatchResult results=scanner.match();
			while(results.groupCount()>0){
				
				List<String> aggregations = new ArrayList<String>(Arrays.asList(results.group().split("\\r\\n")));
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
						if(aggregationPartClass.getAggregations()!=null){
							aggregationPartClass.getAggregations().add(aggregationContainerName);
						}else{
							aggregationPartClass.setAggregations(new ArrayList<String>());
							aggregationPartClass.getAggregations().add(aggregationContainerName);
						}
						
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
				DataApi.aggregations.put(container.getName().trim(), aggregation);
				
				//ajout des details de l'aggregation
				aggregation.setAggregationDetails(String.join("\r\n", aggregations));
				
				
				scanner.findWithinHorizon(this.AGGREGATIONS, 0);
				results=scanner.match();
				
			}
			
			
			
		}catch(Exception e){
			
		}
		
		
		
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
	
	//methode qui verifie si il existe des duplications d'attribut dans une meme classe 
	private boolean checkDuplicates(List<AttributeDao> attributes){
		HashMap<String, Boolean> attrList=new HashMap<String, Boolean>();
		
		for(AttributeDao attr:attributes){
			if(attrList.get(attr.getAttributeName())==null){
				attrList.put(attr.getAttributeName(), true);
			}else{
				return true;
			}
		}
		return false;
	}
	
	//methode qui verifie l'existance d'une diplication de methodes dans une meme classe
	private boolean checkMethodeDuplicates(List<MethodeDao> methodes){
		HashMap<String, Boolean> methodesList=new HashMap<String, Boolean>();
		for (MethodeDao methode:methodes){
			String m=methode.getMethodeName();
			for(AttributeDao params:methode.getParameters()){
				m+=params.getAttributeName()+" "+params.getAttributeType();
			}
			if(methodesList.get(m)!=null){
				return true;
			}
			methodesList.put(m, true);
		}
		return false;
		
		
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
