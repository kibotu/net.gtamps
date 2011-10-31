package gtamapedit.view.propertyPane.components;

import gtamapedit.conf.Configuration;
import gtamapedit.view.ControlType;
import gtamapedit.view.MapEditorComponent;

import javax.swing.JSlider;

public class FloorSlider extends JSlider{
	public FloorSlider(MapEditorComponent mec){
		super(JSlider.VERTICAL,0,Configuration.getMaxFloors(),0);
		setMajorTickSpacing(10);
		setMinorTickSpacing(1);
		setPaintTicks(true);
		setPaintLabels(true);
		this.addChangeListener(mec);
		
	}
}
