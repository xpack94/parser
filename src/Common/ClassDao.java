package Common;
import java.util.ArrayList;


public class ClassDao {

	private String name;
	private ArrayList<AttributeDao> attributes;
	private ArrayList<MethodeDao> methodes;
	
	public ClassDao(String name,ArrayList<AttributeDao>attributes,ArrayList<MethodeDao> methodes){
		this.attributes=attributes;
		this.methodes=methodes;
		this.name=name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<AttributeDao> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<AttributeDao> attributes) {
		this.attributes = attributes;
	}

	public ArrayList<MethodeDao> getMethodes() {
		return methodes;
	}

	public void setMethodes(ArrayList<MethodeDao> methodes) {
		this.methodes = methodes;
	}
	
	
	
}
