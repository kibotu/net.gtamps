package gtamapedit.view.map;

import gtamapedit.conf.Configuration;
import gtamapedit.tileManager.TileImageHolder;
import gtamapedit.view.ControlType;
import gtamapedit.view.MapEditorComponent;
import gtamapedit.view.MapEditorComponent.Modus;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;

import javax.swing.JComponent;

import net.gtamps.shared.game.level.EntityPosition;


public class MapView extends JComponent implements MouseListener, MouseMotionListener {
	private MapModel mm;
	private MapElement[][] map;
	int tileSize = TileImageHolder.tileSize;

	Point selectionStart = null;
	Point selectionEnd = null;
	Point currentPosition = null;
	boolean mouseDown = false;
	private MapEditorComponent mec;
	

	public MapView(MapModel mm, MapEditorComponent mec) {
		this.mm = mm;
		this.mec = mec;
		
		addMouseListener(this);
		addMouseMotionListener(this);
		reloadMap();
	}
	
	public void reloadMap(){
		this.map = this.mm.getMap();
		setPreferredSize(new Dimension(map.length*tileSize,map[0].length*tileSize));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {


				int drawX = x * TileImageHolder.tileSize;
				int drawY = y * TileImageHolder.tileSize;

				if (map[x][y].getTextureTop() == null) {
					g.setColor(Color.BLUE);
					g.fillRect(drawX, drawY, tileSize, tileSize);
				} else {
					switch(map[x][y].getRotation()){
						case 90:
							g.drawImage(map[x][y].getTextureTop().getTileImage90(), drawX, drawY, null);
							break;
						case 180:
							g.drawImage(map[x][y].getTextureTop().getTileImage180(), drawX, drawY, null);
							break;
						case 270:
							g.drawImage(map[x][y].getTextureTop().getTileImage270(), drawX, drawY, null);
							break;
						default:
							g.drawImage(map[x][y].getTextureTop().getTileImage(), drawX, drawY, null);
					}
					
				}
				
				g.setColor(Color.BLACK);
				g.fillRect(drawX, drawY, 10, 10);
				g.setColor(Color.WHITE);
				g.drawString(map[x][y].getFloors()+"", drawX, drawY+10);
			}
		}

		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				
				int drawX = x * TileImageHolder.tileSize;
				int drawY = y * TileImageHolder.tileSize;
				
				if (map[x][y].isSelected()) {
					g.setColor(Color.RED);
					g.drawRect(drawX, drawY, tileSize, tileSize);
				}
			}
		}
		
		if(mec.getMode()==Modus.TILE_MODE){
		
			if(mouseDown){
				g.setColor(Color.GREEN);
				g.drawRect(Math.min(selectionStart.x, currentPosition.x), Math.min(selectionStart.y, currentPosition.y),
						Math.abs(selectionStart.x-currentPosition.x), Math.abs(selectionStart.y-currentPosition.y) );
			}
		} else if(mec.getMode() == Modus.ENTITY_MODE){
			//Graphics2D g2d = (Graphics2D) g;
			g.setColor(new Color(0.0f,0.0f,0.0f,0.5f));
			g.fillRect(0, 0, map.length*tileSize,map[0].length*tileSize);
			for(EntityPosition ep : mm.getEntityList()){
				int es = Configuration.ENTITY_SIZE;
				if(ep.equals(mm.getEntitySelection())){
					g.setColor(Color.WHITE);
				} else {
					g.setColor(Color.RED);
				}
				g.drawOval((int)ep.getPosition().x-es/2, (int)ep.getPosition().y-es/2, es, es);
				g.drawString(ep.getType().toString(), (int)ep.getPosition().x+es/2, (int)ep.getPosition().y+es/2);
			}
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(mec.getMode() == Modus.ENTITY_MODE){
			firePropertyChange(ControlType.ENTITY_CREATE_SELECT,null,e.getPoint());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(mec.getMode() == Modus.TILE_MODE){
			selectionStart = new Point(e.getX(), e.getY());
			mouseDown = true;
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(mec.getMode() == Modus.TILE_MODE){
			selectionEnd = new Point(e.getX(), e.getY());
			mm.changeSelection(selectionStart, selectionEnd);
			mouseDown = false;
			this.repaint();
			firePropertyChange(ControlType.NEW_SELECTION,null,null);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(mec.getMode() == Modus.TILE_MODE){
			currentPosition = new Point(e.getX(),e.getY());
			this.repaint();
		} else if(mec.getMode() == Modus.ENTITY_MODE){
			if(mm.entitySelection!=null){
				mm.entitySelection.getPosition().x = e.getX();
				mm.entitySelection.getPosition().y = e.getY();
				this.repaint();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}
