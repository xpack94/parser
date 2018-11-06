package Components;

import java.awt.Dimension;
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
import Common.Metrics;
import Notifiers.ClassNotifier;
import Notifiers.DetailsNotifier;
import Notifiers.MetricsNotifier;
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
	private ScrollPane scrollPaneForAttributes;
	private ScrollPane scrollPaneForMethodes;
	private ScrollPane scrollPaneForSubClasses;
	private ScrollPane scrollPaneForRelations;

	private DetailsNotifier detailsNotifier;
	private ClassSelectionListener classSelectionListener=new ClassSelectionListener(this);
	private MetricsNotifier metricsNotifier;
	
	
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
			scrollPaneForAttributes.update(attributesOfClass);
			List<MethodeDao> methodesOfClass=chosenClass.getMethodes();
			scrollPaneForMethodes.update(methodesOfClass);
			scrollPaneForSubClasses.update(chosenClass.getSubClasses());
			scrollPaneForRelations.update(chosenClass);
			this.detailsNotifier.setClassContainerName(selectedItem);
			this.detailsNotifier.setSelectedClass(selectedItem);
			this.metricsNotifier.setSelectedClass(this.SelectedClass);
		}else if(componentTitle.equals("metriques")){
			// les elements de la liste des metrique ont été cliqué
			this.detailsNotifier.setUpdatedType("metrics");
			this.detailsNotifier.setSelectedValue(selectedItem);
			
		}
		
	}
	
	
	public void update(Observable o, Object arg) {
		
		if(this.getBorderTitle().equals("classes")){
			//on met a jours les classes
			
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
		}else if(this.getBorderTitle().equals("metriques")){
			// on met a jours les metriques
			String selectedClass=((MetricsNotifier) o).getSelectedClass();
			Metrics metrics=new Metrics();
			DefaultListModel<String> list=new DefaultListModel<String>();
			for(String metric:metrics.getMetrics()){
				
				list.addElement(metric+"="+metrics.metricsCalculator(selectedClass, metric));
				metrics.generateCsv();
			}
			this.setModel(list);
		}
		
		
		
	}
	private void addSelectEvent(){
		ClassSelectionListener listener=new ClassSelectionListener(this);
		this.addListSelectionListener(this.classSelectionListener);
	}
	
	public DetailsNotifier getDetailsNotifier() {
		return detailsNotifier;
	}
	public void setDetailsNotifier(DetailsNotifier detailsNotifier) {
		this.detailsNotifier = detailsNotifier;
	}

	
	public MetricsNotifier getMetricsNotifier() {
		return metricsNotifier;
	}
	public void setMetricsNotifier(MetricsNotifier metricsNotifier) {
		this.metricsNotifier = metricsNotifier;
	}

	



	public ScrollPane getScrollPaneForAttributes() {
		return scrollPaneForAttributes;
	}
	public void setScrollPaneForAttributes(ScrollPane scrollPaneForAttributes) {
		this.scrollPaneForAttributes = scrollPaneForAttributes;
	}
	public ScrollPane getScrollPaneForMethodes() {
		return scrollPaneForMethodes;
	}
	public void setScrollPaneForMethodes(ScrollPane scrollPaneForMethodes) {
		this.scrollPaneForMethodes = scrollPaneForMethodes;
	}
	public ScrollPane getScrollPaneForSubClasses() {
		return scrollPaneForSubClasses;
	}
	public void setScrollPaneForSubClasses(ScrollPane scrollPaneForSubClasses) {
		this.scrollPaneForSubClasses = scrollPaneForSubClasses;
	}
	public ScrollPane getScrollPaneForRelations() {
		return scrollPaneForRelations;
	}
	public void setScrollPaneForRelations(ScrollPane scrollPaneForRelations) {
		this.scrollPaneForRelations = scrollPaneForRelations;
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
