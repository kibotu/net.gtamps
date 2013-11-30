package net.gtamps.server.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NetworkActivityIndicator extends JRadioButton implements ActionListener {

    private Timer timer;
    private Type type;

    public static enum Type {
        SEND, RECEIVE
    }

    public NetworkActivityIndicator(Type type) {
        super();
        this.type = type;
        this.setLabel(this.type.toString());
        timer = new Timer(300, this);
        timer.setInitialDelay(3000);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setEnabled(false);
        if (this.type == Type.SEND) {
            this.setSelected(GUILogger.i().getNetworkSendActivity());
        }
        if (this.type == Type.RECEIVE) {
            this.setSelected(GUILogger.i().getNetworkReceiveActivity());
        }
    }

}
