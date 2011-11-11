package net.gtamps;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.*;
import java.util.Scanner;

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
            while (sc.hasNextLine()) {
                fileContent += sc.nextLine();
            }
        } finally {
            sc.close();
        }
        return fileContent;
    }

}
