package gtamapedit.view.propertyPane;

import gtamapedit.view.ControlType;
import gtamapedit.view.MapEditorComponent;
import gtamapedit.view.MapEditorComponent.Modus;
import gtamapedit.view.propertyPane.tilePanel.FloorSlider;
import gtamapedit.view.propertyPane.tilePanel.SideTextureSelectors;
import gtamapedit.view.propertyPane.tilePanel.TextureRotation;
import gtamapedit.view.propertyPane.tilePanel.TextureSelector;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;

import net.gtamps.shared.game.entity.Entity.Type;

public class PropertyPane extends JComponent {

	MapEditorComponent mec;

	TilePropertyPanel tilePropertyPanel;

	EntityPropertyPanel entityPropertyPanel;

	JButton modeSwitcher = new JButton("Switch to Entity mode");

	public PropertyPane(MapEditorComponent mec) {
		entityPropertyPanel = new EntityPropertyPanel(mec);
		tilePropertyPanel = new TilePropertyPanel(mec);

		modeSwitcher.setActionCommand(ControlType.SWITCH_MODE);
		modeSwitcher.addActionListener(mec);

		this.setLayout(new BorderLayout());
		this.add(modeSwitcher, BorderLayout.WEST);

	}

	public JSlider getFloorSlider() {
		return tilePropertyPanel.getFloorSlider();
	}

	public TextureSelector getTopTextureSelector() {
		return tilePropertyPanel.getTopTexture();
	}

	public TextureSelector getNorthTextureSelector() {
		return tilePropertyPanel.getSideTexture().getNorthTexture();
	}

	public TextureSelector getSouthTextureSelector() {
		return tilePropertyPanel.getSideTexture().getSouthTexture();
	}

	public TextureSelector getEastTextureSelector() {
		return tilePropertyPanel.getSideTexture().getEastTexture();
	}

	public TextureSelector getWestTextureSelector() {
		return tilePropertyPanel.getSideTexture().getWestTexture();
	}

	public TextureRotation getTextureRotation() {
		return tilePropertyPanel.getTextureRotation();
	}

	public void setMode(Modus mode) {
		if (mode == Modus.ENTITY_MODE) {
			this.remove(tilePropertyPanel);
			this.add(entityPropertyPanel, BorderLayout.CENTER);
		}
		if (mode == Modus.TILE_MODE) {
			this.add(tilePropertyPanel, BorderLayout.CENTER);
			this.remove(entityPropertyPanel);
		}
	}

}
