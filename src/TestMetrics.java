import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import Common.Metrics;
import Common.UmlParser;
import Notifiers.ClassNotifier;


public class TestMetrics {

	@SuppressWarnings("deprecation")
	@Test
	public void test() {
		final double delta = 0.00001;
		File anaFile = new File("testAna.ucd");
		UmlParser parser=new UmlParser(anaFile);
		ClassNotifier notifier=new ClassNotifier();
		parser.setClassNotifier(notifier);
		parser.parseFile(parser.getFileToParse());
		//maintenant que le fichier est lu on commence les tests 
		Metrics m = new Metrics();
		float ana =m.calculateAna("ift");
		float ana2=m.calculateAna("ift2");
		float ana3=m.calculateAna("ift3");
		assertEquals(2, ana,delta);
		assertEquals(0, ana2,delta); // test du cas ou il y'a aucun parametre dans les methode 
		assertEquals(0, ana3,delta) ;//test du cas ou il y'a aucune methode  
		
		
		
		//les tests pour Nom
		File nomFile = new File("testNom.ucd");
		parser.setFileToParse(nomFile);
		parser.parseFile(parser.getFileToParse());
		
		/*float nom=m.calculateNom("nom"); 
		float nom2=m.calculateNom("nom2"); 
		float nom4=m.calculateNom("nom4"); */
		//float nom5=m.calculateNom("nom5"); 
		//assertEquals(3, nom,delta); // teste pour le cas ou il y'a que des methode locale
		//assertEquals(1, nom2,delta);// teste pour le cas ou i y'a que des methode hérité
		//assertEquals(2, nom4,delta) ; //teste pour le cas ou il y'a des methodes hérité et local
	//	assertEquals(2, nom5,delta) ; // teste du cas ou il y'a des methode local et hérité de plusieurs classe 
		
		
		// les tests pour 
		File noaFile = new File("testNoa.ucd");
		parser.setFileToParse(noaFile);
		parser.parseFile(parser.getFileToParse());
		float noa=m.calculateNoa("noa"); 
		float noa2=m.calculateNoa("noa5"); 
		float noa3=m.calculateNoa("noa2"); 
		float noa4=m.calculateNoa("noa4"); 
		float noa6=m.calculateNoa("noa6"); 
		assertEquals(0, noa,delta); // le cas d'une class ayant 0 attributs local et 0 hérité
		assertEquals(1, noa2,delta) ; // le cas d'une classe ayant que des attrbiuts locaux 
		assertEquals(1, noa3,delta); // le cas d'une classe ayant que des attributs hérité d'une seule classe
		assertEquals(2, noa4,delta) ; // le cas d'une classe ayant des attributs locaux et hérité d'une seule classe 
		assertEquals(3, noa6,delta ); // le cas d'une classe ayant des attributs locaux et hérité de plusieurs classe 
	
		//les tests pour Itc
		File itcFile = new File("testItc.ucd");
		parser.setFileToParse(itcFile);
		parser.parseFile(parser.getFileToParse());
		float itc=m.calculateItc("itc"); 
		float itc2=m.calculateItc("itc2"); 
		float itc3=m.calculateItc("itc3"); 
		assertEquals(1, itc,delta) ;//le cas ou un seul parametre a le type d'une autre classe du diagramme
		assertEquals(2, itc2,delta) ; // le case ou deux parametres ont le type de d'autres classes du digramme;
		assertEquals(0, itc3,delta) ; //le case ou aucun parametre n'est du type de d'autre classe du diagramme
	
		//les tests pour Etc
		File etcFile = new File("testEtc.ucd");
		parser.setFileToParse(etcFile);
		parser.parseFile(parser.getFileToParse());
		float etc=m.calculateEtc("etc1"); 
		float etc2=m.calculateEtc("etc2"); 
		float etc3=m.calculateEtc("etc3"); 
		assertEquals(1, etc,delta); // le case ou le type etc apparait une seule fois dans les methodes des autres classe du diagramme
		assertEquals(0, etc2,delta); // le cas ou le type etc2 n'apparait pas dans d'autre classe 
		assertEquals(2, etc3,delta); // le case ou etc3 apparait 2 fois dans les methodes  de classes differentes
		
		//les tests pour cac
		File cacFile = new File("testCac.ucd");
		parser.setFileToParse(cacFile);
		parser.parseFile(parser.getFileToParse());
		float cac=m.calculateCac("cac"); 
		float cac4=m.calculateCac("cac4"); 
		float cac3=m.calculateCac("cac3"); 
		assertEquals(1, cac,delta); // le cas ou une classe a que des associaions directe 
		assertEquals(2, cac4,delta);  // le cas ou une classe a que des association héritées
		assertEquals(3, cac3,delta); // le cas ou une classe a des associations héritées et locale
		
		
		//les tests pour dit
		File ditFile = new File("testDit.ucd");
		parser.setFileToParse(ditFile);
		parser.parseFile(parser.getFileToParse());
		float dit=m.calculateDit("dit"); 
		float dit2=m.calculateDit("dit2"); 
		float dit5=m.calculateDit("dit5"); 
		assertEquals(0, dit,delta);  // le cas ou la classe n'a aucune classe parent 
		assertEquals(1, dit2,delta); // le cas ou la classe a un parent
		assertEquals(2, dit5,delta); // le case ou la classe a 2 a plusieurs parents
		
		// les tests pour noc
		File nocFile = new File("testNoc.ucd");
		parser.setFileToParse(nocFile);
		parser.parseFile(parser.getFileToParse());
		float noc2=m.calculateNoc("noc2"); 
		float noc3=m.calculateNoc("noc3"); 
		assertEquals(0, noc2,delta); // le cas ou il y'a aucune sous classe 
		assertEquals(2, noc3,delta); // le cas de une ou plusieurs sous classe directe
		
		// les tests pour nod 
		File nodFile = new File("testNod.ucd");
		parser.setFileToParse(nodFile);
		parser.parseFile(parser.getFileToParse());
		float nod2=m.calculateNod("nod2"); 
		float nod3=m.calculateNod("nod3"); 
		float nod4=m.calculateNod("nod4"); 
		assertEquals(0, nod2,delta); // le cas ou il y'a aucune sous classe 
		assertEquals(1, nod4,delta); // le cas de une ou plusieurs sous classe directe
		assertEquals(5, nod3,delta); // le case ou il y'a une ou plisueurs sous classes directe et indirectes
		
		//les tests pour cld 
		File cldFile = new File("testCld.ucd");
		parser.setFileToParse(cldFile);
		parser.parseFile(parser.getFileToParse());
		float cld=m.calculateCld("cld"); 
		float cld2=m.calculateCld("cld2"); 
		float cld3=m.calculateCld("cld3"); 
		float cld4=m.calculateCld("cld4"); 
		assertEquals(0, cld2,delta); // la cas ou il y'a aucune sous classe 
		assertEquals(1, cld4,delta); // le cas ou il y'a une seule couche  sous classes 
		assertEquals(3, cld3,delta);  // le cas ou une des sous classe a une ou plusieur sous classe 
		
		
	
	}

}
