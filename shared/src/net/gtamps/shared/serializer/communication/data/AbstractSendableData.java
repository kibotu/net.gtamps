package net.gtamps.shared.serializer.communication.data;

import net.gtamps.shared.serializer.communication.AbstractSendable;

public abstract class AbstractSendableData<DataType extends AbstractSendableData<DataType>> extends AbstractSendable<DataType> {

	private static final long serialVersionUID = 7512510685123238578L;

	AbstractSendableData() {
		super();
	}

	public DataMap asMap() {
        Object me = this;
        if(!(me instanceof DataMap)) throw new ClassCastException(this.getClass().getSimpleName() + " is not enough DataMap.");
        return (DataMap) me;
//        return (DataMap) this;
	}

	public <T extends AbstractSendable<T>> ListNode<T> asList() {
        Object me = this;
        if(!(me instanceof ListNode)) throw new ClassCastException(this.getClass().getSimpleName() + " is not enough (ListNode<T>).");
		return (ListNode<T>) me;
        //return (ListNode<T>) this;
	}

	public <T> Value<T> asValue() {
        Object me = this;
        if(!(me instanceof Value)) throw new ClassCastException(this.getClass().getSimpleName() + " is not enough (Value<T>).");
		return (Value<T>) me;
//        return (Value<T>) this;
	}

}
