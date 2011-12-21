package net.gtamps.server.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

import net.gtamps.GTAMultiplayerServer;
import net.gtamps.preview.PreviewFrame;

/**
 * ugly-ass hacked class to easily control some server stuff...
 *
 * @author tom
 */
public class ServerGUI {

	private final JFrame frame;
	private final NetworkActivityIndicator networkSendActivity = new NetworkActivityIndicator(NetworkActivityIndicator.Type.SEND);
	private final NetworkActivityIndicator networkReceiveActivity = new NetworkActivityIndicator(NetworkActivityIndicator.Type.RECEIVE);
	private final Timer checkTimer;
	private PreviewFrame previewFrame;
	public ServerGUI() {
		frame = new JFrame("GTA MultiServer");

		//		JButton startServer = new JButton("start Server");
		//		startServer.addActionListener(new ActionListener() {
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		////				connectionManager.startNewServer(8090);
		//			}
		//		});
		//		JButton stopServer = new JButton("stop Server");
		//		stopServer.addActionListener(new ActionListener() {
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		////				connectionManager.stopAllServers();
		//			}
		//		});
		//
		//		JButton startHttpServer = new JButton("start Http Server");
		//		startHttpServer.addActionListener(new ActionListener() {
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		////				connectionManager.startNewHttpServer();
		//			}
		//		});
		//		JButton stopHttpServer = new JButton("stop Server");
		//		stopHttpServer.addActionListener(new ActionListener() {
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		////				connectionManager.stopHttpServer();
		//			}
		//		});



		final JButton previewGameButton = new JButton("Preview Game");
		if (GTAMultiplayerServer.DEBUG) {
			previewGameButton.setEnabled(false);
			previewGameButton.addActionListener(new ActionListener() {



				@Override
				public void actionPerformed(final ActionEvent e) {
					previewFrame = new PreviewFrame("GamePreview", 1024, 768, DebugGameBridge.instance.getAWorld());
				}
			});
		}

		final JButton restartGameButton = new JButton("Restart Game");
		restartGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				previewGameButton.setEnabled(false);
				if (previewFrame != null) {
					previewFrame.stop();
				}
				GTAMultiplayerServer.getControlCenter().restart();
				//            	System.err.println("Restart not implemented at the moment");
			}
		});



		checkTimer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				if (DebugGameBridge.instance.world != null) {
					previewGameButton.setEnabled(true);
				} else {
					previewGameButton.setEnabled(false);
					if (previewFrame != null) {
						previewFrame.stop();
					}
				}
			}

		});
		checkTimer.start();

		final GridLayout buttonPanel = new GridLayout(1, 0);
		final Container buttonContainer = new Container();
		buttonContainer.setLayout(buttonPanel);
		buttonContainer.add(networkSendActivity);
		buttonContainer.add(networkReceiveActivity);
		buttonContainer.add(previewGameButton);
		buttonContainer.add(restartGameButton);

		final Container container = new Container();
		container.setLayout(new BorderLayout());
		/*container.add(startServer);
          container.add(stopServer);
          container.add(startHttpServer);
          container.add(stopHttpServer);*/
		container.add(BorderLayout.CENTER, new TabbedPane());
		container.add(BorderLayout.SOUTH, buttonContainer);

		frame.setContentPane(container);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 500);
		frame.setVisible(true);
	}
}
