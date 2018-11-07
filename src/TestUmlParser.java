import java.io.File;
import javax.xml.crypto.Data;

import org.junit.Test;

import Common.DataApi;
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
		
	
		
		
		
	}


}



