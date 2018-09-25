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
	
	public ClassesList(ListModel data,String borderTitle){
		super(data);
		this.borderTitle=borderTitle;
		this.setBorder(new TitledBorder(this.borderTitle));
		this.setPreferredSize(new Dimension(this.WIDTH,this.HEIGHT));
		this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.setLayoutOrientation(JList.VERTICAL);
		this.setVisibleRowCount(this.ROW_COUNT);
		JScrollPane listScroller = new JScrollPane(this);
		this.setFeaturesOfComponent();
	}
	
	public void setObservedClass(Object o){
		// code to be implemented soon
	}
	
	//fonction decrivant tout les caracteristiques que le composante va avoir
		private void setFeaturesOfComponent(){
			this.features.gridx=0;
			this.features.gridy=1;
			this.features.gridwidth=2;
			this.features.gridheight=3;
			this.features.fill=GridBagConstraints.VERTICAL;
			this.features.anchor=GridBagConstraints.LINE_START;
			this.features.insets=new Insets(0, 0, 0, 15);
			this.features.ipady=0;
			this.features.weightx=0;
			this.features.weighty=1;
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
