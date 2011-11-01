package net.gtamps.game.physics;

import net.gtamps.game.conf.PhysicalProperties;

import org.jbox2d.collision.MassData;
import org.jbox2d.common.Vec2;

/**
 * A wrapper for jbox2d Body attributes that allows <tt>null</t>
 * as a marker for undefined values, and the setting of default
 * values different from BodyDef's.
 *
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 *
 */
public class BodyData {
	public static final boolean ALLOW_SLEEP_DEFAULT = true;
	public static final boolean FIXED_ROTATION_DEFAULT = false;
	public static final boolean IS_BULLET_DEFAULT = false;
	public static final float ANGLE_DEFAULT = 0f;
	public static final float ANGULAR_DAMPING_DEFAULT = 0f;
	public static final float LINEAR_DAMPING_DEFAULT = 0f;
	public static final Vec2 POSITION_DEFAULT = new Vec2(0f, 0f);
	public static final MassData MASSDATA_DEFAULT = new MassData();
	public static final Object USER_DATA_DEFAULT = null;
	
	Boolean allowSleep;
	Float angle;
	Float angularDamping;
	Boolean fixedRotation;
	Boolean isBullet;
	Float linearDamping;
	MassData massData;
	Vec2 position;
	Object userData;
	
	public BodyData() {
		allowSleep = null;
		angle = null;
		angularDamping = null;
		fixedRotation = null;
		isBullet = null;
		linearDamping = null;
		massData = null;
		position = null;
		userData = null;
	}
	
	public BodyData(final BodyData bodyData) {
		allowSleep = bodyData.allowSleep;
		angle = bodyData.angle;
		angularDamping = bodyData.angularDamping;
		fixedRotation = bodyData.fixedRotation;
		isBullet = bodyData.isBullet;
		linearDamping = bodyData.linearDamping;
		massData = bodyData.massData;
		position = bodyData.position;
		userData = bodyData.userData;
	}
	
	public BodyData(final PhysicalProperties physicalProperties) {
		allowSleep = null;
		angle = null;
		angularDamping = physicalProperties.ANGULAR_DAMPING;
		fixedRotation = null;
		isBullet = physicalProperties.TYPE.equals(PhysicalProperties.Type.BULLET);
		linearDamping = physicalProperties.LINEAR_DAMPING;
		massData = null;
		position = null;
		userData = null;
	}
	
	public boolean getAllowSleep() {
		return allowSleep != null ? allowSleep : ALLOW_SLEEP_DEFAULT;
	}
	
	public float getAngle() {
		return angle != null ? angle : ANGLE_DEFAULT;
	}

	public float getAngularDamping() {
		return angularDamping != null ? angularDamping : ANGULAR_DAMPING_DEFAULT;
	}
	
	public boolean getFixedRotation() {
		return fixedRotation != null ? fixedRotation : FIXED_ROTATION_DEFAULT;
	}
	
	public boolean getIsBullet() {
		return isBullet != null ? isBullet : IS_BULLET_DEFAULT;
	}
	
	public float getLinearDamping() {
		return linearDamping != null ? linearDamping : LINEAR_DAMPING_DEFAULT;
	}
	
	public MassData getMassData() {
		return massData != null ? massData : MASSDATA_DEFAULT;
	}
	
	public Vec2 getPosition() {
		return position != null ? position : POSITION_DEFAULT;
	}
	
	public Object getUserData() {
		return userData != null ? userData : USER_DATA_DEFAULT;
	}
	
}
