package net.gtamps.preview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import net.gtamps.preview.view.BodyView;
import net.gtamps.preview.view.PreviewPerspective;
import net.gtamps.preview.view.Scene;
import net.gtamps.shared.Utils.MovingFloatAverage;

public class PreviewPanel extends JPanel  {
	
	private static final Color DEFAULT_BACKGROUND = Color.BLACK;
	private static final Color DEFAULT_FOREGROUND = Color.WHITE;
	
	private final PreviewPerspective perspective;
	private final Scene scene;
	private final MovingFloatAverage paintRunAverage = new MovingFloatAverage(10);
	private AutoUpdate updateThread;
	
	public PreviewPanel(final PreviewPerspective perspective) {
		if (perspective == null) {
			throw new IllegalArgumentException("'perspective' must not be 'null'");
		}
		this.perspective = perspective;
		scene = new Scene(perspective);
		
        addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(final MouseWheelEvent e) {
				final int amount = e.getWheelRotation();
				final Point loc = e.getPoint();
				perspective.zoomIn(loc, amount);
				updateContent();
			}
		});
        
        addMouseListener(new MouseAdapter() {
			
        	Point clickPoint;
        	
			@Override
			public void mouseReleased(final MouseEvent e) {
				final Point nowPoint = e.getPoint();
				final int dx = nowPoint.x - clickPoint.x;
				final int dy = nowPoint.y - clickPoint.y;
				perspective.move(dx, dy);
				updateContent();
			}
			
			@Override
			public void mousePressed(final MouseEvent e) {
				clickPoint = e.getPoint();
			}
		});
	}
	
	public void startAutoUpdate() {
		if (updateThread == null || !updateThread.isActive()) {
			updateThread = new AutoUpdate(this);
			updateThread.setTargetFps(25);
			updateThread.start();
		}
	}

	
	public void stopAutoUpdate() {
		if (updateThread != null && updateThread.isActive()) {
			updateThread.hardstop();
		}
	}
	
	public void addBody(final BodyView bodyView) {
		scene.addChild(bodyView);
	}
	
	public void updateContent() {
		scene.update();
		repaint();
	}
	
	@Override
	protected void paintComponent(final Graphics g) {
		updatePaintRuntime();
		//adjustUpdateRate();
		
		final Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(DEFAULT_FOREGROUND);
		g2d.setBackground(DEFAULT_BACKGROUND);
		g2d.clearRect(0, 0, getWidth(), getHeight());
		
		g2d.drawString(perspective.getZoom() + "", 10, 10);
		g2d.drawString(perspective.getTopleft().x + ", " + perspective.getTopleft().y, 10, 20);
		g2d.drawString(paintRunAverage.getAverage() + "", 10, 30);
		g2d.drawString(updateThread == null ? "" : updateThread.getTargetFps() + "", 10, 40);

		scene.paint(g2d);
	}

	private long lastTime = System.currentTimeMillis();
	
	private void updatePaintRuntime() {
		final long now = System.currentTimeMillis();
		final float diff = (now - lastTime) / 1000f;
		lastTime = now;
		paintRunAverage.addValue(diff);
	}

	private void adjustUpdateRate() {
		if (updateThread != null && updateThread.isActive()) {
			final float fps = (1f/paintRunAverage.getAverage());
			updateThread.setTargetFps(Math.min(fps, 25));
		}
	}
	
	

}
