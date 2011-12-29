package net.gtamps.shared.serializer.communication.data;

import net.gtamps.shared.serializer.communication.AbstractSendable;

public abstract class AbstractSendableData<DataType extends AbstractSendableData<DataType>> extends AbstractSendable<DataType> implements ISendableData {

	private static final long serialVersionUID = 7512510685123238578L;

	AbstractSendableData(final Class<? extends AbstractSendableData<?>> type) {
		super(type);
	}

}