package net.gtamps.android.simple3Drenderer;

import javax.microedition.khronos.opengles.GL10;

import net.gtamps.android.simple3Drenderer.shapes.AbstractShape;
import net.gtamps.shared.game.level.Tile;

public class CubeTile {
	private static final float TILESIZE = 32f;
	private Tile tile;
	private AbstractShape texturedCube;

	CubeTile(Tile t, AbstractShape tc){
		this.tile = t;
		this.texturedCube = tc;
	}
	
	public void draw(GL10 gl){
		this.texturedCube.draw(gl);
	}
	
	public void bindTexture(GL10 gl){
		this.texturedCube.bindTexture(gl);
	}

	public float getX() {
		return (tile.getX()+TILESIZE)/TILESIZE;
	}

	public float getY() {
		return (tile.getY()+TILESIZE)/TILESIZE;
	}

	public float getRotation() {
		return tile.getRotation();
	}

	public float getHeight() {
		return tile.getHeight()/TILESIZE;
	}
}
