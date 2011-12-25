package net.gtamps.shared.configuration;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ConfigListTest {

    @Mock
    private AbstractConfigSource source;
    private ConfigList configList;

    @Before
    public void createConfigList() throws Exception {
        configList = new ConfigList(source);
    }

    @Test
    public void testSize_whenInitialized_shouldReturn0() {
        assertEquals(0, configList.size());
    }

    @Test
    public void testSize_whenConfigAdded_shouldIncreaseBy1() {
        final int preAddSize = configList.size();

        configList.addConfiguration(new ConfigLiteralString("", source));

        assertEquals(preAddSize + 1, configList.size());
    }

    @Test
    public void testConfigListAbstractConfigSourceConfigList_shouldInitializeEqualToListArgument() {
        configList.addConfiguration(new ConfigLiteralString("", source));

        final ConfigList testee = new ConfigList(source, configList);

        assertEquals(configList, testee);
    }

    @Test
    public void testGetType_shouldReturnJavaUtilListClass() {
        assertSame(java.util.List.class, configList.getType());
    }

    @Test
    public void testGetCount_whenInitialized_shouldReturn0() {
        assertEquals(0, configList.getCount());
    }

    @Test
    public void testGetCount_whenConfigAdded_shouldIncreaseBy1() {
        final int preAddSize = configList.getCount();

        configList.addConfiguration(new ConfigLiteralString("", source));

        assertEquals(preAddSize + 1, configList.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSelectString_whenRepresentationOfInvalidIndex_expectException() {
        configList.select("0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSelectString_whenNotAnInt_expectException() {
        configList.select("wurst");
    }


    @Test
    public void testSelectInt_whenValidIndex_shouldReturnAConfiguration() {
        configList.addConfiguration(new ConfigLiteralString("", source));

        final Configuration selected = configList.select(0);

        assertNotNull(selected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSelectInt_whenInvalidIndex_expectException() {
        configList.select(0);
    }

    @Test
    public void testGetInt_whenValidIndex_shouldReturnConfiguration() {
        configList.addConfiguration(new ConfigLiteralString("", source));

        final Configuration got = configList.get(0);

        assertNotNull(got);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInt_whenInvalidIndex_expectException() {
        configList.get(0);
    }

    @Test
    public void testGetInt_whenExactlyOneElementAndIsInt_shouldReturnItsValue() {
        final Integer testInt = 123;
        configList.addConfiguration(new ConfigLiteralNumber(testInt, source));

        assertEquals(testInt, configList.getInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInt_whenExactlyOneElementAndIsNotInt_expectException() {
        configList.addConfiguration(new ConfigLiteralNumber(2.7f, source));

        configList.getInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInt_whenEmpty_expectException() {
        configList.getInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInt_whenMoreThanOneElement_expectException() {
        final Integer test = 123;
        configList.addConfiguration(new ConfigLiteralNumber(test, source));
        configList.addConfiguration(new ConfigLiteralNumber(test, source));

        configList.getInt();
    }

    @Test
    public void testGetString_whenExactlyOneElement_shouldNotReturnNull() {
        final Integer testInt = 123;
        configList.addConfiguration(new ConfigLiteralNumber(testInt, source));

        assertNotNull(configList.getString());
    }

    @Test
    public void testGetString_whenEmpty_shouldReturnEmptyString() {
        assertEquals("", configList.getString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetString_whenMoreThanOneElement_expectException() {
        final String test = "123";
        configList.addConfiguration(new ConfigLiteralString(test, source));
        configList.addConfiguration(new ConfigLiteralString(test, source));

        configList.getInt();
    }

    @Test
    public void testGetSource_shouldReturnInitialSource() {
        assertSame(source, configList.getSource());
    }

    @Test
    public void testEqualsObject_whenEqual_shouldReturnTrue() {
        final String test = "123";
        configList.addConfiguration(new ConfigLiteralString(test, source));
        final ConfigList equalList = configList.clone();
        assertNotSame("test precondition violated: clone must return different object",
                configList, equalList);

        assertTrue(configList.equals(equalList));
    }

    @Test
    public void testEqualsObject_whenNotEqual_shouldReturnFalse() {
        final ConfigList equalList = configList.clone();
        final String test = "123";
        configList.addConfiguration(new ConfigLiteralString(test, source));
        assertNotSame("test precondition violated: clone must return different object",
                configList, equalList);

        assertFalse(configList.equals(equalList));
    }

    @Test
    public void testEqualsObject_whenDifferentClass_shouldReturnFalse() {
        assertFalse(configList.equals(new Object()));
    }

    @Test
    public void testEqualsObject_whenNull_shouldReturnFalse() {
        assertFalse(configList.equals(null));
    }

    @Test
    public void testEqualsObject_whenSame_shouldReturnTrue() {
        assertTrue(configList.equals(configList));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_expectException() {
        configList.add(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove_expectException() {
        configList.remove(1);
    }

    @Ignore
    @Test
    public void testAddConfiguration() {
        fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testRemoveConfiguration() {
        fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testClearList() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetFloat_whenExactlyOneElementAndIsFloat_shouldReturnItsValue() {
        final Float test = 123.4f;
        configList.addConfiguration(new ConfigLiteralNumber(test, source));

        assertEquals(test, configList.getFloat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFloat_whenExactlyOneElementAndIsNotFloat_expectException() {
        configList.addConfiguration(new ConfigLiteralBool(true, source));

        configList.getFloat();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFloat_whenEmpty_expectException() {
        configList.getFloat();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFloat_whenMoreThanOneElement_expectException() {
        final Float test = 123.4f;
        configList.addConfiguration(new ConfigLiteralNumber(test, source));
        configList.addConfiguration(new ConfigLiteralNumber(test, source));

        configList.getFloat();
    }

    @Test
    public void testGetBoolean_whenExactlyOneElementAndIsBool_shouldReturnItsValue() {
        final Boolean test = true;
        configList.addConfiguration(new ConfigLiteralBool(test, source));

        assertEquals(test, configList.getBoolean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBoolean_whenExactlyOneElementAndIsNotBoolean_expectException() {
        configList.addConfiguration(new ConfigLiteralString("", source));

        configList.getBoolean();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBoolean_whenEmpty_expectException() {
        configList.getBoolean();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBoolean_whenMoreThanOneElement_expectException() {
        final Boolean test = true;
        configList.addConfiguration(new ConfigLiteralBool(test, source));
        configList.addConfiguration(new ConfigLiteralBool(test, source));

        configList.getBoolean();
    }

    @Test
    public void testClone_shouldReturnNewObject() {
        assertNotSame(configList, configList.clone());
    }

    @Test
    public void testClone_shouldReturnDeepCopy() {
        configList.addConfiguration(new ConfigLiteralBool(true, source));
        assertNotSame(configList.get(0), configList.clone().get(0));
    }


    @Test
    public void testGetKeys_shouldReturnCollectionOfValidIndexStrings() {
        configList.addConfiguration(new ConfigLiteralBool(true, source));
        int index = 0;
        for (final String key : configList.getKeys()) {
            assertEquals(Integer.toString(index), key);
            index++;
        }
        assertTrue(true);
    }

    @Test
    public void testIterator_whenEmpty_shouldReturnEmptyIterator() {
        assertFalse(configList.iterator().hasNext());
    }

    @Test
    public void testIterator_whenHasElements_shouldReturnMatchingIterator() {
        final Configuration c1 = new ConfigLiteralBool(true, source);
        final Configuration c2 = new ConfigLiteralBool(true, source);
        configList.addConfiguration(c1);
        configList.addConfiguration(c2);

        final Iterator<Configuration> iter = configList.iterator();

        assertEquals(c1, iter.next());
        assertEquals(c2, iter.next());
        assertFalse(iter.hasNext());
    }

}
