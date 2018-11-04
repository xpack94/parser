package Components;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Common.ClassDao;
import Common.DataApi;
import Common.Features;
import Common.MethodeDao;
import Common.RelationDao;
import Notifiers.AggregationsNotifier;
import Notifiers.AttributesNotifier;
import Notifiers.ClassNotifier;
import Notifiers.DetailsNotifier;
import Notifiers.MethodeNotifier;
import Notifiers.RelationsNotifier;
import Notifiers.SubClassesNotifier;
import Common.AttributeDao;
import java.util.List;

public class ClassesList extends JList implements Observer{

	private Features features=new Features();
	private String borderTitle="";
	private final int WIDTH=350;
	private final int HEIGHT=400;
	private final int ROW_COUNT=-1;
	private String SelectedClass="";
	private ClassNotifier classNotifier;
	private AttributesNotifier attributesNotifier;
	private MethodeNotifier methodesNotifier;
	private SubClassesNotifier subClassNotifier;
	private RelationsNotifier relationsNotifier;
	private DetailsNotifier detailsNotifier;
	private ClassSelectionListener classSelectionListener=new ClassSelectionListener(this);
	
	
	public ClassesList(ListModel data,String borderTitle,Features features){
		super(data);
		this.initList(borderTitle);
		this.features=features;
		this.addSelectEvent();
	}
	public ClassesList(ListModel data,String borderTitle){
		super(data);
		this.initList(borderTitle);
		this.addSelectEvent();
	}
	
	private void initList(String borderTitle){
		this.borderTitle=borderTitle;
		this.setBorder(new TitledBorder(this.borderTitle));
		this.setPreferredSize(new Dimension(this.WIDTH,this.HEIGHT));
		this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.setLayoutOrientation(JList.VERTICAL);
		this.setVisibleRowCount(this.ROW_COUNT);
		JScrollPane listScroller = new JScrollPane(this);
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
	
	
	public ClassNotifier getClassNotifier() {
		return classNotifier;
	}
	public void setClassNotifier(ClassNotifier classNotifier) {
		this.classNotifier = classNotifier;
	}
	public void updateChosenClass(String selectedItem){
		String componentTitle=this.getBorderTitle();
		if(componentTitle.equals("classes")){
			//cette classe est appelé a chauqe fois qu'un element de la liste est choisit
			this.SelectedClass=selectedItem;
			//on recupere la definition de la classe choisit et on notifie les observateurs de chaque composante
			ClassDao chosenClass=DataApi.classes.get(selectedItem);
			List<AttributeDao> attributesOfClass=chosenClass.getAttributes();
			this.attributesNotifier.setAtrributes(attributesOfClass);
			List<MethodeDao> methodesOfClass=chosenClass.getMethodes();
			this.methodesNotifier.setMethodes(methodesOfClass);
			this.subClassNotifier.setSubClasses(chosenClass.getSubClasses());
			this.relationsNotifier.setRelations(chosenClass.getRelations(),chosenClass);
			this.detailsNotifier.setClassContainerName(selectedItem);
		}else if(componentTitle.equals("metriques")){
			// les elements de la liste des metrique ont été cliqué
			this.detailsNotifier.setUpdatedType("metrics");
			this.detailsNotifier.setSelectedValue(selectedItem);
			
		}
		
	}
	
	public void update(Observable o, Object arg) {
		//on met a jours la liste des classes a chaque notification
		if( ((ClassNotifier)o).isShouldRemoveClass() ){				 
			//on supprime tout ce qu'il y'avait dans le ClassList
			((DefaultListModel)this.getModel()).clear();
			((ClassNotifier)o).setShouldRemoveClass(false); //remettre a zero
			return;
		}
		ClassNotifier classToBeAdded=(ClassNotifier)o ;
		if(classToBeAdded!=null){
			((DefaultListModel)this.getModel()).addElement(classToBeAdded.getClassContainer().getName());
		}
		
		
	}
	private void addSelectEvent(){
		ClassSelectionListener listener=new ClassSelectionListener(this);
		this.addListSelectionListener(this.classSelectionListener);
	}
	public AttributesNotifier getAttributesNotifier() {
		return attributesNotifier;
	}
	public void setAttributesNotifier(AttributesNotifier attributesNotifier) {
		this.attributesNotifier = attributesNotifier;
	}
	public MethodeNotifier getMethodesNotifier() {
		return methodesNotifier;
	}
	public void setMethodesNotifier(MethodeNotifier methodesNotifier) {
		this.methodesNotifier = methodesNotifier;
	}
	public SubClassesNotifier getSubClassNotifier() {
		return subClassNotifier;
	}
	public void setSubClassNotifier(SubClassesNotifier subClassNotifier) {
		this.subClassNotifier = subClassNotifier;
	}
	public RelationsNotifier getRelationsNotifier() {
		return relationsNotifier;
	}
	public void setRelationsNotifier(RelationsNotifier relationsNotifier) {
		this.relationsNotifier = relationsNotifier;
	}
	public DetailsNotifier getDetailsNotifier() {
		return detailsNotifier;
	}
	public void setDetailsNotifier(DetailsNotifier detailsNotifier) {
		this.detailsNotifier = detailsNotifier;
	}

	
	private class ClassSelectionListener implements ListSelectionListener {

		private ClassesList classesContainer;
		
		public ClassSelectionListener(ClassesList classesContainer){
			this.classesContainer=classesContainer;
		}
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()){
	            ClassesList source = (ClassesList)e.getSource();
	            if(source.getSelectedValue()!=null){
	            	String selected = source.getSelectedValue().toString();
	            	this.classesContainer.updateChosenClass(selected);
	            }
	      
	            
	            
	        }
			
		}

		

	}
	
	
	
	
	
	
}
