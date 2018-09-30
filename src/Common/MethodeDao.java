package Common;

import java.util.ArrayList;

public class MethodeDao {

	
	private String methodeName;
	private ArrayList<AttributeDao> parameters;
	private String returnType;
	
	public MethodeDao(String name,ArrayList<AttributeDao> parameters,String returnType){
		this.methodeName=name;
		this.parameters=parameters;
		this.returnType=returnType;
		
	}

	public String getMethodeName() {
		return methodeName;
	}

	public void setMethodeName(String methodeName) {
		this.methodeName = methodeName;
	}

	public ArrayList<AttributeDao> getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList<AttributeDao> parameters) {
		this.parameters = parameters;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	
}
