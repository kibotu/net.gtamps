package net.gtamps.shared.serializer.communication.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.gtamps.shared.Utils.cache.TypableObjectCacheFactory;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.level.Tile;
import net.gtamps.shared.serializer.communication.SendableCacheFactory;
import net.gtamps.shared.serializer.communication.SendableProvider;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SendableDataConverterTest {

	private static final int ENTITY_UID_2 = 2;
	private static final int ENTITY_UID_1 = 1;

	private static final String ENTITY_NAME_2 = "name2";
	private static final String ENTITY_NAME_1 = "name1";

	private final TypableObjectCacheFactory cacheFactory = new SendableCacheFactory();

	SendableProvider provider;

	Entity someEntity;
	Entity sameEntity;
	Entity differentEntity;

	@Before
	public final void before() {
		provider = new SendableProvider(cacheFactory); 
		someEntity = new Entity(ENTITY_NAME_1, ENTITY_UID_1);
		sameEntity = new Entity(ENTITY_NAME_1, ENTITY_UID_1);
		differentEntity = new Entity(ENTITY_NAME_2, ENTITY_UID_2);
	}


	@Test
	public final void testUpdateGameobject_whenMatchingObject_shouldUpdate() {
		// setup
		final int newX = someEntity.x.value() + 1;
		someEntity.x.set(newX);
		final DataMap updateData = SendableDataConverter.toSendableData(someEntity, provider);

		// run
		SendableDataConverter.updateGameobject(sameEntity, updateData);

		//assert
		assertEquals(newX, (int) sameEntity.x.value());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testUpdateGameobject_whenInvalidData_expectException() {
		SendableDataConverter.updateGameobject(differentEntity, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testUpdateGameobject_whenObjectNotMatching_expectException() {
		// setup
		final DataMap updateData = SendableDataConverter.toSendableData(someEntity, provider);

		// run
		SendableDataConverter.updateGameobject(differentEntity, updateData);
	}

	@Ignore
	@Test
	public final void testToEntity_whenValidData_shouldCreateEntity() {
		fail("Not yet implemented"); // TODO
	}

	@Test(expected =IllegalArgumentException.class)
	public final void testToSendableDataTSendableProvider_whenNullElement_expectException() {
		SendableDataConverter.toSendableData((GameObject) null, provider);
	}

	@Test(expected =IllegalArgumentException.class)
	public final void testToSendableDataTSendableProvider_whenNullProvider_expectException() {
		SendableDataConverter.toSendableData(someEntity, null);
	}

	@Test
	public final void testToSendableDataTSendableProvider() {
		fail("Not yet implemented"); // TODO
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testToSendableDataListOfTSendableProvider_whenNullList_expectException() {
		SendableDataConverter.toSendableData((List<GameObject>)null, provider);
	}	

	@Test(expected = IllegalArgumentException.class)
	public final void testToSendableDataListOfTSendableProvider_whenNullProvider_expectException() {
		SendableDataConverter.toSendableData(Collections.<GameObject>emptyList(), null);
	}	

	@Test
	public final void testToSendableDataListOfTSendableProvider_shouldReturnListNode() {
		// setup
		final List<Entity> testlist = Collections.singletonList(someEntity);

		// run
		final AbstractSendableData<?> testee = SendableDataConverter.toSendableData(testlist, provider); 

		// assert
		assertEquals(ListNode.class, testee.getClass());
	}


	@Test
	public final void testToSendableDataListOfTSendableProvider_whenEmptyList_shouldReturnEmptyListData() {
		// setup
		final List<Entity> testlist = Collections.emptyList();

		// run
		final ListNode<?> testee = (ListNode<?>) SendableDataConverter.toSendableData(testlist, provider); 

		// assert
		assertTrue(testee.isEmpty());
	}

	@Test
	public final void testToSendableDataListOfTSendableProvider_whenNonEmptyList_shouldReturnSameSizeListData() {
		final List<Entity> testlist = Arrays.asList(new Entity[]{someEntity, differentEntity});

		final ListNode<DataMap> testee = (ListNode<DataMap>) SendableDataConverter.toSendableData(testlist, provider);

		try {
			int i = 0;
			for (final DataMap map: testee) {
				assertEquals(testlist.get(i).getUid(), SendableDataConverter.getGameObjectUid(map));
				i++;
			}
			if (i < testlist.size()) {
				fail("generated list is shorter than input list");
			}
		} catch (final IndexOutOfBoundsException e) {
			fail("generated list is longer than input list");
		}
	}
	
	@Test
	public final void testToSendableDataListofListofTiles() {
		@SuppressWarnings("unchecked")
		List<Tile> tileListList = new LinkedList<Tile>();
		tileListList.add(new Tile("bitmap1.jpg", 0.1f, 0.2f, 4f, 0));
		tileListList.add(new Tile("bitmap12.jpg", 0.12f, 0.22f, 42f, 90));
		tileListList.add(new Tile("bitmap14.jpg", 0.14f, 0.24f, 44f, 180));
		tileListList.add(new Tile("bitmap124.jpg", 0.124f, 0.224f, 424f, 270));
		ListNode<DataMap> listifiedTiles = SendableDataConverter.tileMaptoSendableData(tileListList, provider);
		assertEquals(tileListList, SendableDataConverter.toTileMap(listifiedTiles));

	}

}
