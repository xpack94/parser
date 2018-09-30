package Common;

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
	
	
}
