package gtamapedit.view.propertyPane;

import gtamapedit.view.ControlType;
import gtamapedit.view.MapEditorComponent;
import gtamapedit.view.propertyPane.entityPanel.RotationSelector;

import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import net.gtamps.shared.game.entity.Entity.Type;


public class EntityPropertyPanel extends JPanel{
	JComboBox<String> entityTypeList;
	JButton entityDelete = new JButton("delete selected entity");
	RotationSelector rotationalSelector = new RotationSelector();

	public EntityPropertyPanel(MapEditorComponent mec) {
		this.setLayout(new FlowLayout());

		rotationalSelector.addPropertyChangeListener(mec);
		
		entityDelete.addActionListener(mec);
		entityDelete.setActionCommand(ControlType.ENTITY_DELETE_SELECTION);
		this.add(entityDelete);
		String[] entityTypes = new String[Type.values().length];
		int i = 0;
		for (Type t : Type.values()) {
			entityTypes[i] = t.name();
			i++;
		}
		entityTypeList = new JComboBox<String>(entityTypes);
		entityTypeList.addActionListener(mec);
		entityTypeList.setActionCommand(ControlType.ENTITY_TYPE_CHANGE);
		this.add(entityTypeList);
		
		this.add(rotationalSelector);
	}

}
