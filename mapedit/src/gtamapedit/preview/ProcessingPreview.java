package gtamapedit.preview;

import gtamapedit.file.MapFile;
import gtamapedit.file.MapFileTileElement;
import processing.core.*;

public class ProcessingPreview extends PApplet {
	MapFile mapFile;
	/*
	 * public ProcessingPreview(MapFile mapfile){ this.mapFile = mapfile; }
	 */
	PImage[][] tex;
	float rotx = PI / 4;
	float roty = PI / 4;
	
	float zoom = 30f;

	public void setup() {
		size(1024, 768, P3D);
		
		
		
		int mapXSize = MapReference.getMapFile().getRawData().length;
		int mapYSize = MapReference.getMapFile().getRawData()[0].length;
		tex = new PImage[mapXSize][mapYSize];
		for (int x = 0; x < mapXSize; x++) {
			for (int y = 0; y < mapXSize; y++) {
				if (MapReference.getMapFile().getRawData()[x][y].getTextureTop() != null && 
						MapReference.getMapFile().getRawData()[x][y].getTextureTop() != "") {
					tex[x][y] = loadImage("/home/tom/studium/mmp2/mapedit/tile_images/"
							+ MapReference.getMapFile().getRawData()[x][y].getTextureTop());
				}
			}

		}
		// tex =
		// loadImage("/home/tom/studium/mmp2/mapedit/tile_images/GTATILES-010.jpg");
		// textureMode(NORMALIZED);
		textureMode(NORMALIZED);
		fill(255);
		stroke(color(44, 48, 32));
	}

	private float eyex = 0f;
	private float eyey = 0f;
	private float eyez = 0f;
	private float centerx = 0f;
	private float centery = 0f;
	private float centerz = 0f;
	
	public void keyPressed() {
		  if(key == 'w'){
			  eyez+=10f;
		  }
		  if(key == 's'){
			  eyez-=10f;
		  }
		  if(key == 'a'){
			  eyex+=10f;
		  }
		  if(key == 'd'){
			  eyex-=10f;
		  }
		}
	
	public void draw() {
		//camera(eyex, eyey, eyez, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
		
		int mapXSize = MapReference.getMapFile().getRawData().length;
		int mapYSize = MapReference.getMapFile().getRawData()[0].length;
		background(0);
		noStroke();
		translate((float) (width / 2.0), (float) (height / 2.0), (float) (-100));
		scale(zoom);
		rotateX(rotx);
		rotateY(roty);
		translate(eyex, 0f, eyez);
		
		translate(-(float) (mapXSize / 2.0),- (float) (mapYSize / 2.0), -10f);

		

		for (int x = 0; x < mapXSize; x++) {
			for (int y = 0; y < mapXSize; y++) {
				MapFileTileElement el = MapReference.getMapFile().getRawData()[x][y];
				pushMatrix();
				//2d y is 3d z!
				translate(x * 2, 0, y * 2);
				
				if (tex[x][y] != null) {
					pushMatrix();
					for (int z = -1; z < el.getFloors(); z++) {
						translate(0, -2f, 0 );
						pushMatrix();
							rotateY(-(float) (el.getRotation() / 180 * Math.PI));
							//UGLY HACK: something is wrong: to lazy to fix it.
							if(el.getRotation()==90 || el.getRotation()==270){
								rotateY(-PI/2f);	
							}
							
							TexturedCube(tex[x][y]);
						popMatrix();
					}
					popMatrix();
				}
				popMatrix();
			}
		}
	}

	void TexturedCube(PImage tex) {
		beginShape(QUADS);
		texture(tex);

		// Given one texture and six faces, we can easily set up the uv
		// coordinates
		// such that four of the faces tile "perfectly" along either u or v, but
		// the other
		// two faces cannot be so aligned. This code tiles "along" u, "around"
		// the X/Z faces
		// and fudges the Y faces - the Y faces are arbitrarily aligned such
		// that a
		// rotation along the X axis will put the "top" of either texture at the
		// "top"
		// of the screen, but is not otherwised aligned with the X/Z faces.
		// (This
		// just affects what type of symmetry is required if you need seamless
		// tiling all the way around the cube)

		// +Z "front" face
		vertex(-1, -1, 1, 0, 0);
		vertex(1, -1, 1, 1, 0);
		vertex(1, 1, 1, 1, 1);
		vertex(-1, 1, 1, 0, 1);

		// -Z "back" face
		vertex(1, -1, -1, 0, 0);
		vertex(-1, -1, -1, 1, 0);
		vertex(-1, 1, -1, 1, 1);
		vertex(1, 1, -1, 0, 1);

		// +Y "bottom" face
		vertex(-1, 1, 1, 0, 0);
		vertex(1, 1, 1, 1, 0);
		vertex(1, 1, -1, 1, 1);
		vertex(-1, 1, -1, 0, 1);

		// -Y "top" face
		texture(tex);
	
		vertex(1, -1, -1, 1, 0);
		
		vertex(-1, -1, -1, 0, 0);
		vertex(-1, -1, 1, 0, 1);
		vertex(1, -1, 1, 1, 1);
		
		// +X "right" face
		vertex(1, -1, 1, 0, 0);
		vertex(1, -1, -1, 1, 0);
		vertex(1, 1, -1, 1, 1);
		vertex(1, 1, 1, 0, 1);

		// -X "left" face
		vertex(-1, -1, -1, 0, 0);
		vertex(-1, -1, 1, 1, 0);
		vertex(-1, 1, 1, 1, 1);
		vertex(-1, 1, -1, 0, 1);

		endShape();
	}

	public void mouseDragged() {
		float rate = 0.01f;
		rotx += (pmouseY - mouseY) * rate;
		roty += (mouseX - pmouseX) * rate;
	}

	public void mouseWheelMoved(int wheelRotation) {
		// TODO Auto-generated method stub
		zoom-=wheelRotation/2f;
	}
}
