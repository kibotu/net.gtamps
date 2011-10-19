package android.normalizer;

import java.awt.Container;

import javax.swing.JFrame;

import android.normalizer.controller.Controller;

public class MainFrame {

	public static void main(String [] args) {
		final JFrame frame = new JFrame(Config.APP_NAME);
		final Container container = frame.getContentPane();
		container.add((new Controller(frame)).getView());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
}