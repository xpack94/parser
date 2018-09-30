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


public class UmlParser {
	
	private File fileToParse;
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
				break;
			}else if(subFileContent.get(i).contains("RELATION")){
				break;
			}else if(subFileContent.get(i).contains("AGGREGATION")){
				break;
			}		
		}
	}
	
	private void handleClass(List<String> classDefinition){
		//on se debarasse de tout les espaces
		classDefinition=this.removeWhiteSpaces(classDefinition);
		List<String> attributes=new ArrayList<String>();
		List<String> methodes=new ArrayList<String>();
		//maintenant on dispose d'un array list contenant la description d'une class
		for(int i=0;i<classDefinition.size();i++){
			if(classDefinition.get(i).contains("ATTRIBUTES")){
				int methodesIndex=classDefinition.indexOf("OPERATIONS");
				attributes=classDefinition.subList(i+1, methodesIndex);
			}else if(classDefinition.get(i).contains("OPERATIONS")){
				 methodes=classDefinition.subList(i+1, classDefinition.size());
			}
			
		}
		
		//maintenant puisque on a extrait les attributs et les methode de la class
		//on peut les populer dans leurs classes respectives
		this.populateAttributes(attributes);
		
			
		}
	
	//methode qui cree une list de type AttributeDao et qui le popule avec les attribute données 
	private List<AttributeDao> populateAttributes(List<String> attributes){
		List<AttributeDao> attributesList=new ArrayList<AttributeDao>();
		if(attributes.size()>0){
			for(String attribute:attributes){
				//extraction du nom de l'attribut 
				int clIndex=attribute.indexOf(":");
				String attributeName=attribute.substring(0,clIndex);
				String attributeType=attribute.substring(clIndex+1,attribute.length());
				attributesList.add(new AttributeDao(attributeName, attributeType));
			}
			
		}
		
		return attributesList;
		
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
	
}
