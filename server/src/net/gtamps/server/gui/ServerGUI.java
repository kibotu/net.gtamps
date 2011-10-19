package net.gtamps.server.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import net.gtamps.server.ControlCenter;

/**
 * ugly-ass hacked class to easily control some server stuff...
 * @author tom
 *
 */
public class ServerGUI {
	private JFrame frame;
	private NetworkActivityIndicator networkSendActivity = new NetworkActivityIndicator(NetworkActivityIndicator.Type.SEND);
	private NetworkActivityIndicator networkReceiveActivity = new NetworkActivityIndicator(NetworkActivityIndicator.Type.RECEIVE);
	
	public ServerGUI(){
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
		
		JButton restartGameButton = new JButton("Restart Game");		
		restartGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ControlCenter.instance.restart();
			}
		});
		
		
		

		GridLayout buttonPanel = new GridLayout(1, 0);
		Container buttonContainer = new Container();
		buttonContainer.setLayout(buttonPanel);
		buttonContainer.add(networkSendActivity);
		buttonContainer.add(networkReceiveActivity);
		buttonContainer.add(restartGameButton);
		
		Container container = new Container();
		container.setLayout(new BorderLayout());
		/*container.add(startServer);
		container.add(stopServer);
		container.add(startHttpServer);
		container.add(stopHttpServer);*/
		container.add(BorderLayout.CENTER ,new TabbedPane());
		container.add(BorderLayout.SOUTH, buttonContainer);
		
		frame.setContentPane(container);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setVisible(true);
	}
}
