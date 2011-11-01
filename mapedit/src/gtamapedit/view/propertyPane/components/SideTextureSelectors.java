package gtamapedit.view.propertyPane.components;

import gtamapedit.view.ControlType;
import gtamapedit.view.MapEditorComponent;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JComponent;


public class SideTextureSelectors extends JComponent
{
	TextureSelector northTexture;
	TextureSelector eastTexture;
	TextureSelector southTexture;
	TextureSelector westTexture;
	JCheckBox sameTexture;
	public SideTextureSelectors(MapEditorComponent mec){
		setLayout(new BorderLayout());
		northTexture = new TextureSelector("north texture",ControlType.NORTHTEXTURE,mec);
		eastTexture = new TextureSelector("east texture",ControlType.EASTTEXTURE,mec);
		southTexture = new TextureSelector("south texture",ControlType.SOUTHTEXTURE,mec);
		westTexture = new TextureSelector("west texture",ControlType.WESTTEXTURE,mec);
		sameTexture = new JCheckBox("same");
		sameTexture.addActionListener(mec);
		add(sameTexture,BorderLayout.CENTER);
		add(northTexture,BorderLayout.NORTH);
		add(eastTexture,BorderLayout.EAST);
		add(southTexture,BorderLayout.SOUTH);
		add(westTexture,BorderLayout.WEST);
		
	}
	
	public TextureSelector getNorthTexture() {
		return northTexture;
	}
	public TextureSelector getEastTexture() {
		return eastTexture;
	}
	public TextureSelector getSouthTexture() {
		return southTexture;
	}
	public TextureSelector getWestTexture() {
		return westTexture;
	}
	public JCheckBox getSameTexture() {
		return sameTexture;
	}
}
