package net.gtamps.shared.serializer.communication.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;

import net.gtamps.shared.Utils.cache.ObjectFactory;
import net.gtamps.shared.Utils.cache.TypableObjectCacheFactory;
import net.gtamps.shared.Utils.cache.annotations.ReturnsCachedValue;
import net.gtamps.shared.serializer.communication.SendableCacheFactory;
import net.gtamps.shared.serializer.communication.SendableProvider;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * this is not a proper automated unit test, but rather a quick manual check which
 * test some functionality in a need-your-eyeballs, unmethodical way
 *
 * @author Tom Wallroth, Jan Rabe, Til Boerner
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SendableProviderTest {

	private final TypableObjectCacheFactory cacheFactory = new SendableCacheFactory();

	private SendableProvider sdb;

	@Before
	public void createSendableProvider() {
		sdb = new SendableProvider(cacheFactory);
	}

	@Ignore
	@Test
	public void testSendableProvider() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetValue() {
		final Value<String> sval = sdb.getValue("bla");
		System.out.println(sval.get());
		sval.recycle();
		final Value<String> sval2 = sdb.getValue("blub");
		System.out.println(sval2.get());
		assertEquals(sval.get(), "blub");
		sval2.recycle();

	}

	@Test
			public void testGetListNode() {
				System.out.println();
				final ListNode<Value<String>> ln = sdb.getListNode(sdb.getValue("bla"));
				System.out.println("ln.value = " + ln.value().get());
				final ListNode<Value<String>> ln2 = sdb.getListNode(sdb.getValue("blub"));
				System.out.println("ln2.value = " + ln2.value().get());
				ln.append(ln2);
				System.out.println("ln: " + ln);
				ln.recycle();
			}

	@Test
	public void testGetMapEntry() {
		final MapEntry<Value<Boolean>> e = sdb.getMapEntry("lala", sdb.getValue(true));
		System.out.println(e);
		e.recycle();
	}

	@Test
	public void testGetDataMap() {
		final DataMap map = sdb.getDataMap()
				.add(sdb.getMapEntry().setKey("lala").setValue(sdb.getValue(true)))
				.add(sdb.getMapEntry().setKey("lulu").setValue(sdb.getValue(5)));
		System.out.println(map);
		map.recycle();
	}

	private <T> ObjectFactory<T> createTypedObjectFactory(final Class<T> type) {
		return new ObjectFactory<T>() {
			@ReturnsCachedValue
			@Override
			public T createNew() {
				try {
					return type.getConstructor().newInstance();
				} catch (final IllegalArgumentException e) {
					e.printStackTrace();
				} catch (final SecurityException e) {
					e.printStackTrace();
				} catch (final InstantiationException e) {
					e.printStackTrace();
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
				} catch (final InvocationTargetException e) {
					e.printStackTrace();
				} catch (final NoSuchMethodException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
	}

}
