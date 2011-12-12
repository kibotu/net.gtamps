package net.gtamps.server.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * ugly-ass hacked class to easily control some server stuff...
 *
 * @author tom
 */
public class ServerGUI {
    private final JFrame frame;
    private final NetworkActivityIndicator networkSendActivity = new NetworkActivityIndicator(NetworkActivityIndicator.Type.SEND);
    private final NetworkActivityIndicator networkReceiveActivity = new NetworkActivityIndicator(NetworkActivityIndicator.Type.RECEIVE);

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

        final JButton restartGameButton = new JButton("Restart Game");
        restartGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                //GTAMultiplayerServer.restart();
            	System.err.println("Restart not implemented at the moment");
            }
        });


        final GridLayout buttonPanel = new GridLayout(1, 0);
        final Container buttonContainer = new Container();
        buttonContainer.setLayout(buttonPanel);
        buttonContainer.add(networkSendActivity);
        buttonContainer.add(networkReceiveActivity);
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
        frame.setSize(500, 500);
        frame.setVisible(true);
    }
}
