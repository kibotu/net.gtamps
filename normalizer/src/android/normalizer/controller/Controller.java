package android.normalizer.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.swing.JApplet;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;

import android.normalizer.Config;
import android.normalizer.model.ColorMaterial;
import android.normalizer.model.Model;
import android.normalizer.model.UV;
import android.normalizer.model.Vector;
import android.normalizer.model.Vertex;
import android.normalizer.view.MainView;

public class Controller extends JPanel implements ActionListener {

	private static final long serialVersionUID = -5076261207855491571L;
	private final MainView view;
	private final RootPaneContainer context;
	private final Model model;
	
	public enum FileCommand {
		Patch_Sprite_UV, Obj_to_Vbo, Exit
	}
	
	public Controller(RootPaneContainer context) {
		super();
		model = new Model();
		view = new MainView(this);
		this.context = context;
		try {
			UIManager.setLookAndFeel(Config.DEFAULT_WINDOW_THEME);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	
	public MainView getView() {
		return view;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		final String actionCommand = e.getActionCommand();
		
		if (actionCommand.equals(FileCommand.Exit.name())) {
			System.exit(JApplet.ABORT);
		}
		if (actionCommand.equals(FileCommand.Patch_Sprite_UV.name())) {
			final JFileChooser fc = new JFileChooser(new File(Config.DEFAULT_SAVE_DIR));

			fc.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(final File f) {
					return f.getName().toLowerCase().endsWith(".xml") || f.isDirectory();
				}
				@Override
				public String getDescription() {
					return "Raw Texture (*.xml)";
				}
			});

			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				final File file = fc.getSelectedFile();
				
				// load 
				Document doc = Model.loadXml(file);
				
				processUV(doc);
				
				// save under new name
				Model.saveXml(doc, file, ".xml", "_processed.xml");
			}
		}
		
