package net.gtamps.shared.serializer.communication.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.gtamps.shared.Utils.cache.TypableObjectCacheFactory;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.serializer.communication.SendableCacheFactory;
import net.gtamps.shared.serializer.communication.SendableProvider;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SendableDataHelperTest {

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
		final DataMap updateData = SendableDataHelper.toSendableData(someEntity, provider);

		// run
		SendableDataHelper.updateGameobject(sameEntity, updateData);

		//assert
		assertEquals(newX, (int) sameEntity.x.value());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testUpdateGameobject_whenInvalidData_expectException() {
		SendableDataHelper.updateGameobject(differentEntity, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testUpdateGameobject_whenObjectNotMatching_expectException() {
		// setup
		final DataMap updateData = SendableDataHelper.toSendableData(someEntity, provider);

		// run
		SendableDataHelper.updateGameobject(differentEntity, updateData);
	}

	@Ignore
	@Test
	public final void testToEntity_whenValidData_shouldCreateEntity() {
		fail("Not yet implemented"); // TODO
	}

	@Test(expected =IllegalArgumentException.class)
	public final void testToSendableDataTSendableProvider_whenNullElement_expectException() {
		SendableDataHelper.toSendableData((GameObject) null, provider);
	}

	@Test(expected =IllegalArgumentException.class)
	public final void testToSendableDataTSendableProvider_whenNullProvider_expectException() {
		SendableDataHelper.toSendableData(someEntity, null);
	}

	@Test
	public final void testToSendableDataTSendableProvider() {
		fail("Not yet implemented"); // TODO
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testToSendableDataListOfTSendableProvider_whenNullList_expectException() {
		SendableDataHelper.toSendableData((List<GameObject>)null, provider);
	}	

	@Test(expected = IllegalArgumentException.class)
	public final void testToSendableDataListOfTSendableProvider_whenNullProvider_expectException() {
		SendableDataHelper.toSendableData(Collections.<GameObject>emptyList(), null);
	}	

	@Test
	public final void testToSendableDataListOfTSendableProvider_shouldReturnListNode() {
		// setup
		final List<Entity> testlist = Collections.singletonList(someEntity);

		// run
		final AbstractSendableData<?> testee = SendableDataHelper.toSendableData(testlist, provider); 

		// assert
		assertEquals(ListNode.class, testee.getClass());
	}


	@Test
	public final void testToSendableDataListOfTSendableProvider_whenEmptyList_shouldReturnEmptyListData() {
		// setup
		final List<Entity> testlist = Collections.emptyList();

		// run
		final ListNode<?> testee = (ListNode<?>) SendableDataHelper.toSendableData(testlist, provider); 

		// assert
		assertTrue(testee.isEmpty());
	}

	@Test
	public final void testToSendableDataListOfTSendableProvider_whenNonEmptyList_shouldReturnSameSizeListData() {
		final List<Entity> testlist = Arrays.asList(new Entity[]{someEntity, differentEntity});

		final ListNode<DataMap> testee = (ListNode<DataMap>) SendableDataHelper.toSendableData(testlist, provider);

		try {
			int i = 0;
			for (final DataMap map: testee) {
				assertEquals(testlist.get(i).getUid(), SendableDataHelper.getGameObjectUid(map));
				i++;
			}
			if (i < testlist.size()) {
				fail("generated list is shorter than input list");
			}
		} catch (final IndexOutOfBoundsException e) {
			fail("generated list is longer than input list");
		}
	}

}
