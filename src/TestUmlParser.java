import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.text.html.HTMLEditorKit.Parser;
import javax.xml.crypto.Data;

import org.junit.Test;

import Common.AttributeDao;
import Common.DataApi;
import Common.MethodeDao;
import Common.UmlParser;
import Notifiers.ClassNotifier;
import static org.junit.Assert.*;


public class TestUmlParser {
	@Test
	public void test(){
		File parserTest = new File("testParser.ucd");
		UmlParser parser=new UmlParser(parserTest);
		ClassNotifier notifier=new ClassNotifier();
		parser.setClassNotifier(notifier);
		parser.parseFile(parser.getFileToParse());
		assertEquals(DataApi.classes.size(),5); // on verifie qu'on a le bon nombre de classes parsée
		assertEquals(DataApi.aggregations.size(),1); // on verifie qu'on a le bon nombre d'aggregations
		assertEquals(DataApi.relations.size(),2); // on verifie qu'on a le meme nombre de relations
		assertEquals(DataApi.classes.get("model").getName(), "model"); // on verifie qu'il trouve la classe model 
		assertEquals(DataApi.classes.get("company").getName(), "company"); // trouve la classe company 
		assertEquals(DataApi.classes.get("Ent").getName(), "Ent");  // trouve la classe Ent 
		assertEquals(DataApi.classes.get("test").getName(), "test"); // trouve la classe test 
		assertEquals(DataApi.classes.get("Test").getName(), "Test"); // trouve ma classe Test
		assertEquals(DataApi.relations.get("est_dans").getRelationName(),"est_dans" ); // on verifie qu'il trouve la relation est_dans
		assertEquals(DataApi.relations.get("test").getRelationName(), "test"); //on verifie qu'il trouve la relation test
		
		
		 // on verifie que la premiere class dans la relation est bel est bien la classe company
		assertEquals(DataApi.relations.get("est_dans").getClassesInRelation().get(0).getRelatedTo().getName(), "company" );
		 // on verifie que la deuxiemme class dans la relation est bel est bien la classe test
		assertEquals(DataApi.relations.get("est_dans").getClassesInRelation().get(1).getRelatedTo().getName(), "test" );
		//on verifie que l'aggregation Test est bien parsé 
		assertEquals(DataApi.aggregations.get("Test").getAggregationContainer().getName(), "Test");
		//on verifie que l'aggregation Test a la class model dans les Parts
		assertEquals(DataApi.aggregations.get("Test").getAggregationParts().get(0).getRelatedTo().getName(), "model");
		//on verifie que la classe model est une classe parent de la class test 
		assertEquals(DataApi.classes.get("model").getSubClasses().get(0).getName(),"test");
		//on verifie que la classe company a comme attribut nom_company 
		assertEquals(DataApi.classes.get("company").getAttributes().get(0).getAttributeName(), "nom_company");
		//on verifie que la classe company a le premier attribut de type string
		assertEquals(DataApi.classes.get("company").getAttributes().get(0).getAttributeType(), "String");
		//on verifie que la classe company a la methode nombre_model 
		assertEquals(DataApi.classes.get("company").getMethodes().get(0).getMethodeName(), "nombre_model");
		//on verifie que le type de retour de la premiere methode est Integer 
		assertEquals(DataApi.classes.get("company").getMethodes().get(0).getReturnType(), "Integer");
		
		
		
	}
	@Test
	public void testMultiplicity(){
		File file = new File("testParser.ucd");
		UmlParser parser=new UmlParser(file);
		ClassNotifier notifier=new ClassNotifier();
		
		assertEquals(true, parser.isValidMultiplcity("MANY")) ; // on verifie si la multiplicité  MANY est valide 
		assertEquals(true, parser.isValidMultiplcity("ONE")) ; // on verifie si la multiplicité  ONE est valide 
		assertEquals(true, parser.isValidMultiplcity("ONE_OR_MANY")) ; // on verifie si la multiplicité  ONE_OR_MANY est valide 
		assertEquals(true, parser.isValidMultiplcity("OPTIONALLY_ONE")) ; // on verifie si la multiplicité  OPTIONALLY_ONE est valide 
		assertEquals(true, parser.isValidMultiplcity("UNDEFINED")) ; // on verifie si la multiplicité  UNDEFINED est valide 
		
		assertEquals(false, parser.isValidMultiplcity("OPTIONALLY_TWO")) ; // on verifie si la multiplicité  OPTIONALLY_TWO est invalide 
		assertEquals(false, parser.isValidMultiplcity("")) ; // on verifie une multiplicité vide  
	}
	@Test
	public void testCheckDuplicates(){
		File file = new File("testParser.ucd");
		UmlParser parser=new UmlParser(file);
		ClassNotifier notifier=new ClassNotifier();
		AttributeDao attr1 = new AttributeDao("test", "Integer");
		AttributeDao attr2 = new AttributeDao("test", "String");
		AttributeDao attr3 = new AttributeDao("element", "Float");
		AttributeDao attr4 = new AttributeDao("elementId", "Float");
		List<AttributeDao> attrs = new ArrayList<AttributeDao>();
		List<AttributeDao> attrs2 = new ArrayList<AttributeDao>();
		List<AttributeDao> attrs3 = new ArrayList<AttributeDao>();
		attrs.add(attr1);
		attrs.add(attr2);
		attrs.add(attr3);
		
		boolean b= parser.checkDuplicates(attrs);
		assertEquals(true, b); // on verifie que pour cette liste il detecte une duplication d'attributs 
		attrs2.add(attr1);
		attrs2.add(attr4);
		boolean b2=parser.checkDuplicates(attrs2);
		
		assertEquals(false, b2) ; // on verifie qu'il ne detecte pas de duplication 
		
		assertEquals(false, parser.checkDuplicates(attrs3));  // le teste avec une liste vide 
		
		
		
	}
	@Test
	public void testCheckMethodesDuplicates(){
		
		File file = new File("testParser.ucd");
		UmlParser parser=new UmlParser(file);
		ClassNotifier notifier=new ClassNotifier();
		
		AttributeDao attr1 = new AttributeDao("test", "Integer");
		AttributeDao attr2 = new AttributeDao("test", "String");
		ArrayList<AttributeDao> attrs = new ArrayList<AttributeDao>();
		attrs.add(attr1);
		attrs.add(attr2);
		AttributeDao attr3 = new AttributeDao("ball", "String");
		AttributeDao attr4 = new AttributeDao("count", "Integer");
		
		AttributeDao attr5 = new AttributeDao("ball", "String");
		AttributeDao attr6 = new AttributeDao("count", "Integer");
		ArrayList<AttributeDao> attrs2 = new ArrayList<AttributeDao>();
		attrs2.add(attr3);
		attrs2.add(attr4);
		
		ArrayList<AttributeDao> attrs3 = new ArrayList<AttributeDao>();
		attrs3.add(attr5);
		attrs3.add(attr6);
		
		
		MethodeDao meth1=new MethodeDao("play", attrs,"void" );
		MethodeDao meth2=new MethodeDao("soccer", attrs2,"void" );
		MethodeDao meth3=new MethodeDao("soccer", attrs3,"String" );
		
		List<MethodeDao> methodes=new ArrayList<MethodeDao>();
		List<MethodeDao> methodes2=new ArrayList<MethodeDao>();
		List<MethodeDao> methodes3=new ArrayList<MethodeDao>();
		methodes.add(meth1);
		methodes.add(meth2);
		
		methodes2.add(meth2);
		methodes2.add(meth3);
		
		
		assertEquals(false, parser.checkMethodeDuplicates(methodes)) ; // on verifie qu'il y'a aucune duplication de methodes 
		assertEquals(true, parser.checkMethodeDuplicates(methodes2)); // le cas ou il y'a une duplication de methode 
		assertEquals(false, parser.checkMethodeDuplicates(methodes3)); // avec une liste vide
		
		
	}


}



