package net.gtamps.shared.serializer.communication.data;

import net.gtamps.shared.SharedObject;

/**
 * @deprecated use {@link AbstractSendableData} instead
 */
@Deprecated
public class FloatData extends SharedObject implements ISendableData {

	private static final long serialVersionUID = -6219103367607433401L;

	public final float value;

	public FloatData(final float value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "FloatData{" +
				"value=" + value +
				'}';
	}
}
