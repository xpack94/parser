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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Notifiers.AttributesNotifier;
import Notifiers.ClassNotifier;
import Notifiers.DetailsNotifier;
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

	private final String borderTitle;
	private Features features;
	private final int WIDTH=150;
	private final int HEIGHT=150;
	private final JList<String> list;
	private ShowDetailsListener detailsListener=new ShowDetailsListener();
	//prend en parametre une classe observée 
	public ScrollPane(String borderTitle,Features features){
			super();
			this.borderTitle=borderTitle;
			this.features=features;
			this.list=new JList<String>(new DefaultListModel());
			this.list.addListSelectionListener(detailsListener);
			this.setComponentInScrollPane(this.list);
			this.setBorder(new TitledBorder(this.borderTitle));
			
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
		((DefaultListModel)this.list.getModel()).clear();
		for (AttributeDao attr:((AttributesNotifier) o ).getAtrributes()){
			((DefaultListModel)this.list.getModel()).addElement( attr.getAttributeType()+" "+attr.getAttributeName()+"\r\n");
		}
	}
	private  void updateMethodes(Observable o){
		((DefaultListModel)this.list.getModel()).clear();
		if(((MethodeNotifier) o).getMethodes().size()==0){
			((DefaultListModel)this.list.getModel()).addElement("aucune Methode trouvée");
		}else{
			for(MethodeDao methode:((MethodeNotifier) o).getMethodes()){
				((DefaultListModel)this.list.getModel()).addElement(methode.getReturnType()+" "+methode.getMethodeName()+"("+
					methode.parametersToString()+")"+"\r\n");
			}
		}
		
	}
	private void updateSubClasses(Observable o){
		((DefaultListModel)this.list.getModel()).clear();
		if(((SubClassesNotifier) o).getSubClasses().size()==0){
			((DefaultListModel)this.list.getModel()).addElement("aucune sous classe");
		}else{
			for(ClassDao subClass:((SubClassesNotifier)o).getSubClasses()){
				((DefaultListModel)this.list.getModel()).addElement(subClass.getName()+"\r\n");
			}
		}
	}
	
	private void updateRelations(Observable o){
		
		JViewport viewport = this.getViewport(); 
		JList<String> relationsList= (JList<String>)viewport.getView();
		//supprimer tout les element avant d'afficher les nouveaux 
		((DefaultListModel)relationsList.getModel()).clear();
		
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
				((DefaultListModel)relationsList.getModel()).addElement("(A) P_"+
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

		
	public ShowDetailsListener getDetailsListener() {
		return detailsListener;
	}
	public void setDetailsListener(DetailsNotifier notifier){
		this.detailsListener.setDetailsNotifier(notifier);
	}


	private class ShowDetailsListener implements ListSelectionListener{
		
		
		private DetailsNotifier detailsNotifier;
		public ShowDetailsListener(DetailsNotifier detailsNotifier){
			this.detailsNotifier=detailsNotifier;
		}
		public ShowDetailsListener(){
			
		}
		
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()){
	            JList source = (JList)e.getSource();
	            String selected = source.getSelectedValue()!=null?source.getSelectedValue().toString():"";
	            this.detailsNotifier.setUpdatedType(borderTitle);
	            this.detailsNotifier.setSelectedValue(selected);
	        }
			
		}
		public void setDetailsNotifier(DetailsNotifier detailsNotifier){
			this.detailsNotifier=detailsNotifier;
		}

		
		
		
	}
	
	
}
