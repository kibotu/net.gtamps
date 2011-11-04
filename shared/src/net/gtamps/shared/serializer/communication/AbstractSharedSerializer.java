package net.gtamps.shared.serializer.communication;

//TODO not needed anymore, as soon as ISerializer takes SharedObjects as arguments
@Deprecated
public abstract class AbstractSharedSerializer implements ISerializer {
	
	private static final String SHAREABLE_FIELD_MARKER = "_4e4b219008d44271";
	
	protected abstract byte[] serializeMessageOverride(Message m);

	@Override
	public final byte[] serializeMessage(Message m) {
		if (m == null) {
			throw new IllegalArgumentException("'m' must not be null");
		}
		assert isShareable(m.getClass()) : getAssertionErrorMessage(m);
		return serializeMessageOverride(m);
	}
	
	private String getAssertionErrorMessage(Object o) {
		return String.format("object is not shared: %s %s", 
				o.getClass().getCanonicalName(), o.toString());
	}
	
	private boolean isShareable(Object o) {
		return true;
	}

}
