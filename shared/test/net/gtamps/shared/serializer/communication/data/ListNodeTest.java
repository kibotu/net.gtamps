package net.gtamps.shared.serializer.communication.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import net.gtamps.shared.serializer.communication.AbstractSendable;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ListNodeTest {

	@SuppressWarnings("serial")
	class Content extends AbstractSendable<Content> {

		@Override
		public boolean equals(final Object o) {
			return this == o;
		}

		@Override
		public int hashCode() {
			return this.toString().hashCode();
		}

		@Override
		protected void initHook() {
		}

		@Override
		protected void recycleHook() {
		}
	}

	ListNode<Content> listNode;

	@Before
	public final void before() {
		listNode = new ListNode<Content>(new Content());
	}

	@Ignore
	@Test
	public final void testHashCode() {
		fail("Not yet implemented"); // TODO
	}

	@Ignore
	@Test
	public final void testEqualsObject() {
		fail("Not yet implemented"); // TODO
	}

	@Ignore
	@Test
	public final void testInitHook() {
		fail("Not yet implemented"); // TODO
	}

	@Ignore
	@Test
	public final void testRecycleHook() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testEmptyList_shouldReturnAnEmptyListNode() {
		assertTrue(ListNode.emptyList().isEmpty());
	}

	@Test
	public final void testListNode_shouldReturnNonEmptyListNode() {
		assertFalse(new ListNode<Content>().isEmpty());
	}

	@Test
	public final void testListNode_shouldHaveNullValue() {
		assertNull(new ListNode<Content>().value());
	}

	@Test
	public final void testListNodeT_shouldContainDesiredValue() {
		final Content testContent = new Content();

		final ListNode<Content> testNode = new ListNode<Content>(testContent);

		assertEquals(testContent, testNode.value());
	}

	@Test
	public final void testIsEmpty_shouldBeFalse() {
		assertFalse(listNode.isEmpty());
	}

	@Test
	public final void testAppend_shouldIterateLastOverAppendedElement() {
		final ListNode<Content> existingList = listNode;
		final Content appendedContent = new Content();

		final ListNode<Content> appendedList = existingList.append(new ListNode<Content>(appendedContent));

		Content lastSeenContent = null;
		for (final Content currentContent: appendedList) {
			lastSeenContent = currentContent;
		}
		assertEquals(appendedContent, lastSeenContent);
	}

	@Test
	public final void testIterator_shouldIterateOverCompleteContentInOrderAsAdded() {
		// setup
		final Content[] contents = { new Content(), new Content(), new Content()};
		ListNode<Content> list = ListNode.emptyList();
		for (final Content current : contents) {
			list = list.append(new ListNode<Content>(current));
		}

		try {
			int i = 0;
			for (final Content current : list) {
				final Content expectedContent = contents[i];
				assertEquals("unexpected content at position " + i, expectedContent, current);
				i++;
			}
			if (i < contents.length) {
				fail("fewer listNodes than content objects");
			}
		} catch (final IndexOutOfBoundsException e) {
			fail("more ListNodes than content objects");
		}
	}

	@Test
	public final void testResetIterator() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSet() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testValue() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testNext() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testUnlink() {
		fail("Not yet implemented"); // TODO
	}

}
