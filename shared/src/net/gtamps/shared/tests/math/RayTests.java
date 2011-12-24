package net.gtamps.shared.tests.math;

import net.gtamps.shared.Utils.math.Ray3;
import net.gtamps.shared.Utils.math.RayFactory;
import net.gtamps.shared.Utils.math.Vector3;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static net.gtamps.shared.Utils.math.MathUtils.DEFAULT_EPSILON;
import static org.junit.Assert.assertEquals;

/**
 * Tests für {@link Ray3}
 */
public class RayTests {

    /**
     * Prüft die Distanzberechnung
     */
    @Test
    public void distance() {
        Ray3 ray = RayFactory.rayFromTwoPoints(
                Vector3.createNew(0, 0, 0),
                Vector3.createNew(1, 0, 0)
        );

        // Nulldistanz
        assertEquals(0, ray.getDistanceFromPoint(Vector3.createNew(0, 0, 0)), DEFAULT_EPSILON);
        assertEquals(0, ray.getDistanceFromPoint(Vector3.createNew(-1, 0, 0)), DEFAULT_EPSILON);
        assertEquals(0, ray.getDistanceFromPoint(Vector3.createNew(1000, 0, 0)), DEFAULT_EPSILON);

        // Distanz 1
        assertEquals(1, ray.getDistanceFromPoint(Vector3.createNew(0, 1, 0)), DEFAULT_EPSILON);
        assertEquals(1, ray.getDistanceFromPoint(Vector3.createNew(-1, 1, 0)), DEFAULT_EPSILON);
        assertEquals(1, ray.getDistanceFromPoint(Vector3.createNew(1000, 1, 0)), DEFAULT_EPSILON);
        assertEquals(1, ray.getDistanceFromPoint(Vector3.createNew(0, -1, 0)), DEFAULT_EPSILON); // TODO: Diese Werte sollten vorzeichenbehaftet sein dürfen!
        assertEquals(1, ray.getDistanceFromPoint(Vector3.createNew(-1, -1, 0)), DEFAULT_EPSILON);
        assertEquals(1, ray.getDistanceFromPoint(Vector3.createNew(1000, -1, 0)), DEFAULT_EPSILON);
    }

    /**
     * Testet die Vektorprojektion
     */
    @Test
    public void project() {
        Ray3 ray = RayFactory.rayFromTwoPoints(
                Vector3.createNew(0, 0, 0),
                Vector3.createNew(1, 0, 0)
        );

        assertTrue(ray.projectPoint(Vector3.createNew(0, 0, 0)).equals(0, 0, 0, DEFAULT_EPSILON));
        assertTrue(ray.projectPoint(Vector3.createNew(10, 0, 0)).equals(10, 0, 0, DEFAULT_EPSILON));
        assertTrue(ray.projectPoint(Vector3.createNew(-42, 0, 0)).equals(-42, 0, 0, DEFAULT_EPSILON));

        assertTrue(ray.projectPoint(Vector3.createNew(0, 1, 0)).equals(0, 0, 0, DEFAULT_EPSILON));
        assertTrue(ray.projectPoint(Vector3.createNew(1, 1, 0)).equals(1, 0, 0, DEFAULT_EPSILON));
        assertTrue(ray.projectPoint(Vector3.createNew(10, 1, 0)).equals(10, 0, 0, DEFAULT_EPSILON));
        assertTrue(ray.projectPoint(Vector3.createNew(-42, 1, 0)).equals(-42, 0, 0, DEFAULT_EPSILON));

        assertTrue(ray.projectPoint(Vector3.createNew(0, -1, 2)).equals(0, 0, 0, DEFAULT_EPSILON));
        assertTrue(ray.projectPoint(Vector3.createNew(1, -1, 4)).equals(1, 0, 0, DEFAULT_EPSILON));
        assertTrue(ray.projectPoint(Vector3.createNew(10, -1, 23)).equals(10, 0, 0, DEFAULT_EPSILON));
        assertTrue(ray.projectPoint(Vector3.createNew(-42, -1, 32)).equals(-42, 0, 0, DEFAULT_EPSILON));

        ray = RayFactory.rayFromTwoPoints(
                Vector3.createNew(0, 0, 0),
                Vector3.createNew(0, 1, 0)
        );

        assertTrue(ray.projectPoint(Vector3.createNew(0, 1, 0)).equals(0, 1, 0, DEFAULT_EPSILON));
        assertTrue(ray.projectPoint(Vector3.createNew(1, 10, 0)).equals(0, 10, 0, DEFAULT_EPSILON));
        assertTrue(ray.projectPoint(Vector3.createNew(10, 1, 0)).equals(0, 1, 0, DEFAULT_EPSILON));
        assertTrue(ray.projectPoint(Vector3.createNew(-42, -41, 0)).equals(0, -41, 0, DEFAULT_EPSILON));
    }
}
