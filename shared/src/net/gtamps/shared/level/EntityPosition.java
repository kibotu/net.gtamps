package net.gtamps.shared.level;

import net.gtamps.shared.math.Vector3;

public class EntityPosition{
	enum Type{
		SPAWNPOINT, REDLIGHT_0, REDLIGHT_1, SPORTSCAR, TAXI
	}
	Vector3 position;
	Type type;
	float rotation;
	
	public EntityPosition(float x, float y, float z, float rotation, Type type){
		this.position = Vector3.createNew(x,y,z);
		this.rotation = rotation;
		this.type = type;
	}
}
