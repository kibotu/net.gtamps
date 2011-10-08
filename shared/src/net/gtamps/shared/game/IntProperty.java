package net.gtamps.shared.game;

public class IntProperty extends Propertay<Integer> {

	public IntProperty(GameObject parent, String name, Integer value) {
		super(parent, name, value);
	}

	public IntProperty(GameObject parent, String name) {
		this(parent, name, 0);
	}

}
