package net.gtamps.shared.serializer.communication.data;

import java.util.List;

import net.gtamps.shared.Utils.validate.Validate;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.IProperty;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.serializer.communication.SendableProvider;

public class SendableDataConverter {

	private static final String PROPERTY_VALUE = "PROPERTY_VALUE";
	private static final String PROPERTY_NAME = "PROPERTY_NAME";
	private static final String GAMEOBJECT_ID = "GAMEOBJECT_ID";
	private static final String GAMEOBJECT_NAME = "GAMEOBJECT_NAME";
	private static final String GAMEOBJECT_REVISION = "GAMEOBJECT_REVISION";
	private static final String GAMEOBJECT_PROPERTIES = "GAMEOBJECT_PROPERTIES";
	private static final String GAMEOBJECT_TYPE = "GAMEOBJECT_TYPE";

	public static <T extends GameObject> DataMap toSendableData(final T e, final SendableProvider provider) {
		Validate.notNull(e);
		Validate.notNull(provider);
		final DataMap data = initSendableDataForGameObject(e, provider);

		final MapEntry<Value<String>> typeEntry = createTypeEntry(e, provider);
		data.add(typeEntry);

		return data;
	}

	public static <T extends GameObject> ListNode<DataMap> toSendableData(final List<T> c, final SendableProvider provider) {
		Validate.notNull(c);
		Validate.notNull(provider);
		ListNode<DataMap> list = ListNode.emptyList();
		final int size = c.size();
		for (int i = 0; i < size; i++) {
			final T element = c.get(i);
			if (element == null) {
				//TODO warn? exception?
				continue;
			}
			final DataMap data = toSendableData(element, provider);
			final ListNode<DataMap> newNode = provider.getListNode(data);
			list = list.append(newNode);
		}
		return list;
	}

	public static <T extends GameObject> void updateGameobject(final T gob, final DataMap updateData) {
		Validate.notNull(gob);
		Validate.notNull(updateData);
		validateMatches(gob, updateData);

		updateProperties(gob, updateData);
		gob.setChanged();
	}

	public static Entity toEntity(final DataMap objectdata) {
		final String name = getGameObjectName(objectdata);
		final int uid = getGameObjectUid(objectdata);
		final Entity e = new Entity(name, uid);
		updateGameobject(e, objectdata);
		return e;
	}

	public static int getGameObjectUid(final DataMap map) {
		final Value<Integer> idValue = (Value<Integer>) map.get(GAMEOBJECT_ID);
		return idValue.get();
	}

	private static <T extends GameObject> void putGameObjectUid(final T gob, final DataMap map, final SendableProvider provider) {
		final Value<Integer> idValue = provider.getValue(gob.getUid());
		final MapEntry<Value<Integer>> entry = provider.getMapEntry(GAMEOBJECT_ID, idValue);
		map.add(entry);
	}

	private static <T extends GameObject> void putGameObjectName(final T gob, final DataMap map, final SendableProvider provider) {
		final Value<String> value = provider.getValue(gob.getName());
		final MapEntry<Value<String>> entry = provider.getMapEntry(GAMEOBJECT_NAME, value);
		map.add(entry);
	}

	private static String getGameObjectName(final DataMap map) {
		final Value<String> value = (Value<String>) map.get(GAMEOBJECT_NAME);
		return value.get();
	}

	private static <T extends GameObject> void putGameObjectRevision(final T gob, final DataMap map, final SendableProvider provider) {
		final Value<Long> value = provider.getValue(gob.getRevision());
		final MapEntry<Value<Long>> entry = provider.getMapEntry(GAMEOBJECT_REVISION, value);
		map.add(entry);
	}

	private static long getGameObjectRevision(final DataMap map) {
		final Value<Long> value = (Value<Long>) map.get(GAMEOBJECT_REVISION);
		return value.get();
	}

	private static <T extends GameObject> void putGameObjectProperties(final T gob, final DataMap map, final SendableProvider provider) {
		final AbstractSendableData<?> propData = toSendableData(gob.getAllProperties(), provider);
		final MapEntry<?> propEntry = provider.getMapEntry(GAMEOBJECT_PROPERTIES, propData);
		map.add(propEntry);
	}

	private static ListNode<DataMap> getGameObjectProperties(final DataMap map) {
		return (ListNode<DataMap>) map.get(GAMEOBJECT_PROPERTIES);
	}

	private static <T extends GameObject> void updateProperties(final T gob, final DataMap updateData) {
		ListNode<DataMap> properties = getGameObjectProperties(updateData);
		while (!properties.isEmpty()) {
			final DataMap propertyMap = properties.value();
			updateProperty(gob, propertyMap);
			properties = properties.next();
		}
	}

	private static <T extends GameObject> void updateProperty(final T gob, final DataMap propertyData) {
		final Value<String> nameValue = (Value<String>) propertyData.get(PROPERTY_NAME);
		final String name = nameValue.get();
		final Value<?> valueValue = (Value<?>) propertyData.get(PROPERTY_VALUE);
		final Object value = valueValue.get();
		gob.updateProperty(name, value);
	}

	private static <T extends GameObject> void validateMatches(final T gob, final DataMap updateData) throws IllegalArgumentException {
		final int dataUid = getGameObjectUid(updateData);
		if (dataUid != gob.getUid()) {
			throw new IllegalArgumentException("uid in updateData does not match GameObject uid");
		}
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

	private static DataMap initSendableDataForGameObject(final GameObject o, final SendableProvider provider) {
		final DataMap data = provider.getDataMap();

		putGameObjectUid(o, data, provider);
		putGameObjectName(o, data, provider);
		putGameObjectRevision(o, data, provider);
		putGameObjectProperties(o, data, provider);

		return data;
	}

	private static AbstractSendableData<?> toSendableData(final Iterable<IProperty<?>> properties, final SendableProvider provider) {
		ListNode<DataMap> list = new ListNode<DataMap>();
		for (final IProperty<?> p : properties) {
			final DataMap data = toSendableData(p, provider);
			final ListNode<DataMap> newNode = new ListNode<DataMap>(data);
			list = list.append(newNode);
		}
		return list.next() == null ? list : list.next();
	}

	private static <T extends GameObject> MapEntry<Value<String>> createTypeEntry(final T o, final SendableProvider provider) {
		final Class<?> type = o.getClass();
		final Value<String> typeValue = provider.getValue(type.getSimpleName());
		final MapEntry<Value<String>> entry = provider.getMapEntry(GAMEOBJECT_TYPE, typeValue);
		return entry;
	}

}
