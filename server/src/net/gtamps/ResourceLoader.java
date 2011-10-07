package net.gtamps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class ResourceLoader {
	
	private static final SAXBuilder saxBuilder = new SAXBuilder(); 
	
	public static Element getFileAsXml(String path) throws JDOMException, IOException {
		Element e = null;
		Document doc = saxBuilder.build(new File(path));
		e = doc.getRootElement();
		return e;
	}
	
	public static String getFileAsString(String path) throws FileNotFoundException {
		Scanner sc = null;
		String fileContent = "";
		try {
			sc = new Scanner(new BufferedReader(new FileReader(path)));
			fileContent = "";
			while(sc.hasNextLine()) {
				fileContent += sc.nextLine(); 
			}
		} finally {
			sc.close();
		}
		return fileContent;
	}

}
