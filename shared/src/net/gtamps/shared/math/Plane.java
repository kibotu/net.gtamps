package net.gtamps.shared.math;

import org.jetbrains.annotations.NotNull;

/**
 * Eine Ebene
 */
public final class Plane {

	/**
	 * Position der Plane
	 */
	@NotNull
	private Vector3 _center;
	
	/**
	 * Normale der Plane
	 */
	@NotNull
	private Vector3 _normal;

	/**
	 * Entfernung zum Koordinatzenursprung
	 */
	private float _distance;

	/**
	 * Erzeugt eine Plane aus drei Punkten
	 * @param a Erster Punkt
	 * @param b Zeiter Punkt
	 * @param c Dritter Punkt
	 */
	public Plane(@NotNull Vector3 a, @NotNull Vector3 b, @NotNull Vector3 c) {
		assert !a.equals(b) && !b.equals(c);

		define(a, b, c);
	}
	
	/**
	 * Erzeugt eine Plane über ihren Mittelpunkt und ihre Normale
	 * @param center Mittelpunkt
	 * @param normal Normale
	 */
	public Plane(@NotNull Vector3 center, @NotNull Vector3 normal) {
		assert !normal.isEmpty();

		define(center, normal);
	}
	
	/**
	 * Erzeugt eine XZ-Plane
	 */
	public Plane() {
		define(Vector3.ZERO, Vector3.YAXIS);
	}
	
	/**
	 * Definiert die Plane über drei Punkte.
	 * @param a Erster Punkt
	 * @param b Zweiter Punkt
	 * @param c Dritter Punkt
	 */
	public void define(@NotNull Vector3 a, @NotNull Vector3 b, @NotNull Vector3 c) {
		assert !a.equals(b) && !b.equals(c);

		_center = a;
		_distance = _center.getLength();

		// Normale berechnen: _normal = a.sub(b).cross(a.sub(c));
		Vector3 aSubB = a.sub(b);
		Vector3 aSubC = a.sub(c);
		try {
			_normal = aSubB.cross(aSubC);
			_normal.normalize();
		}
		finally {
			aSubB.recycle();
			aSubC.recycle();
		}
	}
	
	/**
	 * Definiert die Plane über einen Punkt und eine Normake
	 * @param center Der Punkt
	 * @param normal Die Normale
	 */
	public void define(@NotNull Vector3 center, @NotNull Vector3 normal) {
		assert !normal.isEmpty();

		_center = center;
		_distance = center.getLength();
		_normal = normal;
	}

	/**
	 * Ermittelt die Distanz eines Punktes zur Ebene
	 * @param point Der Punkt
	 * @return Die Distanz zur Ebene
	 */
	public float getDistance(@NotNull Vector3 point) {
		Vector3 direction = point.sub(_center);
		try {
			return _normal.dot(direction); // / _normal.getLength();
		}
		finally {
			direction.recycle();
		}
	}

	/**
	 * Ermittelt die Distanz zum Koordinatenursprung
	 * @return Die Distanz zum Koordinatenursprung
	 */
	public float getDistance() {
		return _distance;
	}

	/**
	 * Stringumwandlungs-Fu
	 * @return Le string
	 */
	@Override
	public String toString() {
		return "{center: "+_center+", normal: " + _normal+"}";
	}


}
