package Common;

import java.util.ArrayList;
import java.util.List;

/*
 * classe qui comporte toutes les informations relatif aux aggregations 
 * **/
public class AggregationDao {
	private ClassDao aggregationContainer;
	private String aggregationType;
	private List<RelationType> aggregationParts=new ArrayList<RelationType>();
	private String aggregationDetails;
	
	
	
	public AggregationDao(ClassDao aggregationContainer,String aggregationType,List<RelationType> parts){
		this.aggregationContainer=aggregationContainer;
		this.aggregationType=aggregationType;
		this.aggregationParts=parts;
	}
	public AggregationDao(ClassDao aggregationContainer,String aggregationType,List<RelationType> parts,String details){
		this.aggregationContainer=aggregationContainer;
		this.aggregationType=aggregationType;
		this.aggregationParts=parts;
		this.aggregationDetails=details;
	}

	public ClassDao getAggregationContainer() {
		return aggregationContainer;
	}

	public void setAggregationContainer(ClassDao aggregationContainer) {
		this.aggregationContainer = aggregationContainer;
	}

	public String getAggregationType() {
		return aggregationType;
	}

	public void setAggregationType(String aggregationType) {
		this.aggregationType = aggregationType;
	}

	public List<RelationType> getAggregationParts() {
		return aggregationParts;
	}

	public void setAggregationParts(List<RelationType> aggregationParts) {
		this.aggregationParts = aggregationParts;
	}

	public String getAggregationDetails() {
		return aggregationDetails;
	}

	public void setAggregationDetails(String aggregationDetails) {
		this.aggregationDetails = aggregationDetails;
	}
	
	
	
}
