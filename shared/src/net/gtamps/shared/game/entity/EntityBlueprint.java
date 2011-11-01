package net.gtamps.shared.game.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.gtamps.shared.game.handler.HandlerBlueprint;

import org.jetbrains.annotations.NotNull;

public final class EntityBlueprint {
	
	// STATIC
	
	private static final int EXPECTED_NUMBER_OF_HANDLERS = 10;
	private static final int EXPECTED_NUMBER_OF_PROTOTYPES = 10;
	private static final Map<String, EntityBlueprint> protoCache = 
		new HashMap<String, EntityBlueprint>(EXPECTED_NUMBER_OF_PROTOTYPES);
	
	public static EntityBlueprint get(String name) {
		String realName = Entity.normalizeName(name);
		if (protoCache.containsKey(realName)) {
			return protoCache.get(realName);
		} else {
			return new EntityBlueprint(name);
		}
	}
	

	// INSTANCE
	
	private final String name;
	private final Collection<HandlerBlueprint> handlerPrototypes;
	
	private EntityBlueprint(String name)  {
		this.name = Entity.normalizeName(name);
		handlerPrototypes = new ArrayList<HandlerBlueprint>(EXPECTED_NUMBER_OF_HANDLERS);
		protoCache.put(this.name, this);
	}
	
	public String getName() {
		return this.name;
	}
	
	public Entity createEntity(Integer pixX, Integer pixY, Integer deg) {
		Entity entity = new Entity(this.name);
		for (HandlerBlueprint hproto : handlerPrototypes) {
			assert hproto != null;
			entity.setHandler(hproto.createHandler(entity, pixX, pixY, deg));
		}
		return entity;
	}

	public void addHandlerPrototype(@NotNull HandlerBlueprint proto) {
		assert proto != null;
		handlerPrototypes.add(proto.copy());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof EntityBlueprint)) {
			return false;
		}
		EntityBlueprint other = (EntityBlueprint) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
	
}
