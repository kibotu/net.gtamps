package net.gtamps.shared.serializer.communication;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

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
		String fullName = c.getCanonicalName(); 
		if (!fullName.startsWith("java.")) {
			try {
				c.getField(SHAREABLE_FIELD_MARKER);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				return false;
			}
		}
		Field[] ownFields = c.getDeclaredFields();
		for (Field ownField: ownFields) {
			if (!isShareable(ownField.)) {
				return false;
			}
		}
		return true;
	}

}
