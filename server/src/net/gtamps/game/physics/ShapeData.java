package net.gtamps.game.physics;

import org.jbox2d.collision.FilterData;
import org.jbox2d.collision.shapes.ShapeDef;
import org.jbox2d.collision.shapes.ShapeType;

public class ShapeData {

	public static final Float DENSITY_DEFAULT = 0f;
	public static final FilterData FILTER_DEFAULT = new ShapeDef().filter;
	public static final Float FRICTION_DEFAULT = 0.2f;
	public static final Boolean ISSENSOR_DEFAULT = false;
	public static final Float RESTITUTION_DEFAULT = 0f;
	public static final ShapeType TYPE_DEFAULT = ShapeType.UNKNOWN_SHAPE;
	public static final Object USERDATA_DEFAULT = null;
	public static final Float HX_DEFAULT = 1f;
	public static final Float HY_DEFAULT = 1f;
	public static final Float RADIUS_DEFAULT = (float)Math.sqrt(2.0);

	Float density;
	FilterData filter;
	Float friction;
	Boolean isSensor;
	Float restitution;
	ShapeType type;
	Object userData;

	Float hx;
	Float hy;
	Float radius;
	
	public Float getDensity() {
		return density != null ? density : DENSITY_DEFAULT;
	}

	public FilterData getFilter() {
		return filter != null ? filter : FILTER_DEFAULT;
	}

	public Float getFriction() {
		return friction != null ? friction : FRICTION_DEFAULT;
	}

	public Boolean getIsSensor() {
		return isSensor != null ? isSensor : ISSENSOR_DEFAULT;
	}

	public Float getRestitution() {
		return restitution != null ? restitution : RESTITUTION_DEFAULT;
	}

	public ShapeType getType() {
		return type != null ? type : TYPE_DEFAULT;
	}

	public Object getUserData() {
		return userData != null ? userData : USERDATA_DEFAULT;
	}

	public Float getHx() {
		return hx;
	}

	public Float getHy() {
		return hy;
	}

	public Float getRadius() {
		return radius;
	}



}
