package net.gtamps.shared.serializer.communication;

import net.gtamps.shared.serializer.communication.data.AbstractSendableData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NewSendable extends AbstractSendable<NewSendable> {//extends SharedObject implements ISendable {

	public static final int INVALID_ID = -1;

	private static final long serialVersionUID = 5034854192792203952L;

	private static int idCounter = 0;

	public int id = INVALID_ID;
	@Nullable
	public SendableType type = null;
	@Nullable
	public AbstractSendableData<?> data = null;
	@Nullable
	public transient String sessionId = null;

	public NewSendable() {
		super();
	}

	public NewSendable(final SendableType type) {
		this(type, null);
	}

	public NewSendable(@NotNull final SendableType type, @Nullable final AbstractSendableData<?> data) {
		this(type, createId(), data);
	}


	public NewSendable(final SendableType type, final int id, final AbstractSendableData<?> data) {
		super();
		this.type = type;
		this.data = data;
		this.id = id;
	}

	public NewSendable setType(final SendableType type) {
		this.type = type;
		return this;
	}

	public NewSendable setData(final AbstractSendableData<?> data) {
		this.data = data;
		return this;
	}

	public NewSendable createResponse(final SendableType type) {
		if (type == null) {
			return null;
		}
		final NewSendable response = new NewSendable(type, this.id, null);
		response.sessionId = this.sessionId;
		return response;
	}

	@Override
	public String toString() {
		return String.format("%s (%d) [%s]", type.toString(), id, data != null ? data.toString() : "");
	}

	private static int createId() {
		return NewSendable.idCounter++;
	}

	@Override
	public boolean equals(final Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initHook() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void recycleHook() {
		// TODO Auto-generated method stub

	}

	public NewSendable setId(final int id) {
		this.id = id;
		return this;
	}
}
