package Common;

import java.util.HashMap;

/*
 * class comportant tout les données relatif au fichier parser comme 
 * les classes , les relations et les aggregation
 * **/
public class DataApi {
	
	public static HashMap<String, ClassDao> classes=new HashMap<String, ClassDao>();
	public static HashMap<String, RelationDao> relations=new HashMap<String, RelationDao>();
	public static HashMap<String,AggregationDao> aggregations=new HashMap<String, AggregationDao>();

	
}
