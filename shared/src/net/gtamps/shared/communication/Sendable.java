package net.gtamps.shared.communication;

import net.gtamps.shared.communication.data.ISendableData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Sendable {

	private static int idCounter = 0;
	
	public final int id;
    @NotNull
    public final SendableType type;
    @Nullable
    public ISendableData data;
    @Nullable
    public transient String sessionId; 

    public Sendable(@NotNull final SendableType type,@Nullable final ISendableData data) {
        this.type = type;
        this.data = data;
        this.id = createId();
    }

    public Sendable(SendableType type) {
        this(type, null);
    }
    
    private Sendable(final SendableType type, int id, final ISendableData data) {
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
    
    private int createId() {
    	return Sendable.idCounter++;
    }
}
