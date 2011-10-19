package android.normalizer.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class Model {
	
	private static final SAXBuilder saxBuilder = new SAXBuilder();

	public Model() {
		
	}
	
	public static Document loadXml(File file) {
		Document document = null;
		try {
			document = saxBuilder.build(file);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}

	public static void saveXml(Document doc, File file, String beforeExtension, String afterExtension) {
		final XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat().setIndent("  "));
		PrintWriter fout = null;
		try {
			File file2 = new File(file.getAbsolutePath());
			if(!file.getAbsolutePath().endsWith(afterExtension)) {
				file2 = new File(file.getAbsolutePath().replace(beforeExtension, afterExtension));
			}
			fout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2), "UTF-8")));
			xmlOut.output(doc, fout);
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			fout.close();
		}
	}

	public static Scanner load(File file) {
		
		Scanner fin = null;
		
		try {
			fin = new Scanner(new BufferedReader(new FileReader(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return fin;
	}
}
