package ActionListeners;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Components.ClassesList;
import Notifiers.DetailsNotifier;

public class ShowDetailsListener implements ListSelectionListener{
	
	
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
            this.detailsNotifier.setSelectedValue(selected);
        }
		
	}
	public void setDetailsNotifier(DetailsNotifier detailsNotifier){
		this.detailsNotifier=detailsNotifier;
	}

	
	
	
}
