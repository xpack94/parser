package Common;

import java.util.ArrayList;
import java.util.List;

public class RelationDao {

	private String relationName;
	List<RelationType> classesInRelation=new ArrayList<RelationType>();
	
	
	public RelationDao(String relationName){
		this.relationName=relationName;
	}


	public String getRelationName() {
		return relationName;
	}


	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}


	public List<RelationType> getClassesInRelation() {
		return classesInRelation;
	}


	public void setClassesInRelation(List<RelationType> classesInRelation) {
		this.classesInRelation = classesInRelation;
	}
	public void setClassRelation(RelationType className){
		this.classesInRelation.add(className);
	}


	
	
	
}
