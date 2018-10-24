package ActionListeners;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import controller.FrameFactory;

import Components.FileUploaderButton;


public class UploadFileListener implements ActionListener{
	private FileUploaderButton fileUploaderButton;
	
	
	public UploadFileListener(FileUploaderButton fileUploaderButton){
		this.fileUploaderButton=fileUploaderButton;
	}
	
	//la methode qui s'occupe d'afficher le fileChooser et retourne le fichier selectionné
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser =new JFileChooser();
		int returnVal = fileChooser.showOpenDialog( (Component) e.getSource());
		    if (returnVal == JFileChooser.APPROVE_OPTION){ ;
		        File file = fileChooser.getSelectedFile();
		        try {
		          this.fileUploaderButton.setReadFile(file);
		        } catch (Exception ex) {
		        	JOptionPane.showMessageDialog(FrameFactory.getFrame(), "probleme d'acces au fichier"+file.getAbsolutePath(),
		        			"Erreur",JOptionPane.ERROR_MESSAGE);
		        }
		    } 
		    else {
		        System.out.println("access au fichier annulé.");
		    }       
		}   

}
