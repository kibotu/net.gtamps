package net.gtamps.game.physics;


@Deprecated // Use GameEvent instead 
public class PhysicsAction {
	
	public static enum Type{
		ACCELERATE, DECELERATE, LEFT, RIGHT
	}
	
	public final Type type;
	//public final Entity entity;
	
	public PhysicsAction(Type type){
		this.type = type;
	}
}
