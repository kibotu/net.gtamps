package gtamapedit.view.propertyPane.entityPanel;

import gtamapedit.view.ControlType;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

public class RotationSelector extends JPanel implements MouseListener{
	private double currentRotation = 0; 
	private final int radius = 30;
	private final int indicatorSize = 6; 
	
	public RotationSelector(){
		setPreferredSize(new Dimension(radius*2+indicatorSize,radius*2+indicatorSize));
		addMouseListener(this);
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawOval(indicatorSize/2, indicatorSize/2, radius*2, radius*2);
		g.fillRect((int)(Math.cos(currentRotation)*radius)+radius, (int)(Math.sin(currentRotation)*radius)+radius, indicatorSize, indicatorSize);
		int degrees = (int)(currentRotation/Math.PI*180);
		degrees = degrees<0 ? degrees+360 : degrees;
		g.drawString(degrees+"°", radius-5, radius+5);
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
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
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if((e.getX()-radius)!=0){
			double previousRotation = currentRotation; 
			currentRotation = Math.atan2((e.getY()-radius),(e.getX()-radius));
			firePropertyChange(ControlType.ENTITY_CHANGE_ROTATION, previousRotation, currentRotation);
		}
		repaint();
	}
	public void setRotationInDegrees(int degrees) {
		this.currentRotation = degrees*Math.PI/180;
	}
	public int getRotationInDegrees() {
		return (int) (currentRotation/Math.PI*180);
	}
}
