package net.gtamps.shared.configuration;

import static org.junit.Assert.*;
import java.io.File;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigSourceTest {

	@Mock
	private File file;

	@Test
	public void testHashCode_whenSame_shouldReturnSameHashCode() {
		final AbstractConfigSource configSource = new ConfigSource(file);
		final AbstractConfigSource sameConfigSource = new ConfigSource(file);
		assertEquals(configSource.hashCode(), sameConfigSource.hashCode());
	}

	@Test(expected=NullPointerException.class)
	public void testConfigSourceURL_whenNull_shouldThrowNullPointerException() {
		new ConfigSource((URL) null);
	}

	@Test(expected=NullPointerException.class)
	public void testConfigSourceFile_whenNull_shouldThrowNullPointerException() {
		new ConfigSource((File) null);
	}

	@Test(expected=NullPointerException.class)
	public void testConfigSourceString_whenNull_shouldThrowNullPointerException() {
		new ConfigSource((String) null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConfigSourceString_whenEmpty_shouldThrowIllegalArgumentException() {
		new ConfigSource("");
	}

	@Test
	public void testEqualsObject_whenSame_shouldReturnTrue() {
		final AbstractConfigSource configSource = new ConfigSource(file);
		assertTrue(configSource.equals(configSource));
	}

	/**
	 * use the string version, because mock objects don't do equals()
	 */
	@Test
	public void testEqualsObject_whenDifferentValues_shouldReturnFalse() {
		final AbstractConfigSource aConfigSource = new ConfigSource("a");
		final AbstractConfigSource bConfigSource = new ConfigSource("b");
		assertFalse(aConfigSource.equals(bConfigSource));
	}

	@Test
	public void testEqualsObject_whenDifferentTypes_shouldReturnFalse() {
		final String string = "abc";
		final AbstractConfigSource aConfigSource = new ConfigSource(string);
		final AbstractConfigSource bConfigSource = new ConfigSource(new File(string));
		assertFalse(aConfigSource.equals(bConfigSource));
	}

}
