package android.normalizer;

import java.awt.Container;
import java.awt.Frame;

import javax.swing.JApplet;

import android.normalizer.controller.Controller;


public class Bootstrap extends JApplet {

	private static final long serialVersionUID = 4895015725301923346L;

	public Bootstrap() {
		super();
	}
	
	@Override
	public void init() {
		
		final Container context = getContentPane();
		context.add((new Controller(this)).getView());
		setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
		setTitle(Config.APP_NAME);
		setVisible(true);
	}

	protected Frame getFrame() {
		for (Container p = getParent(); p != null; p = p.getParent()) {
			if (p instanceof Frame) {
				return (Frame) p;
			}
		}
		return null;
	}

	protected void setTitle(String title) {
		getFrame().setTitle(title);
	}
}