package Common;
import java.util.ArrayList;
import java.util.List;


public class ClassDao {

	private String name;
	private List<AttributeDao> attributes;
	private List<MethodeDao> methodes;
	private List<ClassDao> subClasses;
	private List<RelationDao> relations;
	
	public ClassDao(String name,List<AttributeDao> classAttributes,List<MethodeDao> classMethodes){
		this.attributes=classAttributes;
		this.methodes=classMethodes;
		this.subClasses=new ArrayList<ClassDao>();
		this.relations=new ArrayList<RelationDao>();
		this.name=name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AttributeDao> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeDao> attributes) {
		this.attributes = attributes;
	}

	public List<MethodeDao> getMethodes() {
		return methodes;
	}

	public void setMethodes(List<MethodeDao> methodes) {
		this.methodes = methodes;
	}

	public List<ClassDao> getSubClasses() {
		return subClasses;
	}

	public void setSubClasses(List<ClassDao> subClasses) {
		this.subClasses = subClasses;
	}
	
	public void setSubClassToParent(ClassDao subClass){
		this.subClasses.add(subClass);
	}

	public List<RelationDao> getRelations() {
		return relations;
	}

	public void setRelations(List<RelationDao> relations) {
		this.relations = relations;
	}
	
	public void addRelationToRelations(RelationDao relation){
		this.relations.add(relation);
	}
	
}
