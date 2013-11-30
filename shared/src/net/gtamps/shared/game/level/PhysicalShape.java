package net.gtamps.shared.game.level;

import net.gtamps.shared.Utils.math.Vector3;

import java.util.LinkedList;

/**
 * A List of all edges of the physical object in CW or CCW order
 * First and last points have to be connected by the physic engine if necessary
 *
 * @author tom
 */
public class PhysicalShape extends LinkedList<Vector3> {

    private static final long serialVersionUID = 2215716382681624371L;
}
