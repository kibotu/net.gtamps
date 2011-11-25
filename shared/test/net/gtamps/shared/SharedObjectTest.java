package net.gtamps.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.gtamps.shared.serializer.communication.Message;
import net.gtamps.shared.serializer.communication.data.FloatData;

import org.junit.Assert;
import org.junit.Test;


public class SharedObjectTest extends Assert {


	@Test
	public void OKSelfTest() {
		new SharedObject();
	}

	@Test
	public void OKExtensionSelfTest() {
		final OKExtensionSelf o = new OKExtensionSelf();
		o.isShareable();
	}

	@Test
	public void OKExtensionSamePackageTest() {
		final OKExtensionSamePackage o = new OKExtensionSamePackage();
		o.isShareable();
	}

	@Test
	public void OKExtensionSubPackageTest() {
		final OKExtensionSubPackage o = new OKExtensionSubPackage();
		o.isShareable();
	}

	@Test
	public void OKExtensionStringTest() {
		final OKExtensionString o = new OKExtensionString();
		o.isShareable();
	}

	@Test
	public void OKExtensionAnnotationTest() {
		final OKExtensionAnnotation o = new OKExtensionAnnotation();
		o.isShareable();
	}

	@Test
	public void OKMutualReferenceTest() {
		final OKMutualReferenceA o = new OKMutualReferenceA();
		o.isShareable();
	}

	@Test
	public void OKByAnnotationTest() {
		final OKByAnnotation o = new OKByAnnotation();
		o.isShareable();
	}

	@Test(expected = ClassCastException.class)
	public void WrongExtensionPackageTest() {
		final WrongExtensionPackage o = new WrongExtensionPackage();
		assertTrue(o.isShareable());
	}

	@Test(expected = ClassCastException.class)
	public void WrongExtensionPackageListTest() {
		final WrongExtensionPackageList o = new WrongExtensionPackageList();
		o.listField = new ArrayList<Object>();
		o.listField.add(new Object());
		assertTrue(o.isShareable());
	}

	@Test(expected = ClassCastException.class)
	public void WrongExtensionInSubclassTest() {
		final WrongExtensionInSubclass o = new WrongExtensionInSubclass();
		assertTrue(o.subclassField.isShareable());
		assertTrue(o.isShareable());
	}

	@Test(expected = ClassCastException.class)
	public void WrongExtensionWrongMemberButIsNullTest() {
		final WrongExtensionWrongMemberButIsNull o = new WrongExtensionWrongMemberButIsNull();
		assertTrue(o.isShareable());
	}

	@Test(expected = ClassCastException.class)
	public void WrongExtensionWrongMemberDeclaresOKTypeTest() {
		final WrongExtensionWrongMemberDeclaresOKType o = new WrongExtensionWrongMemberDeclaresOKType();
		assertTrue(o.subclassField.isShareable());
		assertTrue(o.isShareable());
	}

	@Test(expected = ClassCastException.class)
	public void WrongNotFinalTest() {
		final SharedObject o = new WrongNotFinal();
		assertTrue(o.isShareable());
	}

	@Test(expected = ClassCastException.class)
	public void WrongNotPublicTest() {
		final SharedObject o = new WrongNotPublic();
		assertTrue(o.isShareable());
	}

}

class OKExtensionSelf extends SharedObject {
	// everything else would be endless recursion
	OKExtensionSelf selfField = null;
}

class OKExtensionSamePackage extends SharedObject {
	Message samePackageField = new Message();
}

class OKExtensionSubPackage extends SharedObject {
	FloatData sendableField = new FloatData(0);
}

class OKExtensionString extends SharedObject {
	String stringField = "blub";
}

class OKExtensionAnnotation extends SharedObject {
	@SuppressWarnings("unused")
	private String annotatedField;
}

class WrongExtensionPackage extends SharedObject {
	javax.swing.Box wrongPackageField;
}

class WrongExtensionPackageList extends SharedObject {
	List<Object> listField;
}

class WrongExtensionInSubclass extends SharedObject {
	WrongExtensionPackage subclassField = new WrongExtensionPackage();
}

class WrongExtensionWrongMemberButIsNull extends SharedObject {
	List<?> subclassField = null;
}

class WrongExtensionWrongMemberDeclaresOKType extends SharedObject {
	SharedObject subclassField = new WrongExtensionWrongMemberButIsNull();
}

class OKMutualReferenceA extends SharedObject {
	OKMutualReferenceB b = new OKMutualReferenceB(); 
}

class OKMutualReferenceB extends SharedObject {
	OKMutualReferenceA a; 
}

class OKByAnnotation extends SharedObject {
	@CheckedShareable
	public final Map<Object,Object> immutableReferenceToOKClass = new HashMap<Object,Object>();
}

class WrongNotFinal extends SharedObject {
	public Map<Object,Object> mutableReferenceToOKClass = new HashMap<Object,Object>();
}

class WrongNotPublic extends SharedObject {
	protected final Map<Object,Object> inaccessibleReferenceToOKClass = new HashMap<Object,Object>();
}

