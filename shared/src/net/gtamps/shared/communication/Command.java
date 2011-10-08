package net.gtamps.shared.communication;


import net.gtamps.shared.communication.ISendable;

public class Command implements ISendable {

    public enum Type {
        ACCELERATE, DECELERATE, ENTEREXIT, SHOOT, HANDBRAKE, LEFT, RIGHT;
    }

    public final Type type;
    public final int percent;

    public Command (Type type, int percent) {
        this.type = type;
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "Command{" +
                "type=" + type +
                ", percent=" + percent +
                '}';
    }
}
