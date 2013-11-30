package net.gtamps.shared.serializer.communication.data;

import java.util.LinkedList;
import java.util.List;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.validate.Validate;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.IProperty;
import net.gtamps.shared.game.level.Tile;
import net.gtamps.shared.serializer.communication.SendableProvider;
import net.gtamps.shared.serializer.communication.StringConstants;

public class SendableDataConverter {

	public static <T extends GameObject> DataMap toSendableData(final T e, final SendableProvider provider) {
		Validate.notNull(e);
		Validate.notNull(provider);

		//		if(GameEvent.class.isAssignableFrom(e.getClass())){
		//			System.out.print(".");
		//		}
		//		if(Entity.class.isAssignableFrom(e.getClass())){
		//			System.out.print("O");
		//		}

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
		try{
			gob.setName(getGameObjectName(updateData));
			gob.updateRevision(getGameObjectRevision(updateData));
			updateProperties(gob, updateData);
		} catch (final RuntimeException e){
			Logger.e("SENDABLEDATACONVERTER", updateData);
			throw e;
		}
		gob.setChanged();
	}

	public static int getGameObjectUid(final DataMap map) {
		final Value<Integer> idValue = (Value<Integer>) map.get(StringConstants.GAMEOBJECT_ID);
		return idValue.get();
	}

	private static <T extends GameObject> void putGameObjectUid(final T gob, final DataMap map, final SendableProvider provider) {
		final Value<Integer> idValue = provider.getValue(gob.getUid());
		final MapEntry<Value<Integer>> entry = provider.getMapEntry(StringConstants.GAMEOBJECT_ID, idValue);
		map.add(entry);
	}

	private static <T extends GameObject> void putGameObjectName(final T gob, final DataMap map, final SendableProvider provider) {
		final Value<String> value = provider.getValue(gob.getName());
		assert value.get() != null;
		final MapEntry<Value<String>> entry = provider.getMapEntry(StringConstants.GAMEOBJECT_NAME, value);
		map.add(entry);
	}

	private static String getGameObjectName(final DataMap map) {
		final Value<String> value = (Value<String>) map.get(StringConstants.GAMEOBJECT_NAME);
		return value.get();
	}

	private static <T extends GameObject> void putGameObjectRevision(final T gob, final DataMap map, final SendableProvider provider) {
		final Value<Long> value = provider.getValue(gob.getRevision());
		final MapEntry<Value<Long>> entry = provider.getMapEntry(StringConstants.GAMEOBJECT_REVISION, value);
		map.add(entry);
	}

	private static long getGameObjectRevision(final DataMap map) {
		final Value<Long> value = (Value<Long>) map.get(StringConstants.GAMEOBJECT_REVISION);
		return value.get();
	}

	private static <T extends GameObject> void putGameObjectProperties(final T gob, final DataMap map, final SendableProvider provider) {
		final AbstractSendableData<?> propData = toSendableData(gob.getAllProperties(), provider);
		final MapEntry<?> propEntry = provider.getMapEntry(StringConstants.GAMEOBJECT_PROPERTIES, propData);
		map.add(propEntry);
	}

	private static ListNode<DataMap> getGameObjectProperties(final DataMap map) {
		return (ListNode<DataMap>) map.get(StringConstants.GAMEOBJECT_PROPERTIES);
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
		final Value<String> nameValue = (Value<String>) propertyData.get(StringConstants.PROPERTY_NAME);
		final String name = nameValue.get();
		final Value<?> valueValue = (Value<?>) propertyData.get(StringConstants.PROPERTY_VALUE);
		final Object value = valueValue.get();
		gob.updateProperty(name, value);
	}

	private static <T extends GameObject> void validateMatches(final T gob, final DataMap updateData) throws IllegalArgumentException {
		final int dataUid = getGameObjectUid(updateData);
		final String name = getGameObjectName(updateData);
		if (dataUid != gob.getUid()) {
			throw new IllegalArgumentException("uid in updateData does not match GameObject uid");
		}
	}


	private static DataMap toSendableData(final IProperty<?> p, final SendableProvider provider) {
		final DataMap dataMap = provider.getDataMap();

		// name
		final Value<?> nameValue = provider.getValue(p.getName());
		final MapEntry<?> nameEntry = provider.getMapEntry(StringConstants.PROPERTY_NAME, nameValue);
		dataMap.add(nameEntry);

		// value
		final Value<?> valueValue = provider.getValue(p.value());
		final MapEntry<?> valueEntry = provider.getMapEntry(StringConstants.PROPERTY_VALUE, valueValue);
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
		final MapEntry<Value<String>> entry = provider.getMapEntry(StringConstants.GAMEOBJECT_TYPE, typeValue);
		return entry;
	}

	public static ListNode<DataMap> tileMaptoSendableData(List<Tile> tileListList, final SendableProvider provider){
		ListNode<DataMap> xList = null;
		for(int x=0; x<tileListList.size(); x++){
				DataMap tileMap = provider.getDataMap();
				tileMap.add(provider.getMapEntry("x", provider.getValue(tileListList.get(x).getX())));
				tileMap.add(provider.getMapEntry("y", provider.getValue(tileListList.get(x).getY())));
				tileMap.add(provider.getMapEntry("h", provider.getValue(tileListList.get(x).getHeight())));
				tileMap.add(provider.getMapEntry("t", provider.getValue(tileListList.get(x).getBitmap())));
				tileMap.add(provider.getMapEntry("r", provider.getValue(tileListList.get(x).getRotation())));
				ListNode<DataMap> yListElement = provider.getListNode(tileMap);				
			if(xList == null){
				xList = yListElement;
			} else {
				xList.append(yListElement);
			}
		}
		return xList;
	}
	
	public static LinkedList<Tile> toTileMap(ListNode<DataMap> tileListMap){
		LinkedList<Tile> linkedTileList = new LinkedList<Tile>();
		tileListMap.resetIterator();
		for(DataMap tile : tileListMap){
			linkedTileList.add(new Tile(tile.getString("t"), tile.getFloat("x"), tile.getFloat("y"), tile.getFloat("h"), tile.getInt("r")));
		}
		return linkedTileList;
	}
}
