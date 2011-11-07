package net.gtamps.shared;

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
		SharedObject o = new SharedObject();
		o.isShareable();
	}

	@Test
	public void OKExtensionSelfTest() {
		OKExtensionSelf o = new OKExtensionSelf();
		o.isShareable();
	}

	@Test
	public void OKExtensionSamePackageTest() {
		OKExtensionSamePackage o = new OKExtensionSamePackage();
		o.isShareable();
	}

	@Test
	public void OKExtensionSubPackageTest() {
		OKExtensionSubPackage o = new OKExtensionSubPackage();
		o.isShareable();
	}

	@Test
	public void OKExtensionStringTest() {
		OKExtensionString o = new OKExtensionString();
		o.isShareable();
	}

	@Test
	public void OKExtensionAnnotationTest() {
		OKExtensionAnnotation o = new OKExtensionAnnotation();
		o.isShareable();
	}
	
	@Test
	public void OKMutualReferenceTest() {
		OKMutualReferenceA o = new OKMutualReferenceA();
		o.isShareable();
	}
	
	@Test
	public void OKPublicFinalMemberTest() {
		OKPublicFinalMember o = new OKPublicFinalMember();
		o.isShareable();
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
	
	@Test(expected = ClassCastException.class)
	public void WrongNotFinalTest() {
		new WrongNotFinal();
	}

	@Test(expected = ClassCastException.class)
	public void WrongNotPublicTest() {
		new WrongNotPublic();
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

class OKPublicFinalMember extends SharedObject {
	public final Map<Object,Object> immutableReferenceToOKClass = new HashMap<Object,Object>();
}

class WrongNotFinal extends SharedObject {
	public Map<Object,Object> mutableReferenceToOKClass = new HashMap<Object,Object>();
}

class WrongNotPublic extends SharedObject {
	protected final Map<Object,Object> inaccessibleReferenceToOKClass = new HashMap<Object,Object>();
}

