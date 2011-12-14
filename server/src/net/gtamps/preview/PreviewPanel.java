package net.gtamps.preview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import net.gtamps.preview.view.BodyView;
import net.gtamps.preview.view.PreviewPerspective;

import org.jbox2d.common.Vec2;

public class PreviewPanel extends JPanel  {
	
	private static final Color DEFAULT_BACKGROUND = Color.BLACK;
	private static final Color DEFAULT_FOREGROUND = Color.WHITE;
	
	private final List<BodyView> bodyViews = new ArrayList<BodyView>();
	
	public PreviewPanel() {
		
		
        addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(final MouseWheelEvent e) {
				final int amount = e.getWheelRotation();
				final Point loc = e.getPoint();
				PreviewPerspective.zoom(amount, new Vec2(loc.x, loc.y));
				repaint();
			}
		});
        
        addMouseListener(new MouseListener() {
			
        	Point clickPoint;
        	
			@Override
			public void mouseReleased(final MouseEvent e) {
				final Point nowPoint = e.getPoint();
				final Vec2 diff = new Vec2(nowPoint.x - clickPoint.x, nowPoint.y - clickPoint.y);
//				clickPoint = nowPoint;
				PreviewPerspective.move(diff.x, diff.y);
				repaint();
			}
			
			@Override
			public void mousePressed(final MouseEvent e) {
				clickPoint = e.getPoint();
			}
			
			@Override
			public void mouseExited(final MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(final MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(final MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        
	}
	
	public void addBody(final BodyView bodyView) {
		bodyViews.add(bodyView);
	}
	
	@Override
	protected void paintComponent(final Graphics g) {
		final Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(DEFAULT_FOREGROUND);
		g2d.setBackground(DEFAULT_BACKGROUND);
		g2d.clearRect(0, 0, getWidth(), getHeight());
		
		g2d.drawString(PreviewPerspective.getZoomLevel() + "", 10, 10);
		g2d.drawString(PreviewPerspective.getOffset().toString() + "", 10, 20);
		
		g2d.scale(PreviewPerspective.getZoomLevel(), PreviewPerspective.getZoomLevel());
		g2d.translate(PreviewPerspective.getOffset().x, PreviewPerspective.getOffset().y);

		for (final BodyView bv : bodyViews) {
			bv.paint(g2d);
		}
		
		
	}

}
