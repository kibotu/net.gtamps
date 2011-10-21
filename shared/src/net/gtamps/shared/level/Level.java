package net.gtamps.shared.level;

import java.io.Serializable;
import java.util.LinkedList;

public class Level implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3756877185706183170L;
	
	LinkedList<PhysicalShape> physicalShapes = new LinkedList<PhysicalShape>();
	LinkedList<EntityPosition> entityPositions = new LinkedList<EntityPosition>();
	private String OBJMap = "";

	public LinkedList<PhysicalShape> getPhysicalShapes() {
		return physicalShapes;
	}

	public LinkedList<EntityPosition> getEntityPositions() {
		return entityPositions;
	}

	public void set3DMap(String ObjFromMap) {
		this.OBJMap = ObjFromMap;
	}
}
