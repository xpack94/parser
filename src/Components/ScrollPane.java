package Components;

import java.awt.Component;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import javax.swing.JViewport;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Notifiers.DetailsNotifier;
import Common.AggregationDao;
import Common.AttributeDao;
import Common.ClassDao;
import Common.DataApi;
import Common.Features;
import Common.MethodeDao;
import Common.RelationDao;
import Common.RelationType;


/**
 * classe qui contient toutes les composante du mileu de l'interface graphique incluant ceux des 
 * methode , attributs , relation/aggregation et sous classes 
 * 
 * 
 * **/

public class ScrollPane extends JScrollPane {

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
	
	/**
	 * @param Object qui correspond a l'objet qui est sur le point d'etre mis a jours
	 * cette methode met a jours les composante en appelant les bonne methode en fonction du type de composant
	 * 
	 * **/
	//la fonction qui va s'executer a chaque fois que les elements de la classe observée changent 
		public void update( Object o) {
			if(this.getBorderTitle().equals("Attributs") ){
				this.updateAttributes(o);
			}else if(this.getBorderTitle().equals("Methodes")){
				this.updateMethodes(o);
			}else if(this.getBorderTitle().equals("Sous Classes")){
				this.updateSubClasses(o);
			}else if(this.getBorderTitle().equals("Associations/Aggregations")){
				this.updateRelations(o);
				this.updateAggregations(o);
			}
			
		}
	
	/**@param o de type Object qui represente une liste d'attributs 
	 * 
	 * cette methode popule la liste d'attribut dans le JList et l'affiche 
	 * 
	 * 
	 * **/
	private void updateAttributes(Object o){
		// sera implementer une fois que les classes observées seront ajoutés
		((DefaultListModel)this.list.getModel()).clear();
		for (AttributeDao attr:(List<AttributeDao>)o){
			((DefaultListModel)this.list.getModel()).addElement( attr.getAttributeType()+" "+attr.getAttributeName()+"\r\n");
		}
	}
	
	
	/**
	 * @param o de type Object qui correspond a une list de methodes 
	 * 
	 *  cette methode popule le Jlist avec les methodes données en entrée et les affiches 
	 * 
	 * 
	 * **/
	private  void updateMethodes(Object o){
		((DefaultListModel)this.list.getModel()).clear();
		List<MethodeDao> methodes=(List<MethodeDao>)o;
		if(methodes.size()==0){
			((DefaultListModel)this.list.getModel()).addElement("aucune Methode trouvée");
		}else{
			for(MethodeDao methode:methodes){
				((DefaultListModel)this.list.getModel()).addElement(methode.getReturnType()+" "+methode.getMethodeName()+"("+
					methode.parametersToString()+")"+"\r\n");
			}
		}
		
	}
	
	/**
	 * 
	 * @param o de type Object qui correspond a une liste de sous classes 
	 * 
	 * cette methode popule le Jlist des sous classe et l'affiche 
	 * 
	 * 
	 * **/
	private void updateSubClasses(Object o){
		((DefaultListModel)this.list.getModel()).clear();
		List<ClassDao> subClasses=(List<ClassDao>)o;
		if(subClasses.size()==0){
			((DefaultListModel)this.list.getModel()).addElement("aucune sous classe");
		}else{
			for(ClassDao subClass:subClasses){
				((DefaultListModel)this.list.getModel()).addElement(subClass.getName()+"\r\n");
			}
		}
	}
	/**
	 * @param o de type Object qui correspond a une classe choiste dans le composant des classe 
	 * elle permet d'extraire les relation de la classe donnée en entrée et de remplire le Jlist et l'afficher
	 * 
	 * 
	 * **/
	private void updateRelations(Object o){
		
		JViewport viewport = this.getViewport(); 
		JList<String> relationsList= (JList<String>)viewport.getView();
		 List<RelationDao> relations=((ClassDao)o).getRelations();
		//supprimer tout les element avant d'afficher les nouveaux 
		((DefaultListModel)relationsList.getModel()).clear();
		
		for(RelationDao relation:relations){
			((DefaultListModel)relationsList.getModel()).addElement("(R) "+
				relation.getRelationName());
		}
		
	}
	/**
	 *  @param o de type Object qui correspond a une classe choiste dans le composant des classe 
	 * elle permet d'extraire les aggregations de la classe donnée en entrée et de remplire le Jlist et l'afficher
	 * 
	 * **/
	private void updateAggregations(Object o){
		AggregationDao aggr=DataApi.aggregations.get(((ClassDao)o).getName());
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
