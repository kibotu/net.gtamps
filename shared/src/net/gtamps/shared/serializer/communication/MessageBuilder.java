package net.gtamps.shared.serializer.communication;

import net.gtamps.shared.Utils.validate.Validate;
import net.gtamps.shared.serializer.communication.data.ListNode;

public class MessageBuilder {

	SendableFactory sendableFactory;
	NewMessage msg;

	MessageBuilder(final SendableFactory sendableFactory) {
		Validate.notNull(sendableFactory);
		this.sendableFactory = sendableFactory;
		this.msg = sendableFactory.createMessage();
	}

	public MessageBuilder addSendable(final NewSendable s) throws IllegalStateException {
		validateNotYetBuilt();
		final ListNode<NewSendable> node = sendableFactory.wrapSendable(s);
		msg.addSendable(node);
		return this;
	}

	public NewMessage build() {
		final NewMessage tmp = msg;
		msg = null;
		return tmp;
	}

	private void validateNotYetBuilt() throws IllegalStateException {
		if (msg == null) {
			throw new IllegalStateException("message was already built!");
		}
	}
}