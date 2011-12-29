package net.gtamps.shared.serializer.communication.data;

import java.util.List;

import net.gtamps.shared.Utils.validate.Validate;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.IProperty;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.serializer.communication.SendableProvider;

public class SendableDataHelper {

	private static final String OBJECT_TYPE = "OBJECT_TYPE";
	private static final String OBJECT_DATA = "OBJECT_DATA";
	private static final String PROPERTY_VALUE = "PROPERTY_VALUE";
	private static final String PROPERTY_NAME = "PROPERTY_NAME";
	private static final String GAMEOBJECT_ID = "GAMEOBJECT_ID";
	private static final String GAMEOBJECT_NAME = "GAMEOBJECT_NAME";
	private static final String GAMEOBJECT_REVISION = "GAMEOBJECT_REVISION";
	private static final String GAMEOBJECT_PROPERTIES = "GAMEOBJECT_PROPERTIES";

	public static Entity toEntity(final AbstractSendableData<?> data) {
		Validate.notNull(data);
		final GameObject gob = toGameObject(data);
		return (Entity) gob;
	}

	private static GameObject toGameObject(final AbstractSendableData<?> data) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T extends GameObject> DataMap toSendableData(final T e, final SendableProvider provider) {
		Validate.notNull(e);
		Validate.notNull(provider);
		final DataMap outerMap = provider.getDataMap();
		final DataMap data = initSendableDataForGameObject(e, provider);

		final MapEntry<Value<String>> typeEntry = createTypeEntry(e, provider);
		final MapEntry<DataMap> dataEntry = provider.getMapEntry(OBJECT_DATA, data);
		outerMap.add(typeEntry);
		outerMap.add(dataEntry);

		return outerMap;
	}

	private static DataMap toSendableData(final IProperty<?> p, final SendableProvider provider) {
		final DataMap dataMap = provider.getDataMap();

		// name
		final Value<?> nameValue = provider.getValue(p.getName());
		final MapEntry<?> nameEntry = provider.getMapEntry(PROPERTY_NAME, nameValue);
		dataMap.add(nameEntry);

		// value
		final Value<?> valueValue = provider.getValue(p.value());
		final MapEntry<?> valueEntry = provider.getMapEntry(PROPERTY_VALUE, valueValue);
		dataMap.add(valueEntry);

		return dataMap;
	}


	public static <T extends GameObject> AbstractSendableData<?> toSendableData(final List<T> c, final SendableProvider provider) {
		Validate.notNull(c);
		Validate.notNull(provider);
		final ListNode<DataMap> list = new ListNode<DataMap>();
		final int size = c.size();
		for (int i = 0; i < size; i++) {
			final T element = c.get(i);
			final DataMap data = toSendableData(element, provider);
			final ListNode<DataMap> newNode = new ListNode<DataMap>(data);
			list.append(newNode);
		}
		return list.next() == null ? list : list.next();
	}

	private static DataMap initSendableDataForGameObject(final GameObject o, final SendableProvider provider) {
		final DataMap data = provider.getDataMap();


		// id
		final Value<?> idValue = provider.getValue(o.getUid());
		final MapEntry<?> idEntry = provider.getMapEntry(GAMEOBJECT_ID,idValue);
		data.add(idEntry);

		// name
		final Value<?> nameValue = provider.getValue(o.getName());
		final MapEntry<?> nameEntry = provider.getMapEntry(GAMEOBJECT_NAME,nameValue);
		data.add(nameEntry);

		// revision
		final Value<?> revValue = provider.getValue(o.getRevision());
		final MapEntry<?> revEntry = provider.getMapEntry(GAMEOBJECT_REVISION, revValue);
		data.add(revEntry);

		// properties
		final AbstractSendableData<?> propData = toSendableData(o.getAllProperties(), provider);
		final MapEntry<?> propEntry = provider.getMapEntry(GAMEOBJECT_PROPERTIES, propData);
		data.add(propEntry);

		return data;
	}

	private static AbstractSendableData<?> toSendableData(final Iterable<IProperty<?>> properties, final SendableProvider provider) {
		final ListNode<DataMap> list = new ListNode<DataMap>();
		for (final IProperty<?> p : properties) {
			final DataMap data = toSendableData(p, provider);
			final ListNode<DataMap> newNode = new ListNode<DataMap>(data);
			list.append(newNode);
		}
		return list.next() == null ? list : list.next();
	}

	private static <T extends GameObject> MapEntry<Value<String>> createTypeEntry(final T o, final SendableProvider provider) {
		final Class<?> type = o.getClass();
		final Value<String> typeValue = provider.getValue(type.getSimpleName());
		final MapEntry<Value<String>> entry = provider.getMapEntry(OBJECT_TYPE, typeValue);
		return entry;
	}

}
