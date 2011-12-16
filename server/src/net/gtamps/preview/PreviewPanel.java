package net.gtamps.preview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import net.gtamps.preview.view.BodyView;
import net.gtamps.preview.view.PreviewPerspective;
import net.gtamps.preview.view.Scene;
import net.gtamps.shared.Utils.MovingFloatAverage;

public class PreviewPanel extends JPanel {
	
	private static final Color DEFAULT_BACKGROUND = Color.BLACK;
	private static final Color DEFAULT_FOREGROUND = Color.WHITE;
	
	private static final int TARGET_FPS = 20;
	private static final int TARGET_FPS_SPEED = 1000 / TARGET_FPS;
	
//	private final List<BodyView> bodyViews = new ArrayList<BodyView>();
	private final PreviewPerspective perspective;
	private final Scene scene;
	private final Timer timer;
	private final MovingFloatAverage fpsAverage = new MovingFloatAverage(5);
	
	private boolean dragScene = false;
	private Point clickPoint;
	
	public PreviewPanel(final PreviewPerspective perspective) {
		if (perspective == null) {
			throw new IllegalArgumentException("'perspective' must not be 'null'");
		}
		this.perspective = perspective;
		scene = new Scene(perspective);
		timer = new Timer(TARGET_FPS_SPEED, new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				updateContent();
			}
			
		});
		
        addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(final MouseWheelEvent e) {
				final int amount = e.getWheelRotation();
				final Point loc = e.getPoint();
//				PreviewPerspective.zoom(amount, new Vec2(loc.x, loc.y));
				perspective.zoomIn(loc, -amount);
				updateContent();
			}
		});
        
        addMouseListener(new MouseAdapter() {
			
        	
			@Override
			public void mouseReleased(final MouseEvent e) {
				dragScene = false;
				
			}
			
			@Override
			public void mousePressed(final MouseEvent e) {
				dragScene = true;
				clickPoint = e.getPoint();
				
			}
		});
        
        addMouseMotionListener(new MouseMotionListener() {
			
        	
			@Override
			public void mouseMoved(final MouseEvent e) {
				
				
			}
			
			@Override
			public void mouseDragged(final MouseEvent e) {
				if(dragScene){
					final Point nowPoint = e.getPoint();
					final int dx = nowPoint.x - clickPoint.x;
					final int dy = nowPoint.y - clickPoint.y;
					perspective.move(dx, dy);
					updateContent();
					clickPoint = e.getPoint();
				}
				
			}
		});
	}
	
	public void addBody(final BodyView bodyView) {
		scene.addChild(bodyView);
	}
	
	public void updateContent() {
		scene.update();
		repaint();
	}
	
	public void startAutoUpdate() {
		lastTime = System.currentTimeMillis();
		timer.start();
	}
	
	public void stopAutoUpdate() {
		timer.stop();
	}

	@Override
	protected void paintComponent(final Graphics g) {
		computeFps();
		
		final Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(DEFAULT_FOREGROUND);
		g2d.setBackground(DEFAULT_BACKGROUND);
		g2d.clearRect(0, 0, getWidth(), getHeight());
		
		g2d.drawString(perspective.getZoom() + "", 10, 10);
		g2d.drawString(perspective.getTopleft().x + ", " + perspective.getTopleft().y, 10, 20);
		g2d.drawString(fpsAverage.getAverage() + "", 10, 30);

		scene.paint(g2d);
	}

	private long lastTime = System.currentTimeMillis();
	
	private void computeFps() {
		final long now = System.currentTimeMillis();
		final long timeDiff = now - lastTime;
		lastTime = now;
		final float fps = 1000f / timeDiff;
		fpsAverage.addValue((float)timeDiff);
	}
	

}
