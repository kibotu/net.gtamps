package net.gtamps.preview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.gtamps.preview.view.PreviewPerspective;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;


@SuppressWarnings("serial")
public class PreviewFrame extends JFrame {

	private static final int DEFAULT_HEIGHT = 768;
	private static final int DEFAULT_WIDTH = 1024;
	private static final String DEFAULT_NAME = "Server Physics View";

	public static void main(final String[] args) {
		final PreviewFrame pframe = new PreviewFrame(DEFAULT_NAME, DEFAULT_WIDTH, DEFAULT_HEIGHT, initTestPhysics());
		pframe.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	PreviewPanel preview;
	JButton redrawButton;
	JButton startUpdateButton;
	JButton stopUpdateButton;
	
	
	public PreviewFrame(final String name, final int width, final int height, final World world) {
		super(name);

		final Dimension size = new Dimension (width > 0 ? width : DEFAULT_WIDTH, height > 0 ? height : DEFAULT_HEIGHT);
		final PreviewPerspective perspective = new PreviewPerspective(size);

		final Container content = new JPanel();
		content.setBackground(Color.lightGray);
		content.setLayout(new BorderLayout());
		
		
		final PhysicsAccessor physics = new PhysicsAccessor(world);
		preview = new PreviewPanel(perspective, physics);
		preview.setPreferredSize(size);

		preview.setBackground(Color.BLACK);
		content.add(preview, BorderLayout.CENTER);
        
		redrawButton = new JButton("Redraw");
		startUpdateButton = new JButton("Auto Update ON");
		stopUpdateButton = new JButton("AutoUpdate OFF");
		stopUpdateButton.setEnabled(false);

		redrawButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				preview.updateContent();
			}
		});
		
		startUpdateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				startUpdateButton.setEnabled(false);
				stopUpdateButton.setEnabled(true);
				redrawButton.setEnabled(false);
				preview.startAutoUpdate();
			}
		});
		
		stopUpdateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				startUpdateButton.setEnabled(true);
				stopUpdateButton.setEnabled(false);
				redrawButton.setEnabled(true);
				preview.stopAutoUpdate();
			}
		});
		
        final GridLayout buttonPanel = new GridLayout(1, 0);
        final Container buttonContainer = new Container();
        buttonContainer.setLayout(buttonPanel);
        buttonContainer.add(redrawButton);
        buttonContainer.add(startUpdateButton);
        buttonContainer.add(stopUpdateButton);
        
        content.add(buttonContainer, BorderLayout.SOUTH);

        getContentPane().add(content);
        
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(final WindowEvent we){
				setVisible(false);
				dispose();
			}
		});
		
	
        
        setSize(size);
		pack();
        setVisible(true);

	}
	
	
	
	public void stop() {
		preview.stopAutoUpdate();
		redrawButton.setEnabled(false);
		startUpdateButton.setEnabled(false);
		stopUpdateButton.setEnabled(false);
	}
	
	private static World initTestPhysics() {
        final Vec2 lowerVertex = new Vec2(0, 0);
        final Vec2 upperVertex = new Vec2(100, 100);
        final Vec2 gravity = new Vec2(0f, 0f);
        final AABB aabb = new AABB(lowerVertex, upperVertex);
        final World world = new World(aabb, gravity, true);
        
        final CircleDef circleDef = new CircleDef();
        circleDef.localPosition = new Vec2(1f, 1f);
        circleDef.radius = 10f;
        
        final PolygonDef polyDef = new PolygonDef();
        polyDef.setAsBox(3, 3, upperVertex.mul(0.1f), 0f);
//        polyDef.setAsBox(3, 3);
        
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position = new Vec2(10, 10);
        
        final Body body = world.createBody(bodyDef);
        body.createShape(circleDef);
        body.createShape(polyDef);
        body.setMassFromShapes();
        
        
        return world;
	}

}
