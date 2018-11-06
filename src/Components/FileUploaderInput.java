package Components;
import javax.swing.JTextField;
import Common.Features;

public class FileUploaderInput extends JTextField {

	private Features features=new Features();
	private  boolean IS_ENABLED=false;
	private String fileName;
	public FileUploaderInput(Features features){
		super();
		this.features=features;
		this.setEnabled(this.IS_ENABLED);
	}
	public FileUploaderInput(String text){
		super(text);
		this.setEnabled(this.IS_ENABLED);
	}
	
	public FileUploaderInput(String text,Features features){
		super(text);
		this.features=features;
		this.setEnabled(this.IS_ENABLED);
	}
		
	public Features getComponentFeatures(){
		return this.features;
	}
	public void setComponentFeatures(Features features){
		this.features=features;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
		this.setText(fileName);
	}
		
}

