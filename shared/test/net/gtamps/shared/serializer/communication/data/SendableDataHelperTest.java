package net.gtamps.shared.serializer.communication.data;

import static org.junit.Assert.*;
import java.util.Collections;
import java.util.List;

import net.gtamps.shared.Utils.cache.TypableObjectCacheFactory;
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

	@Test
	public final void testToSendableDataTSendableProvider() {
		fail("Not yet implemented"); // TODO
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
		fail("Not yet implemented"); // TODO
	}

}
