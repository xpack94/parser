package Common;

/**
 * 
 * classe qui contient une classe qui est en relation avec une autre ainsi que sa multiplicité 
 * 
 * */
public class RelationType {

	private String type;
	private ClassDao relatedTo;
	
	public RelationType(String type,ClassDao relatedTo){
		this.type=type;
		this.relatedTo=relatedTo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ClassDao getRelatedTo() {
		return relatedTo;
	}

	public void setRelatedTo(ClassDao relatedTo) {
		this.relatedTo = relatedTo;
	}
	
	
	
}
