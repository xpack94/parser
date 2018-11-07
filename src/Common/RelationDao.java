package Common;

import java.util.ArrayList;
import java.util.List;
/*
 * classe qui comporte toutes les informations relatif aux relations 
 * 
 * 
 * **/
public class RelationDao {

	private String relationName;
	private List<RelationType> classesInRelation=new ArrayList<RelationType>();
	private String relationDetails;
	
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


	public String getRelationDetails() {
		return relationDetails;
	}


	public void setRelationDetails(String relationDetails) {
		this.relationDetails = relationDetails;
	}


	
	
	
}
