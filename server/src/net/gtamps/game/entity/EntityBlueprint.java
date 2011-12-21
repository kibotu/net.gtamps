package net.gtamps.game.entity;

import net.gtamps.game.handler.blueprints.HandlerBlueprint;
import net.gtamps.shared.game.entity.Entity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class EntityBlueprint {

	// STATIC

	private static final int EXPECTED_NUMBER_OF_HANDLERS = 10;
	private static final int EXPECTED_NUMBER_OF_PROTOTYPES = 10;
	private static final Map<String, EntityBlueprint> protoCache =
		new HashMap<String, EntityBlueprint>(EXPECTED_NUMBER_OF_PROTOTYPES);

	public static EntityBlueprint getCached(final String name) {
		final String realName = Entity.normalizeName(name);
		if (protoCache.containsKey(realName)) {
			return protoCache.get(realName);
		} else {
			final EntityBlueprint blup = new EntityBlueprint(realName);
			protoCache.put(blup.name, blup);
			return blup;
		}
	}

	public static EntityBlueprint getDisposableBlueprint(final String name) {
		final String realName = Entity.normalizeName(name);
		return new EntityBlueprint(realName);
	}


	// INSTANCE

	private final String name;
	private final Collection<HandlerBlueprint> handlerPrototypes;

	private EntityBlueprint(final String name) {
		this.name = Entity.normalizeName(name);
		handlerPrototypes = new ArrayList<HandlerBlueprint>(EXPECTED_NUMBER_OF_HANDLERS);

	}

	public String getName() {
		return this.name;
	}

	public Entity createEntity(final Integer pixX, final Integer pixY, final Integer deg) {
		final Entity entity = new Entity(this.name);
		for (final HandlerBlueprint hproto : handlerPrototypes) {
			assert hproto != null;
			entity.setHandler(hproto.createHandler(entity, pixX, pixY, deg));
		}
		return entity;
	}

	public void addHandlerPrototype(@NotNull final HandlerBlueprint proto) {
		assert proto != null;
		handlerPrototypes.add(proto.copy());
	}

	public void removeAllHandlerPrototypes() {
		handlerPrototypes.clear();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof EntityBlueprint)) {
			return false;
		}
		final EntityBlueprint other = (EntityBlueprint) obj;
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
