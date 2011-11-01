package gtamapedit.view.propertyPane.components;

import gtamapedit.tileManager.TileImageHolder;
import gtamapedit.view.MapEditorComponent;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class TextureSelector extends JComponent{
	JLabel texture = new JLabel("none");
	public TextureSelector(String title, String actioncommand ,MapEditorComponent mec){
		setLayout(new BorderLayout());
		
		JButton applyTexture = new JButton("set");
		
		add(new JLabel(title),BorderLayout.NORTH);
		add(texture,BorderLayout.CENTER);
		add(applyTexture, BorderLayout.SOUTH);
		applyTexture.addActionListener(mec);
		applyTexture.setActionCommand(actioncommand);
		
	}
	
	public void setTexture(TileImageHolder tileImageHolder){
		if(tileImageHolder == null){
			this.texture.setIcon(null);
			this.texture.setText("Mixed Textures");
		} else {
			ImageIcon icon = new ImageIcon(tileImageHolder.getTileImage());
			this.texture.setIcon(icon);
			this.texture.setSize(icon.getImage().getWidth(null), icon.getImage().getHeight(null));
			this.texture.setText(tileImageHolder.getFilename());
		}
	}
}
