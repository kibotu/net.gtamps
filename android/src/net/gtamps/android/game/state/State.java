package net.gtamps.android.game.state;

/**
 * This enum represents a state inside the state manager
 */
public class State {

    /**
     * Pre-defines valid state values.
     */
    public enum Type {
        IDLE, MOVE, ATTACK, STRUCK, DIE, DESTROYED, SPAWN
    }

    /**
     * Holds the current state type.
     */
    private Type type;

    /**
     * Holds the current time of the state which has been past.
     */
    private int stateCurrentTime;

    /**
     * Defines how long the state last.
     */
    private final int stateDuration;

    /**
     * Defines the default value of a state.
     */
    private final int defaultTransitionIndex;

    /**
     * Iterates how often a state has been active.
     */
    private int counter;

    /**
     * Creates a new State.
     *
     * @param type     has to be one of the provided stateIDs, e.g. State.Type.IDLE
     * @param duration specifies the duration of this very state.
     */
    public State(final Type type, final int defaultTransitionIndex, final int duration) {
        this.type = type;
        stateDuration = duration;
        stateCurrentTime = 0;
        counter = 0;
        this.defaultTransitionIndex = defaultTransitionIndex;
    }

    /**
     * Starts a State.
     *
     * @param startOffsetTime Sets the currentTime to the desired offset. Normally you can just pass 0 as argument to start the state at its beginning.
     */
    public void start(final int startOffsetTime) {
        // duration of the state is reset, so that it can be incremented on every update.
        stateCurrentTime = 0;
        counter++;
    }

    /**
     * updates a State.
     *
     * @param dt increments the state's currentTime by dt.
     */
    public void update(final int dt) {
        stateCurrentTime += dt;
    }

    /**
     * Calculates the percentage the transition between states
     *
     * @return float between 0 and 1 ... except the state is overdue, then it could be more.
     */
    public float getPercentage() {
        return (float) stateCurrentTime / (float) stateDuration;
    }

    /**
     * Determines whether a state has expired
     *
     * @return either a negative number: meaning the state has not expired, but
     *         will in n milliseconds
     *         or a positive number: which tells you for
     *         how many milliseconds the state has expired.
     */
    public int hasExpiredSince() {
        return stateCurrentTime - stateDuration;
    }

    /**
     * Gets current time.
     *
     * @return current state time
     */
    public int getCurrentTime() {
        return stateCurrentTime;
    }

    /**
     * Gets state duration.
     *
     * @return state duration
     */
    public int getDuration() {
        return stateDuration;
    }

    /**
     * Returns the StateID.
     *
     * @return The stateID
     */
    public Type getType() {
        return type;
    }

    /**
     * returns the default transition index
     *
     * @return the default transition index
     */
    public int getDefaultTransitionIndex() {
        return defaultTransitionIndex;
    }

    /**
     * Returns how many times the state was active. This will help implementing
     * everything that repeatedly uses the same state, because each state now
     * can tell for how long it was running.
     *
     * @return times the state was active
     */
    public int getCounter() {
        return counter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;

        State state = (State) o;

        if (counter != state.counter) return false;
        if (defaultTransitionIndex != state.defaultTransitionIndex) return false;
        if (stateCurrentTime != state.stateCurrentTime) return false;
        if (stateDuration != state.stateDuration) return false;
        if (type != state.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + stateCurrentTime;
        result = 31 * result + stateDuration;
        result = 31 * result + defaultTransitionIndex;
        result = 31 * result + counter;
        return result;
    }
}
