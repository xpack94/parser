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

import ActionListeners.ClassSelectionListener;
import Common.ClassDao;
import Common.DataApi;
import Common.Features;
import Common.MethodeDao;
import Notifiers.AttributesNotifier;
import Notifiers.ClassNotifier;
import Notifiers.MethodeNotifier;
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
	
	
	public ClassNotifier getClassNotifier() {
		return classNotifier;
	}
	public void setClassNotifier(ClassNotifier classNotifier) {
		this.classNotifier = classNotifier;
	}
	public void updateChosenClass(String selectedClass){
		//cette classe est appelé a chauqe fois qu'un element de la liste est choisit
		this.SelectedClass=selectedClass;
		//on recupere la definition de la classe choisit et on notifie les observateurs de chaque composante
		ClassDao chosenClass=DataApi.classes.get(selectedClass);
		List<AttributeDao> attributesOfClass=chosenClass.getAttributes();
		this.attributesNotifier.setAtrributes(attributesOfClass);
		List<MethodeDao> methodesOfClass=chosenClass.getMethodes();
		this.methodesNotifier.setMethodes(methodesOfClass);
		this.subClassNotifier.setSubClasses(chosenClass.getSubClasses());
		
		
	}
	
	public void update(Observable o, Object arg) {
		//on met a jours la liste des classes a chaque notification
		ClassNotifier classToBeAdded=(ClassNotifier)o ;
		((DefaultListModel)this.getModel()).addElement(classToBeAdded.getClassContainer().getName());
		
	}
	private void addSelectEvent(){
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
	
	
	
	
	
	
}
