package net.gtamps.shared.game.handler;


import net.gtamps.shared.game.IGameActor;
import net.gtamps.shared.game.Propertay;
import net.gtamps.shared.game.SharedGameActor;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.IGameEventListener;

/**
 * Handlers handle stuff for {@link Entity entities}, mainly events of a certain
 * type. They make entities modular in that way, behavior-wise and whatnot.
 * Handlers can make use of of an entity's {@link Propertay properties}.
 *
 * @author jan, tom, til
 */
public class Handler implements IGameActor {

    public enum Type {
        DRIVER, SENSOR, MOBILITY, PHYSICS, SHOOTING, HEALTH
    }

    protected final Type type;
    protected final Entity parent;
    protected final IGameActor actor;
    

    public Handler(Type type, Entity parent) {
        if (parent == null) {
            throw new IllegalArgumentException("'parent' must not be null");
        }
        this.actor = new SharedGameActor(type.name().toLowerCase()); 
        this.parent = parent;
        this.type = type;
    }
    
    @Override
    public String getName() {
    	return actor.getName();
    }

    public void addEventListener(EventType type, IGameEventListener listener) {
		actor.addEventListener(type, listener);
	}


	public void receiveEvent(GameEvent event) {
		actor.receiveEvent(event);
	}


	public boolean isEnabled() {
		return actor.isEnabled();
	}


	public void enable() {
		actor.enable();
	}


	public void disable() {
		actor.disable();
	}


	public void removeEventListener(EventType type, IGameEventListener listener) {
		actor.removeEventListener(type, listener);
	}


	public void connectUpwardsActor(SharedGameActor other) {
		actor.connectUpwardsActor(other);
	}


	public void dispatchEvent(GameEvent event) {
		actor.dispatchEvent(event);
	}


	public void connectDownwardsActor(SharedGameActor actor) {
		actor.connectDownwardsActor(actor);
	}


	public void disconnectUpwardsActor(SharedGameActor other) {
		actor.disconnectUpwardsActor(other);
	}


	public void disconnectDownwardsActor(SharedGameActor actor) {
		actor.disconnectDownwardsActor(actor);
	}


	public void setSendsUp(EventType[] sendsUp) {
		actor.setSendsUp(sendsUp);
	}


	public void setReceivesDown(EventType[] receivesDown) {
		actor.setReceivesDown(receivesDown);
	}


	public Entity getParent() {
        return this.parent;
    }


    @Override
    public String toString() {
        String s = "";
        s = String.format("%s (%s)", this.getName(), this.isEnabled() ? "on" : "off");
        return s;
    }

}
