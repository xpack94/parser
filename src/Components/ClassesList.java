package Components;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import Common.Features;

public class ClassesList extends JList{

	private Features features=new Features();
	private String borderTitle="";
	private final int WIDTH=350;
	private final int HEIGHT=400;
	private final int ROW_COUNT=-1;
	
	
	
	public ClassesList(ListModel data,String borderTitle,Features features){
		super(data);
		this.initList(data, borderTitle);
		this.features=features;
	}
	public ClassesList(ListModel data,String borderTitle){
		super(data);
		this.initList(data, borderTitle);
	}
	
	private void initList(ListModel data,String borderTitle){
		this.borderTitle=borderTitle;
		this.setBorder(new TitledBorder(this.borderTitle));
		this.setPreferredSize(new Dimension(this.WIDTH,this.HEIGHT));
		this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.setLayoutOrientation(JList.VERTICAL);
		this.setVisibleRowCount(this.ROW_COUNT);
		JScrollPane listScroller = new JScrollPane(this);
	}
	
	public void setObservedClass(Object o){
		// code to be implemented soon
	}
	
	//fonction decrivant tout les caracteristiques que le composante va avoir
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
	
	
	public Features getFeatures() {
		return features;
	}
	public void setFeatures(Features features) {
		this.features = features;
	}
	public String getBorderTitle() {
		return borderTitle;
	}
	public void setBorderTitle(String borderTitle) {
		this.borderTitle = borderTitle;
	}
	
	
	
	
}
