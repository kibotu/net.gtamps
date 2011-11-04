package net.gtamps.shared.game;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * An abstract wrapper for {@link Propertay}, to allow proxyfication through
 * {@link ProxyProperty}.
 *
 * @param <T> it is highly recommended that this type properly
 *            implement {@link Object#toString() toString()},
 *            {@link Object#hashCode() hashCode()} and {@link Object#equals() equals()}
 * @author til, jan, tom
 */
@SuppressWarnings("serial")
public abstract class AbstractPropertay<T> extends GameObject implements Serializable {

    protected transient final GameObject parent;

    public AbstractPropertay(@NotNull GameObject parent, @NotNull String name) {
        super(name.toLowerCase());
        this.parent = parent;
        this.parent.hasChanged = true;
    }

    public GameObject getParent() {
        return this.parent;
    }

    public abstract T value();

    public abstract String getAsString();

    public abstract boolean set(T value);

// don't
//	@Override
//	public <T> Propertay<T> useProperty(@NotNull String name, @NotNull T value) throws NoSuchElementException {
//		throw new UnsupportedOperationException("this type of gameObject does not support properties");
//	}

}
