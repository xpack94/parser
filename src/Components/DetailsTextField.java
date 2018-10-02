package Components;

import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import Common.DataApi;
import Common.Features;
import Common.RelationDao;
import Notifiers.DetailsNotifier;

public class DetailsTextField extends JTextArea implements Observer {
	Features features =new Features();
	
	public DetailsTextField(String title){
		super();
		this.setLineWrap(true);
		this.setBorder(new TitledBorder(title));
		
	}
	public DetailsTextField(String title,Features features){
		super();
		this.setLineWrap(true);
		this.setBorder(new TitledBorder(title));
		this.features=features;
	}
	
	private void setFeaturesOfComponent(int gridx,int gridy,int gridWidth,int gridHeight,int fill,int anchor
			,Insets insets,int ipadx,int ipady,float weightx,float weighty){
		this.features.gridx=gridx;
		this.features.gridy=gridy;
		this.features.gridwidth=gridWidth;
		this.features.gridheight=gridHeight;
		this.features.fill=fill;
		this.features.anchor=anchor;
		this.features.insets=insets;
		this.features.ipady=ipady;
		this.features.ipadx=ipadx;
		this.features.weightx=weightx;
		this.features.weighty=weighty;
	}
	
	public Features getFeatures(){
		return this.features;
	}
	public void setFeatures(Features features){
		this.features=features;
	}
	public void update(Observable o, Object arg) {
		//extraire la valeur 
		String selectedValue=((DetailsNotifier)o).getSelectedValue();
		String details="";
		if(selectedValue.contains(("R"))){
			//afficher les details de la relations
			String relationName=selectedValue.substring(selectedValue.indexOf(")")+1,selectedValue.length()).trim();
			RelationDao relation=DataApi.relations.get(relationName);
			if(relation!=null){
				details=relation.getRelationDetails();
			}
			
		}else if(selectedValue.contains("A")){
			//afficher les details de l'aggregations
			
		}
		this.setText("");
		this.append(details);
		
	}
}
