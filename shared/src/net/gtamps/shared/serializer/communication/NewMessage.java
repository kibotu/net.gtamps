package net.gtamps.shared.serializer.communication;

import net.gtamps.shared.serializer.communication.data.ListNode;

import org.jetbrains.annotations.NotNull;

public class NewMessage extends AbstractSendable<NewMessage> {//extends SharedObject {

	private static final long serialVersionUID = 608050744473650094L;

	@NotNull
	private volatile String sessionId = null;

	//	@CheckedShareable
	//	public final ArrayList<Sendable> sendables;

	public ListNode<NewSendable> sendables = null;

	//	public Message(@NotNull final Sendable sendable) {
	//		this();
	//		//		sendables.add(sendable);
	//		addSendable(sendable);
	//	}

	public NewMessage() {
		super(NewMessage.class);
	}

	public NewMessage addSendable(final ListNode<NewSendable> sendable) {
		if (sendables == null) {
			sendables = sendable;
		} else {
			sendables.append(sendable);
		}
		return this;
	}

	public
	@NotNull
	String getSessionId() {
		return sessionId;
	}

	public void setSessionId(@NotNull final String sessionId) {
		this.sessionId = sessionId;
		for (final NewSendable s : sendables) {
			s.sessionId = sessionId;
		}
	}

	@Override
	public String toString() {
		return "Message{" +
				"sessionId='" + sessionId + '\'' +
				", sendables=" + sendables +
				'}';
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
	}

	@Override
	protected void recycleHook() {
		this.sendables.recycle();
		this.sessionId = null;
		this.sendables = null;
	}
}
