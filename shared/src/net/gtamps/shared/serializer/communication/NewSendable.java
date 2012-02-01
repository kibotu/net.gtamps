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

	@Deprecated
	public NewSendable() {
	}

	public NewSendable(final SendableType type) {
		this(type, null);
	}

	public NewSendable(@NotNull final SendableType type, @Nullable final AbstractSendableData<?> data) {
		this(type, createId(), data);
	}


	public NewSendable(final SendableType type, final int id, final AbstractSendableData<?> data) {
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
		return String.format("%s (%d) [%s]", type != null ? type.toString() : "no type", id, data != null ? data.toString() : "");
	}

	private static int createId() {
		return NewSendable.idCounter++;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final NewSendable other = (NewSendable) obj;
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!data.equals(other.data)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (sessionId == null) {
			if (other.sessionId != null) {
				return false;
			}
		} else if (!sessionId.equals(other.sessionId)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + id;
		result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	protected void initHook() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void recycleHook() {
		if(data!=null){
			this.data.recycle();
			this.data = null;
		}
		this.id = INVALID_ID;
		this.sessionId = null;
		this.type = null;
	}

	public NewSendable setId(final int id) {
		this.id = id;
		return this;
	}
}
