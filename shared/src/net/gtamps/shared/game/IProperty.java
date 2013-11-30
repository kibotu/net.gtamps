package net.gtamps.shared.game;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

/**
 * An abstract wrapper for {@link Propertay}, to allow proxyfication through
 * {@link ProxyProperty}.
 *
 * @param <T> it is highly recommended that this type properly
 *            implement {@link Object#toString() toString()},
 *            {@link Object#hashCode() hashCode()} and {@link Object#equals(Object) equals()}
 * @author til, jan, tom
 */
public interface IProperty<T> extends Serializable {

	@NotNull
	public String getName();

	@NotNull
	public T value();

	public boolean set(T value);

	@NotNull
	public String getAsString();

}
