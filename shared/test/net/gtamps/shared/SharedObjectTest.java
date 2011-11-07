package net.gtamps.shared;

import java.util.List;

import net.gtamps.shared.SharedObject;
import net.gtamps.shared.serializer.communication.Message;
import net.gtamps.shared.serializer.communication.data.FloatData;

import org.junit.Assert;
import org.junit.Test;

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


public class SharedObjectTest extends Assert {

	static final String SERIALIZATION_LINE = "";

	@Test
	public void OKSelfTest() {
		SharedObject o = new SharedObject();
	}

	@Test
	public void OKExtensionSelfTest() {
		OKExtensionSelf o = new OKExtensionSelf();
	}

	@Test
	public void OKExtensionSamePackageTest() {
		OKExtensionSamePackage o = new OKExtensionSamePackage();
	}

	@Test
	public void OKExtensionSubPackageTest() {
		OKExtensionSubPackage o = new OKExtensionSubPackage();
	}

	@Test
	public void OKExtensionStringTest() {
		OKExtensionString o = new OKExtensionString();
	}

	@Test
	public void OKExtensionAnnotationTest() {
		OKExtensionAnnotation o = new OKExtensionAnnotation();
	}
	
	@Test
	public void OKMutualReferenceTest() {
		OKMutualReferenceA o = new OKMutualReferenceA();
	}

	@Test(expected = ClassCastException.class)
	public void WrongExtensionPackageTest() {
			WrongExtensionPackage o = new WrongExtensionPackage();
	}

	@Test(expected = ClassCastException.class)
	public void WrongExtensionPackageListTest() {
			WrongExtensionPackageList o = new WrongExtensionPackageList();
	}

	@Test(expected = ClassCastException.class)
	public void WrongExtensionInSubclassTest() {
			WrongExtensionInSubclass o = new WrongExtensionInSubclass();
	}

	@Test(expected = ClassCastException.class)
	public void WrongExtensionWrongMemberButIsNullTest() {
			WrongExtensionWrongMemberButIsNull o = new WrongExtensionWrongMemberButIsNull();
	}

	@Test(expected = ClassCastException.class)
	public void WrongExtensionWrongMemberDeclaresOKTypeTest() {
			WrongExtensionWrongMemberDeclaresOKType o = new WrongExtensionWrongMemberDeclaresOKType();
	}

}
