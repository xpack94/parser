package Common;

import java.util.ArrayList;


/**
 * classe qui contient les informations relatif aux methodes d'une classe 
 * */
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
	//methode qui retourne les types des parametres sous forme de String
	public String parametersToString(){
		String parametersToString="";
		for(AttributeDao parameter:this.parameters){
			if(!parametersToString.equals("")){
				parametersToString+=","+parameter.getAttributeType();
			}else{
				parametersToString+=parameter.getAttributeType();
			}
			
		}
		return parametersToString;
	}
	
}
