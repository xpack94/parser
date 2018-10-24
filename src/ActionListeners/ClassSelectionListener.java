package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Components.ClassesList;

public class ClassSelectionListener implements ListSelectionListener {

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
