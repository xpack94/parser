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

/**la classe UmlParser s'occupe de faire le parsing du fichier ucd lit , cette derniere 
 * contient les methode qui parse les classes , les relations , les generalization et les aggregations
 * elle permet aussi de detecter les erreurs l'ors du parsing du fichier
 * 
 * @version 2.0
 * 
 * 
 * **/
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
	
	private String CLASSNAME="CLASS.*";
	public UmlParser(File fileToParse){
		this.fileToParse=fileToParse;
	}
	/**@param file de type File qui represente le fichier a parser 
	 * @return void car elle se contante d'appler les methodes qui font le parsing 
	 * et qui remplissent les hashMaps de la classes DataApi
	 * 
	 * 
	 * 
	 * **/
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
			
			if(DataApi.classes.size()==0){
				//on a a faire a un fichier vide
				JOptionPane.showMessageDialog(FrameFactory.getFrame(), "le fichier fourni est vide","Message D'erreur",JOptionPane.ERROR_MESSAGE);
				this.ERROR_ENCOUNTERED=true;
				return;
			}
		}
		
	}
	/**
	 * @param scanner du type Scanner
	 * @return void
	 * permet de chercher les classes du fichier avec une expression reguliere relatif a la classe
	 * si une ou plusieurs classes sont trouvées alors elle appele la methode @see handleOneClass qui 
	 * fait le parsing d'une seule classe et ceux pour toute les classes trouvées
	 * 
	 **/
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
	
	
	/**
	 * @param attributes de type {@link List<String>} corrspodant au attributs de la classe lu du fichier 
	 * @return {@link List<AttributeDao>} correspondant au attributs de la classe avec tout leurs informations
	 * 
	 *  cette methode permet de creer les AttributeDao pour chaque attribut lu 
	 *  une attributeDao est une classe contenant toutes les informations de ce dernier
	 * */
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
	/**
	 * @param oneClass correspondant a une des classes du fichier lu 
	 * @return void
	 * s'occupe de faire le parsing d'une seule classe et remplit les hashMaps du DataApi
	 * 
	 * */
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
					
					classMethodes=this.populateMethodes(this.removeWhiteSpaces(new ArrayList<String>(Arrays.asList(MatcherMethodes.group().split("\n")))));
					
				}
				List<String > classDefinition=new ArrayList<String>(Arrays.asList(oneClass.split("\n")));	
				//on se debarasse de tout les espaces
				classDefinition=this.removeWhiteSpaces(classDefinition);
				String ClassName="";
				//maintenant on dispose d'un array list contenant la description d'une class
				Pattern patternClass=Pattern.compile(this.CLASSNAME);
				Matcher matcherClass=patternClass.matcher(oneClass);
				if(matcherClass.find()){
					
					ClassName=matcherClass.group().substring(matcherClass.group().indexOf("CLASS")+6).trim();
					
				}
				// faire la validation des methode et attribut et class qui viennent d'etre parser
				if(!this.classValidation(ClassName,classAttributes,classMethodes)){
					return;
				}
				
				//l'ajout de la class avec sa definition dans le hashMap qui contient toutes les classes
				DataApi.classes.put(ClassName, new ClassDao(ClassName, classAttributes, classMethodes));
				DataApi.classes.get(ClassName).setClassDescription(oneClass.trim());
				//ajout de la class dans le notificateur
				//ce dernier va notifier le ClassList pour qu'il se met a jours a chaque ajout de class
				this.classNotifier.setClassContainer(DataApi.classes.get(ClassName));
			
	}
	/**
	 * @param methodes qui represente les methodes lu du fichier
	 * @return {@link List<MethodeDao>} apres population dans les classe MethodeDao
	 * 
	 * permet de populer les methodes lu dans des classes de type MethodeDao contenant
	 * toutes les informations des methodes
	 * */
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
	/**
	 * @param scanner qui correspond au fichier lu 
	 * @return void 
	 * s'occupe de faire le parsing des generalization a l'aide de l'expression reguliere 
	 * relatif a cette derniere 
	 * */
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
	
						ClassDao parentClass=DataApi.classes.get(generalizationClassName.trim());

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
	/**
	 * @param scanner correspondant au fichier lu 
	 * @return void 
	 * 
	 * s'occupe de faire le parsing des relations trouvée a l'aide d'une experssion reguliere relatif 
	 * aux relations 
	 * cette methode popule le hashMap des relations de la classe DataApi 
	 * */
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
					relation=new ArrayList<String>(Arrays.asList(results.group().split("\n")));
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
	
	/**
	 * @param scanner qui represente le fichier lu 
	 * @return void 
	 * d'occupe de faire le parsing des aggregations trouvée a l'aide d'une expression reguliere relatif a cette derniere
	 * cette methode remplit le hashMap des aggregations de la classe DataApi
	 * 
	 * */
	//methode qui fait la gestion des aggregation et qui popule le hashMap des aggregations
	//avec le container comme clé 
	private void handleAggregations(Scanner scanner){
		
		try{
			scanner.findWithinHorizon(this.AGGREGATIONS, 0);
			MatchResult results=scanner.match();
			while(results.groupCount()>0){
				
				List<String> aggregations = new ArrayList<String>(Arrays.asList(results.group().split("\n")));
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
						
						aggregationRelations.add(new RelationType(aggregationPartsType,aggregationPartClass));
						
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
	
	/**
	 * @param multiplicity qui represente la multiplicité d'une relation 
	 * @return boolean qui correspond a true si la multiplicité donnée en entrée est valide et false sinon
	 * 
	 * */
	//methode qui verifie si la multiplicité est valide ou non
	//retourne un boolean 
	private boolean isValidMultiplcity(String multiplicity){
		List<String> multiplicities=Arrays.asList("ONE","MANY","ONE_OR_MANY","OPTIONALLY_ONE","UNDEFINED");
		if(multiplicities.indexOf(multiplicity)==-1){
			return false;
		}
		return true;
	}
	
	/**
	 * @param List<AttributeDao> correspondant a tout les attribut d'une classe 
	 * @return boolean qui represente true si il existe des duplication d'un meme attribut et false sinon
	 * 
	 * */
	
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
	
	/**
	 * @param List<MethodeDao> qui represente les methode d'une classe 
	 * @return boolean qui represente true si il y'a une duplication d'une meme methode avec les meme signatures et false sinon
	 * */
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
	
	/**
	 * 
	 * @return void 
	 * permet de remetre a zero les hashMaps du DataApi
	 * */
	// la methode qui remet a zero les données 
	private void reset(){
		DataApi.classes.clear();
		DataApi.aggregations.clear();
		DataApi.relations.clear();
	}
	/**@param className qui represente le nom d'une classe du fichier
	 * @param List<AttributeDao> qui represente les attributs d'une classe donnée
	 * @param List<MethodeDao> qui represente les methodes d'une classe donnée 
	 * @return qui represente false si la classe n'est pas valide te true sinon 
	 * 
	 * cette methode fait la validation des données d'une classe avant de rajouter cette derniere dans le HashMap
	 * 
	 * 
	 * */
	private  boolean classValidation(String className,List<AttributeDao> attributes,List<MethodeDao> methdoes){
		
		//on verifie si il existe d'attribut dupliqué dans la meme classe
		if(attributes!=null  && this.checkDuplicates(attributes)){
			JOptionPane.showMessageDialog(FrameFactory.getFrame(), "duplication d'attribut dans la classe "+className
					,"Message D'erreur",JOptionPane.ERROR_MESSAGE);
			this.ERROR_ENCOUNTERED=true;
			return false;	
		}
		//on verifie si il existe des duplications de methodes dans la meme classe
		if(this.checkMethodeDuplicates(methdoes)){
			JOptionPane.showMessageDialog(FrameFactory.getFrame(), "duplication de methodes avec les memes parametres dans la classe "+className,
					"Messager D'erreur",JOptionPane.ERROR_MESSAGE);
			this.ERROR_ENCOUNTERED=true;
			return false;
		}
		//on verifie si la classe existe deja et donc qu'il ya de duplication de classe 
		if(DataApi.classes.get(className)!=null){
			JOptionPane.showMessageDialog(FrameFactory.getFrame(), "duplication de la classe "+className,"Message D'erreur",JOptionPane.ERROR_MESSAGE);
			this.ERROR_ENCOUNTERED=true;
			return false;
		}
		// on verifie si c'est une classe vide
		if(attributes.size()==0 && methdoes.size()==0){
			JOptionPane.showMessageDialog(FrameFactory.getFrame(), "la classe "+className+" est une classe vide","Message D'erreur",JOptionPane.ERROR_MESSAGE);
			this.ERROR_ENCOUNTERED=true;
			return false;
		}
		
		return true;
	}
	
	/**@param classdefinition qui represente une difinition d'une classe sous la forme d'une liste 
	 * 
	 * @return List<String> correspondant a la definition de la classe mais sans espace blanc 
	 * 
	 * 
	 * */
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
