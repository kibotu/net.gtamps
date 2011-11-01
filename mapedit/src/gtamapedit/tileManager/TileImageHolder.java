package gtamapedit.tileManager;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class TileImageHolder{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2769517517555300254L;
	private BufferedImage tileImage;
	private BufferedImage tileImage90;
	private BufferedImage tileImage180;
	private BufferedImage tileImage270;
	private String filename;
	public static final int tileSize = 64;

	TileImageHolder(File file) {
		this.setFilename(file.getName());
		try {
			tileImage = ImageIO.read(file);
			tileImage90 = ImageIO.read(file);
			tileImage180 = ImageIO.read(file);
			tileImage270 = ImageIO.read(file);

			AffineTransform tx = new AffineTransform();
			tx.rotate(Math.PI / 2, tileImage90.getWidth() / 2, tileImage90.getHeight() / 2);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			tileImage90 = op.filter(tileImage90, null);

			tx = new AffineTransform();
			tx.rotate(Math.PI, tileImage180.getWidth() / 2, tileImage180.getHeight() / 2);
			op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			tileImage180 = op.filter(tileImage180, null);

			tx = new AffineTransform();
			tx.rotate(-Math.PI / 2, tileImage270.getWidth() / 2, tileImage270.getHeight() / 2);
			op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			tileImage270 = op.filter(tileImage270, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BufferedImage getTileImage() {
		return tileImage;
	}

	public BufferedImage getTileImage90() {
		return tileImage90;
	}

	public BufferedImage getTileImage180() {
		return tileImage180;
	}

	public BufferedImage getTileImage270() {
		return tileImage270;
	}

	public void setTileImage(BufferedImage tileImage) {
		this.tileImage = tileImage;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + ((tileImage == null) ? 0 : tileImage.hashCode());
		result = prime * result + ((tileImage180 == null) ? 0 : tileImage180.hashCode());
		result = prime * result + ((tileImage270 == null) ? 0 : tileImage270.hashCode());
		result = prime * result + ((tileImage90 == null) ? 0 : tileImage90.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TileImageHolder other = (TileImageHolder) obj;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;

		return true;
	}
}
