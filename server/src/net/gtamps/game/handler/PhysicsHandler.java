package net.gtamps.game.handler;

import net.gtamps.game.conf.PhysicalProperties;
import net.gtamps.game.physics.PhysicsFactory;
import net.gtamps.game.property.PositionProperty;
import net.gtamps.game.property.Property;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class PhysicsHandler extends SimplePhysicsHandler{
	
	public PhysicsHandler(Entity parent, Body physicalRepresentation, PhysicalProperties physicalProperties) {
		super(parent, physicalRepresentation, physicalProperties);
	}
	
	@Override
	public void enable() {
		int pixX = parent.x.value();
		int pixY = parent.y.value();
		int rota = parent.rota.value();
		this.body = null;
		while(body == null) {
			this.body = PhysicsFactory.createHuman(world, physicalProperties, pixX, pixY, rota);
		}
		super.enable();
	}
	
	@Override
	public void update() {
		
		if (!this.isEnabled()) {
			if (this.body != null) {
				world.destroyBody(this.body);
				this.body = null;
			}
			return;
		}
		
		// put all player inputs inside the physics engine
		Vec2 front = new Vec2((float) Math.cos(this.body.getAngle()),(float) Math.sin(this.body.getAngle()));
		Vec2 worldCenter = body.getWorldCenter();
		
		float forward = Math.signum(Vec2.dot(front,body.getLinearVelocity()));
		
		while (!actionQueue.isEmpty()) {
			EventType pa = actionQueue.poll().getType();
			// int uid = parent.getUid();

			
			if(physicalProperties.TYPE == PhysicalProperties.Type.CAR){
				if (pa == EventType.ACTION_ACCELERATE) {
					Vec2 force = new Vec2((float) Math.cos(this.body.getAngle())*velocityForce, (float) Math.sin(this.body.getAngle())*velocityForce);
					this.body.applyForce(force, this.body.getWorldCenter());
				}
				if (pa == EventType.ACTION_DECELERATE) {
					Vec2 force = new Vec2((float) -Math.cos(this.body.getAngle())*velocityForce, (float) -Math.sin(this.body.getAngle())*velocityForce);
					this.body.applyForce(force, this.body.getWorldCenter());
				}
				if (pa == EventType.ACTION_TURNRIGHT) {
					Vec2 force = new Vec2((float) Math.cos(this.body.getAngle()+Math.PI/2f), (float) Math.sin(this.body.getAngle()+Math.PI/2f));
					this.body.applyForce(force.mul(steeringForce*forward), worldCenter.add(front.mul(steeringRadius)));
				}
				if (pa == EventType.ACTION_TURNLEFT) {
					Vec2 force = new Vec2((float) Math.cos(this.body.getAngle()-Math.PI/2f), (float) Math.sin(this.body.getAngle()-Math.PI/2f));
					this.body.applyForce(force.mul(steeringForce*forward), worldCenter.add(front.mul(steeringRadius)));
				}
			}
			if(physicalProperties.TYPE == PhysicalProperties.Type.HUMAN){
				if (pa == EventType.ACTION_ACCELERATE) {
					Vec2 force = new Vec2((float) Math.cos(this.body.getAngle())*velocityForce, (float) Math.sin(this.body.getAngle())*velocityForce);
					this.body.setLinearVelocity(force);
					this.body.wakeUp();
				}
				if (pa == EventType.ACTION_DECELERATE) {
					Vec2 force = new Vec2((float) -Math.cos(this.body.getAngle())*velocityForce, (float) -Math.sin(this.body.getAngle())*velocityForce);
					this.body.setLinearVelocity(force);
					this.body.wakeUp();
				}
				if (pa == EventType.ACTION_TURNRIGHT) {
					this.body.wakeUp();
					this.body.setAngularVelocity(physicalProperties.STEERING_FORCE);
				}
				if (pa == EventType.ACTION_TURNLEFT) {
					this.body.wakeUp();
					this.body.setAngularVelocity(-physicalProperties.STEERING_FORCE);
				}
			}
		}
		
		
		if(physicalProperties.TYPE == PhysicalProperties.Type.CAR){
//			Apply orthogonal friction, so that all the cars appear to run on "tracks"
//			deteremines whether the current velocity is directed to the front of the vehicle.
			float speed = body.getLinearVelocity().length();
//			front is normalized: set it to the length of the current speed:
			Vec2 frontvectorvelocity = front.mul(speed*forward);
//			mix the current velocity vector with the front vector, according to the slidyness 
			body.setLinearVelocity(body.getLinearVelocity().mul(slidyness).add(frontvectorvelocity.mul(1f-slidyness)));
		}
		
		if(physicalProperties.TYPE == PhysicalProperties.Type.HUMAN){
			this.body.setAngularVelocity(this.body.getAngularVelocity()*0.9f);
			this.body.setLinearVelocity(this.body.getLinearVelocity().mul(0.9f));
		}
		if(physicalProperties.TYPE == PhysicalProperties.Type.CAR){
			this.body.setAngularVelocity(this.body.getAngularVelocity()*0.93f);
			this.body.setLinearVelocity(this.body.getLinearVelocity().mul(0.96f));
		}
		
		super.update();

	}	
}
