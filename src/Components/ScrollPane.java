package Components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.border.TitledBorder;
import Notifiers.AttributesNotifier;
import Notifiers.ClassNotifier;
import Notifiers.MethodeNotifier;
import Notifiers.RelationsNotifier;
import Notifiers.SubClassesNotifier;
import Common.AggregationDao;
import Common.AttributeDao;
import Common.ClassDao;
import Common.DataApi;
import Common.Features;
import Common.MethodeDao;
import Common.RelationDao;
import Common.RelationType;
import Notifiers.AttributesNotifier;

public class ScrollPane extends JScrollPane implements Observer {

	private String borderTitle;
	private Features features;
	private final int WIDTH=150;
	private final int HEIGHT=150;
	//prend en parametre une classe observée 
	public ScrollPane(String borderTitle,Features features){
			super();
			this.borderTitle=borderTitle;
			this.features=features;
			this.setBorder(new TitledBorder(this.borderTitle));
			
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
			
	//la fonction qui va s'executer a chaque fois que les elements de la classe observée changent 
	public void update(Observable o, Object arg) {
		if(this.getBorderTitle().equals("Attributs") ){
			this.updateAttributes(o);
		}else if(this.getBorderTitle().equals("Methodes")){
			this.updateMethodes(o);
		}else if(this.getBorderTitle().equals("Sous Classes")){
			this.updateSubClasses(o);
		}else if(this.getBorderTitle().equals("Associations/Aggregations")){
			this.updateRelations(o);
			this.updateAggregations((ClassDao)arg);
		}
		
	}
	
	private void updateAttributes(Observable o){
		// sera implementer une fois que les classes observées seront ajoutés
		JViewport viewport = this.getViewport(); 
		JTextArea textArea= (JTextArea)viewport.getView();
		textArea.setText("");
		for (AttributeDao attr:((AttributesNotifier) o ).getAtrributes()){
			textArea.append( attr.getAttributeType()+" "+attr.getAttributeName()+"\r\n");
		}
	}
	private  void updateMethodes(Observable o){
		JViewport viewport = this.getViewport(); 
		JTextArea textArea= (JTextArea)viewport.getView();
		textArea.setText("");
		if(((MethodeNotifier) o).getMethodes().size()==0){
			textArea.append("aucune Methode trouvée");
		}else{
			for(MethodeDao methode:((MethodeNotifier) o).getMethodes()){
				textArea.append(methode.getReturnType()+" "+methode.getMethodeName()+"("+
					methode.parametersToString()+")"+"\r\n");
			}
		}
		
		
	}
	private void updateSubClasses(Observable o){
		JViewport viewport = this.getViewport(); 
		JTextArea textArea= (JTextArea)viewport.getView();
		textArea.setText("");
		if(((SubClassesNotifier) o).getSubClasses().size()==0){
			textArea.append("aucune sous classe");
		}else{
			for(ClassDao subClass:((SubClassesNotifier)o).getSubClasses()){
				textArea.append(subClass.getName()+"\r\n");
			}
		}
	}
	
	private void updateRelations(Observable o){
		
		JViewport viewport = this.getViewport(); 
		JList<String> relationsList= (JList<String>)viewport.getView();
		//supprimer tout les element avant d'afficher les nouveaux 
		((DefaultListModel)relationsList.getModel()).removeAllElements();
		for(RelationDao relation:((RelationsNotifier)o).getRelations()){
			((DefaultListModel)relationsList.getModel()).addElement("(R) "+
				relation.getRelationName());
		}
		
	}
	
	private void updateAggregations(ClassDao selectedClass){
		AggregationDao aggr=DataApi.aggregations.get(selectedClass.getName());
		JViewport viewport = this.getViewport(); 
		JList<String> relationsList= (JList<String>)viewport.getView();
		if(aggr!=null){
			for(RelationType parts:aggr.getAggregationParts()){
				((DefaultListModel)relationsList.getModel()).addElement("(A) "+
						parts.getRelatedTo().getName());
			}
		}
		
		
		
	}
	
	public void setComponentInScrollPane(Component c){
		this.getViewport().add(c);
	}
	public Features getFeatures() {
		return this.features;
	}
	public void setFeatures(Features features) {
		this.features = features;
	}
	public String getBorderTitle() {
		return this.borderTitle;
	}
	public void setBorderTitle(String borderTitle) {
		this.borderTitle = borderTitle;
	}
}
