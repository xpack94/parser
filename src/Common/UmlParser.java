package Common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import sun.nio.cs.StandardCharsets;

public class UmlParser {
	
	private File fileToParse;
	public UmlParser(File fileToParse){
		this.fileToParse=fileToParse;
	}

	
	public byte[] readFile(){
		if(this.fileToParse !=null){
			FileReader fileReader;
			try {
				fileReader = new FileReader(this.fileToParse);
				BufferedReader bufferedReader =new BufferedReader(fileReader);
				FileInputStream fis = new FileInputStream(this.fileToParse);
				byte[] data = new byte[(int) this.fileToParse.length()];
				try {
					fis.read(data);
					fis.close();
					return data;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return new byte[1];
	}
	
	public void parseFile(String file){
		System.out.println(file);
	}
	
	
	public void setFile(File file){
		this.fileToParse=file;
	}
	
}
