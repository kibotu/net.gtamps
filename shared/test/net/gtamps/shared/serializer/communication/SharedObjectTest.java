package net.gtamps.shared.serializer.communication;

import java.util.List;

import net.gtamps.shared.serializer.communication.data.FloatData;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("serial")
class AssertionErrorWrapper extends RuntimeException {
	public AssertionErrorWrapper() {
		super();
	}
	public AssertionErrorWrapper(Throwable cause) {
		super(cause.getMessage(), cause);
	}

	public String toString() {
		return (this.getCause() == null) ? "" : this.getCause().toString();
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
	WrongExtensionPackage subclassField;
}

class WrongExtensionWrongMemberButIsNull extends SharedObject {
	List<?> subclassField = null;
}

class WrongExtensionWrongMemberDeclaresOKType extends SharedObject {
	SharedObject subclassField = new WrongExtensionWrongMemberButIsNull();
}

public class SharedObjectTest extends Assert {

	static final String SERIALIZATION_LINE = "[[[------- serialization: ----->]]]";

	@Test
	public void OKSelfTest() {
		System.out.println("\n\nNEW TEST\n======== SharedObject");
		SharedObject o = new SharedObject();
		System.out.println("\n\n");
	}

	@Test
	public void OKExtensionSelfTest() {
		System.out.println("\n\nNEW TEST\n======== OKExtensionSelf");
		OKExtensionSelf o = new OKExtensionSelf();
		System.out.println(SERIALIZATION_LINE);
		o.selfField = o;
		assert o.isShareable();
		System.out.println("\n\n");
	}

	@Test
	public void OKExtensionSamePackageTest() {
		System.out.println("\n\nNEW TEST\n======== OKExtensionSamePackage");
		OKExtensionSamePackage o = new OKExtensionSamePackage();
		System.out.println(SERIALIZATION_LINE);
		assert o.isShareable();
		System.out.println("\n\n");
	}

	@Test
	public void OKExtensionSubPackageTest() {
		System.out.println("\n\nNEW TEST\n======== OKExtensionSubPackage");
		OKExtensionSubPackage o = new OKExtensionSubPackage();
		System.out.println(SERIALIZATION_LINE);
		assert o.isShareable();
		System.out.println("\n\n");
	}

	@Test
	public void OKExtensionStringTest() {
		System.out.println("\n\nNEW TEST\n======== OKExtensionString");
		OKExtensionString o = new OKExtensionString();
		System.out.println(SERIALIZATION_LINE);
		assert o.isShareable();
		System.out.println("\n\n");
	}

	@Test
	public void OKExtensionAnnotationTest() {
		System.out.println("\n\nNEW TEST\n======== OKExtensionAnnotation");
		OKExtensionAnnotation o = new OKExtensionAnnotation();
		System.out.println(SERIALIZATION_LINE);
		assert o.isShareable();
		System.out.println("\n\n");
	}

	@Test(expected = AssertionErrorWrapper.class)
	public void WrongExtensionPackageTest() {
		try {
			System.out.println("\n\nNEW TEST\n======== WrongExtensionPackage");
			WrongExtensionPackage o = new WrongExtensionPackage();
			System.out.println(SERIALIZATION_LINE);
			if (! o.isShareable()) {
				throw new AssertionError();
			}
		} catch (AssertionError e) {
			throw new AssertionErrorWrapper(e);
		} finally {
			System.out.println("\n\n");
		}
	}

	@Test(expected = AssertionErrorWrapper.class)
	public void WrongExtensionPackageListTest() {
		try {
			System.out.println("\n\nNEW TEST\n======== WrongExtensionPackageList");
			WrongExtensionPackageList o = new WrongExtensionPackageList();
			System.out.println(SERIALIZATION_LINE);
			if (! o.isShareable()) {
				throw new AssertionError();
			}
		} catch (AssertionError e) {
			throw new AssertionErrorWrapper(e);
		} finally {
			System.out.println("\n\n");
		}
	}

	@Test(expected = AssertionErrorWrapper.class)
	public void WrongExtensionInSubclassTest() {
		try {
			System.out.println("\n\nNEW TEST\n======== WronExtensionInSubclass");
			WrongExtensionInSubclass o = new WrongExtensionInSubclass();
			System.out.println(SERIALIZATION_LINE);
			if (! o.isShareable()) {
				throw new AssertionError();
			}
		} catch (AssertionError e) {
			throw new AssertionErrorWrapper(e);
		} finally {
			System.out.println("\n\n");
		}
	}

	@Test(expected = AssertionErrorWrapper.class)
	public void WrongExtensionWrongMemberButIsNullTest() {
		try {
			System.out.println("\n\nNEW TEST\n======== WrongExtensionWrongMemberButIsNull");
			WrongExtensionWrongMemberButIsNull o = new WrongExtensionWrongMemberButIsNull();
			System.out.println(SERIALIZATION_LINE);
			if (! o.isShareable()) {
				throw new AssertionError();
			}
		} catch (AssertionError e) {
			throw new AssertionErrorWrapper(e);
		} finally {
			System.out.println("\n\n");
		}

	}

	@Test(expected = AssertionErrorWrapper.class)
	public void WrongExtensionWrongMemberDeclaresOKTypeTest() {
		try {
			System.out.println("\n\nNEW TEST\n======== WrongExtensionWrongMemberDeclaresOKType");
			WrongExtensionWrongMemberDeclaresOKType o = new WrongExtensionWrongMemberDeclaresOKType();
			System.out.println(SERIALIZATION_LINE);
			if (! o.isShareable()) {
				throw new AssertionError();
			}
		} catch (AssertionError e) {
			throw new AssertionErrorWrapper(e);
		} finally {
			System.out.println("\n\n");
		}
	}

}
