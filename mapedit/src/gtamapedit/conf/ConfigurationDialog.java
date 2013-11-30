package gtamapedit.conf;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ConfigurationDialog extends JDialog implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 725318983133131981L;
	
	HashMap<String, JTextField> textFieldMap = new HashMap<String, JTextField>();
	public ConfigurationDialog(JFrame frame, boolean modal) {
		super(frame,modal);
		JPanel confPanel = new JPanel(new GridLayout(0,2));
		
		for(String title : Configuration.getConfigurations().keySet()){
			confPanel.add(new JLabel(title.replace("_", " ")));
			JTextField textfield = new JTextField(Configuration.getConfigurations().get(title).toString());
			textFieldMap.put(title, textfield);
			confPanel.add(textfield);
			System.out.println(title);
			
		}
		
		JButton saveButton = new JButton("ok!");
		saveButton.setActionCommand("save");
		saveButton.addActionListener(this);
		confPanel.add(saveButton);
		
		this.add(confPanel);
		this.setSize(800, 200);
		this.setLocationRelativeTo(frame);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equals("save")){
			for(String title : Configuration.getConfigurations().keySet()){
				JTextField textField = textFieldMap.get(title);
				if(textField!=null){
					Configuration.setConfig(title,textField.getText());
				} else {
					System.out.println(title+" field is null!");
				}
			}
		}
		dispose();
	}
	
}
