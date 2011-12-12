package gtamapedit.view.propertyPane.components;

import java.awt.Dimension;
import java.awt.GridLayout;

import gtamapedit.view.ControlType;
import gtamapedit.view.MapEditorComponent;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JRadioButton;

public class TextureRotation extends JComponent{
	JRadioButton deg0;
	JRadioButton deg90;
	JRadioButton deg180;
	JRadioButton deg270;
	
	public TextureRotation(MapEditorComponent mec){
		deg0 = new JRadioButton("0°");
		deg0.setActionCommand(ControlType.TEXTURE_ROTATION_0);
		deg0.addActionListener(mec);
	    deg90 = new JRadioButton("90°");
	    deg90.setActionCommand(ControlType.TEXTURE_ROTATION_90);
	    deg90.addActionListener(mec);
	    deg180 = new JRadioButton("180°");
	    deg180.setActionCommand(ControlType.TEXTURE_ROTATION_180);
	    deg180.addActionListener(mec);
	    deg270 = new JRadioButton("270°");
	    deg270.setActionCommand(ControlType.TEXTURE_ROTATION_270);
	    deg270.addActionListener(mec);
	    ButtonGroup group = new ButtonGroup();
	    group.add(deg0);
	    group.add(deg90);
	    group.add(deg180);
	    group.add(deg270);
	    this.setLayout(new GridLayout(0,1));
	    this.add(deg0);
	    this.add(deg90);
	    this.add(deg180);
	    this.add(deg270);
	  	}
	public void setRotation(int rot){
		if(rot==90){
			deg90.doClick();
		}
		if(rot==0){
			deg0.doClick();
		}
		if(rot==180){
			deg180.doClick();
		}
		if(rot==270){
			deg270.doClick();
		}
	}
}
