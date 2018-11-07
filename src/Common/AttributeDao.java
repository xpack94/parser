package Common;

/*
 * classe qui contient un attribut d'une classe avec ses informations
 * **/
public class AttributeDao {

	private String attributeName;
	private String attributeType;
	
	public AttributeDao(String name,String type){
		this.attributeName=name;
		this.attributeType=type;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	@Override
	public String toString() {
		return "AttributeDao [attributeName=" + attributeName
				+ ", attributeType=" + attributeType + "]";
	}
	
	
}