		if (actionCommand.equals(FileCommand.Obj_to_Vbo.name())) {
			final JFileChooser fc = new JFileChooser(new File(Config.DEFAULT_SAVE_DIR));

			fc.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(final File f) {
					return f.getName().toLowerCase().endsWith(".obj") || f.isDirectory();
				}
				@Override
				public String getDescription() {
					return "Raw Obj (*.obj)";
				}
			});

			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				final File file = fc.getSelectedFile();
				
				// load 
				Scanner sin = Model.load(file);
				
				Document doc = processObj(sin,file.getParent());
				
				// save under new name
				Model.saveXml(doc, file, ".obj", "_processed.vbo");
			}
		}
	}

	private Document processObj(Scanner sin, String filepath) {
		
		boolean hasNormals = false;
		boolean hasTextureCoordinates = false;
		boolean hasColorMaterials = false;
		
		// build xml
		Document doc = new Document();
		final Element root = new Element("object");
		doc.setRootElement(root);
		
		Element infoElemnet = new Element("info");
		Element verticeElement = new Element("vertices");
		Element indicesElement = new Element("indices");
		Element normalsElement = new Element("normals");
		Element colorsElement = new Element("colors");
		Element textureCoordsElement = new Element("textureCoords");
		root.addContent(infoElemnet);
		root.addContent(verticeElement);
		root.addContent(indicesElement);
		root.addContent(normalsElement);
		root.addContent(colorsElement);
		root.addContent(textureCoordsElement);
		
		// build temp lists
		ArrayList<Vector> vertices = new ArrayList<Vector>();
		ArrayList<Vector> normals = new ArrayList<Vector>();
		ArrayList<ColorMaterial> colors = new ArrayList<ColorMaterial>();
		ArrayList<UV> textureCoords = new ArrayList<UV>();
		
		// current to-save-list of vertices (no doubles)
		ArrayList<Vertex> vertexe = new ArrayList<Vertex>(); 
		
		// faces 
		StringBuilder verticesBuilder = new StringBuilder();
		StringBuilder indicesBuilder = new StringBuilder();
		StringBuilder normalsBuilder = new StringBuilder();
		StringBuilder textureCoordsBuilder = new StringBuilder();
		StringBuilder ambientColorBuilder = new StringBuilder();
		StringBuilder diffuseColorBuilder = new StringBuilder();
		StringBuilder specularColorBuilder = new StringBuilder();
		
		String usemtl = "";
		
		// parse lists
		while(sin.hasNextLine()) {
			
			String line = sin.nextLine();
			
			if(line.startsWith("usemtl ")) {
				usemtl = line.substring("usemtl ".length());
			}
			
			if(line.startsWith("mtllib ")) {
				Scanner scanner = new Scanner(line);
				scanner.next();
				colors = parseColorMaterials(filepath + "/"+ scanner.next());
				hasColorMaterials = true;
			} else if(line.startsWith("vn ")) {
				normals.add(parseVector(line));
				hasNormals = true;
			} else if(line.startsWith("vt ")) {
				textureCoords.add(parseUV(line));
				hasTextureCoordinates = true;
			} else if(line.startsWith("v ")) {
				vertices.add(parseVector(line));
			} else if(line.startsWith("f ")) {
				
				// build triangle
				int [] face = parseFace(line, hasTextureCoordinates, hasNormals);
				
				ColorMaterial color = null; 
				if(hasColorMaterials) {
					for(ColorMaterial temp : colors) {
						if(temp.name.equals(usemtl)) {
							color = temp;
						}
					}
				}
				
				if(hasTextureCoordinates && hasNormals) {
					// face  v1/t1/n1 v2/t2/n2 v3/t3/n3
                    ArrayList<Float[]> temp = new ArrayList<Float[]>();
                    Vertex[] vertexFace = new Vertex[3];
					for(int i = 0; i < 3; i++) {
						vertexFace[i] = new Vertex(vertices.get(face[i*3]),textureCoords.get(face[i*3+1]),normals.get(face[i*3+2]),color);
                        temp.add(vertexFace[i].vertice.toFloatArray());
						if(!vertexe.contains(vertexFace[i])) {
							vertexe.add(vertexFace[i]);
						} 
						indicesBuilder.append(vertexe.indexOf(vertexFace[i]) + " ");
					}

                    // recalc normals
                    Float[] correctFaceNormal = getArbitraryPolygonNormal(temp);
                    for(int i = 0; i < vertexFace.length; i++) {
                        vertexFace[i].normal.set(correctFaceNormal);
                    }

				} else if(!hasTextureCoordinates && hasNormals) {
					// face  v1//n1 v2//n2 v3/t3/n3
					for(int i = 0; i < 3; i++) {
						Vertex vertex = new Vertex(vertices.get(face[i*3]),normals.get(face[i*3+2]),color);
						if(!vertexe.contains(vertex)) {
							vertexe.add(vertex);
						} 
						indicesBuilder.append(vertexe.indexOf(vertex) + " ");
					}
					
				} else if(!hasTextureCoordinates && !hasNormals) {
					
				}
			}
		}

		// finish document
		for(int i = 0; i < vertexe.size(); i++) {
			verticesBuilder.append(vertexe.get(i).vertice + " ");
			normalsBuilder.append(vertexe.get(i).normal + " ");
			UV uv = vertexe.get(i).texture;
			if(uv != null) {
				textureCoordsBuilder.append(uv + " ");
			}
			ColorMaterial color = vertexe.get(i).colors;
			if(color != null) {
				ambientColorBuilder.append(color.ambient + " 1 ");
				diffuseColorBuilder.append(color.diffuse + " 1 ");
				specularColorBuilder.append(color.specular + " 1 ");
			}
		}
		verticeElement.setText(verticesBuilder.toString());
		normalsElement.setText(normalsBuilder.toString());
		textureCoordsElement.setText(textureCoordsBuilder.toString());
		indicesElement.setText(indicesBuilder.toString());
		
		colorsElement.addContent(new Element("ambient").setText(ambientColorBuilder.toString()));
		colorsElement.addContent(new Element("diffuse").setText(diffuseColorBuilder.toString()));
		colorsElement.addContent(new Element("specular").setText(specularColorBuilder.toString()));
		
		// set info
		infoElemnet.setAttribute("indices",""+indicesBuilder.toString().split(" ").length);
		infoElemnet.setAttribute("vertices",""+verticesBuilder.toString().split(" ").length/3);
		infoElemnet.setAttribute("normals",""+normalsBuilder.toString().split(" ").length/3);
		infoElemnet.setAttribute("uvs",""+ textureCoordsBuilder.toString().split(" ").length/2);
		infoElemnet.setAttribute("ambientColor", "" + ambientColorBuilder.toString().split(" ").length/4);
		infoElemnet.setAttribute("diffuseColor", "" + diffuseColorBuilder.toString().split(" ").length/4);
		infoElemnet.setAttribute("specularColor", "" + specularColorBuilder.toString().split(" ").length/4);
		
		return doc;
	}

	private int[] parseFace(String line, boolean hasTextures, boolean hasNormals) {
		
		int [] face = new int[9];
		
		line = line.replace("f ", "");
		line = line.replace("/", " ");
		Scanner scanner = new Scanner(line);	
		
		if(hasTextures && hasNormals) {
			face[0] = scanner.nextInt()-1; // v1
			face[1] = scanner.nextInt()-1; // t1
			face[2] = scanner.nextInt()-1; // n1
			face[3] = scanner.nextInt()-1; // v2
			face[4] = scanner.nextInt()-1; // t2
			face[5] = scanner.nextInt()-1; // n2
			face[6] = scanner.nextInt()-1; // v3
			face[7] = scanner.nextInt()-1; // t3
			face[8] = scanner.nextInt()-1; // n3
			
		} else if(!hasTextures && hasNormals) {
			face[0] = scanner.nextInt()-1; // v1
			face[1] = 0;				 // t1
			face[2] = scanner.nextInt()-1; // n1
			face[3] = scanner.nextInt()-1; // v2
			face[4] = 0;				 // t2
			face[5] = scanner.nextInt()-1; // n2
			face[6] = scanner.nextInt()-1; // v3
			face[7] = 0;			 	 // t3
			face[8] = scanner.nextInt()-1; // n3
			
		} else if(!hasTextures && !hasNormals) {
			face[0] = scanner.nextInt()-1; // v1
			face[1] = 0;				 // t1
			face[2] = 0; 				 // n1
			face[3] = scanner.nextInt()-1; // v2
			face[4] = 0;				 // t2
			face[5] = 0; 				 // n2
			face[6] = scanner.nextInt()-1; // v3
			face[7] = 0;				 // t3
			face[8] = 0; 				 // n3
		}
		
		return face;
	}

	private Vector parseVector(String line) {
		Scanner scanner = new Scanner(line);
		// initial letter definition not needed
		scanner.next();
		return new Vector(Float.parseFloat(scanner.next()),Float.parseFloat(scanner.next()),Float.parseFloat(scanner.next()));
	}

	private UV parseUV(String line) {
		Scanner scanner = new Scanner(line);
		// initial letter definition not needed
		scanner.next();
		return new UV(Float.parseFloat(scanner.next()),Float.parseFloat(scanner.next()));
	}

	private ArrayList<ColorMaterial> parseColorMaterials(String filepath) {
		ArrayList<ColorMaterial> colors = new ArrayList<ColorMaterial> ();
		Scanner fin = Model.load(new File(filepath));
		
		while(fin.hasNextLine()) {
			
			String line = fin.nextLine();
			
			if(line.startsWith("newmtl ")) {
				// newmtl
				String name = line.substring("newmtl ".length());
				// Ns not needed
				fin.nextLine();
				colors.add(new ColorMaterial(name,parseVector(fin.nextLine()),parseVector(fin.nextLine()),parseVector(fin.nextLine())));
			} else {
				continue;
			}
		}
		
		// parse materials
		return colors;
	}

    /**
     * compute arbitrary normal for a face
     *
     * @param face
     * @return normal
     */
    protected static Float[] getArbitraryPolygonNormal(List<Float[]> face) {
        Float[] normal = new Float[] {0f,0f,0f};
        for(int i = 0; i < face.size(); ++i) {
            Float[] current = face.get(i);
            Float[] next = face.get((i+1) % face.size());
            normal[0] += (current[1]-next[1])*(current[2]+next[2]);
            normal[1] += (current[2]-next[2])*(current[0]+next[0]);
            normal[2] += (current[0]-next[0])*(current[1]+next[1]);
        }
        return normal;
    }

	/**
	 * <p>Basically turns texture coordinates into normalized values.</p>
	 * <p>e.g.:</p> 
	 * <p>dimension = 512x512</p>
	 * <p>x = 85, y = 69, w = 16, h = 35</p> 
	 * <p>=>x = 0.16601562 y = 0.13476562 w =  0.03125  h = 0.068359375</p> 
	 * 
	 * @param doc
	 */
	private void processUV(Document doc) {
		
		Element dim = doc.getRootElement().getChild("dimension");
		try {
			int width = dim.getAttribute("width").getIntValue();
			int height = dim.getAttribute("height").getIntValue();
			Attribute isNormalized = dim.getAttribute("normalized");
			
			if(!isNormalized.getBooleanValue()) {
				@SuppressWarnings("unchecked")
				List<Element> imgs = doc.getRootElement().getChildren("img");
				
				for(int i = 0; i < imgs.size(); i++) {

					Element img = imgs.get(i);
					
					Attribute aX = img.getAttribute("x");
					Attribute aY = img.getAttribute("y");
					Attribute aW = img.getAttribute("w");
					Attribute aH = img.getAttribute("h");
					
					aX.setValue(""+(aX.getFloatValue()/width));
					aY.setValue(""+(aY.getFloatValue()/height));
					aW.setValue(""+(aW.getFloatValue()/width));
					aH.setValue(""+(aH.getFloatValue()/height));
				}
				
				isNormalized.setValue("true");
			}
			
		} catch (DataConversionException e) {
			e.printStackTrace();
		}
	}
}

