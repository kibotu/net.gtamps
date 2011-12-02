package net.gtamps.shared.serializer.communication;

import net.gtamps.shared.SharedObject;
import net.gtamps.shared.serializer.communication.data.ISendableData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Sendable extends SharedObject implements ISendable {

    private static final long serialVersionUID = 5034854192792203952L;

    private static int idCounter = 0;

    public final int id;
    @NotNull
    public final SendableType type;
    @Nullable
    public ISendableData data;
    @Nullable
    public transient String sessionId;

    public Sendable(@NotNull final SendableType type, @Nullable final ISendableData data) {
        this.type = type;
        this.data = data;
        this.id = createId();
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sendable other = (Sendable) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (id != other.id)
			return false;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public Sendable(SendableType type) {
        this(type, null);
    }

    public Sendable(final SendableType type, int id, final ISendableData data) {
        this.type = type;
        this.data = data;
        this.id = id;
    }

    public Sendable createResponse(SendableType type) {
        if (type == null) {
            return null;
        }
        Sendable response = new Sendable(type, this.id, null);
        response.sessionId = this.sessionId;
        return response;
    }

    @Override
    public String toString() {
        return String.format("%s (%d) [%s]", type.toString(), id, data != null ? data.toString() : "");
    }

    private int createId() {
        return Sendable.idCounter++;
    }
}
