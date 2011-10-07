package net.gtamps.shared.communication;

import net.gtamps.shared.communication.ISendable;

public enum Command implements ISendable {
	ACCELERATE, DECELERATE, ENTEREXIT, SHOOT, HANDBRAKE, LEFT, RIGHT;
}
