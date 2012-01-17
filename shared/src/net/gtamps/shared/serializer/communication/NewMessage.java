package net.gtamps.shared.serializer.communication;

import net.gtamps.shared.serializer.communication.data.ListNode;

import org.jetbrains.annotations.NotNull;

public class NewMessage extends AbstractSendable<NewMessage> {// extends
	// SharedObject
	// {

	private static final long serialVersionUID = 608050744473650094L;

	@NotNull
	private volatile String sessionId = null;

	// @CheckedShareable
	// public final ArrayList<Sendable> sendables;

	public ListNode<NewSendable> sendables = null;

	// public Message(@NotNull final Sendable sendable) {
	// this();
	// // sendables.add(sendable);
	// addSendable(sendable);
	// }

	public NewMessage() {
		super();
	}

	public NewMessage addSendable(final ListNode<NewSendable> sendable) {
		if (sendables == null) {
			sendables = sendable;
		} else {
			sendables.append(sendable);
		}
		return this;
	}

	public @NotNull
	String getSessionId() {
		return sessionId;
	}

	public void setSessionId(@NotNull final String sessionId) {
		this.sessionId = sessionId;
		if (sendables != null) {
			sendables.resetIterator();
			for (final NewSendable s : sendables) {
				s.sessionId = sessionId;
			}
		}
	}

	@Override
	public String toString() {
		return "Message{" + "sessionId='" + sessionId + '\'' + ", sendables=" + sendables + '}';
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sendables == null) ? 0 : sendables.hashCode());
		result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
		return result;
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
		final NewMessage other = (NewMessage) obj;
		if (sendables == null) {
			if (other.sendables != null) {
				return false;
			}
		} else if (!sendables.equals(other.sendables)) {
			return false;
		}
		if (sessionId == null) {
			if (other.sessionId != null) {
				return false;
			}
		} else if (!sessionId.equals(other.sessionId)) {
			return false;
		}
		return true;
	}

	@Override
	protected void initHook() {
	}

	@Override
	protected void recycleHook() {
		sendables.recycle();
		sessionId = null;
		sendables = null;
	}
}
