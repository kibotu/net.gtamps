package net.gtamps.shared.state;

import android.util.Log;

/**
 * This class implements the state manager which handles the state of an Entity.
 */
public abstract class StateManager {

    @SuppressWarnings("unused")
    private static final String TAG = StateManager.class.getSimpleName();
    protected int activeState;
    protected int lastState;
    protected State[] stateTable;

    /**
     * Constructs a state manager.
     *
     * @param activeState
     * @param stateTable
     */
    protected StateManager(int activeState, State[] stateTable) {
        this.activeState = lastState = activeState;
        this.stateTable = stateTable;
    }

    /**
     * the update method lets states perform their default transitions and also
     * performs the carry over of time once a state is left
     *
     * @param dt the time that this state manager shall progress in milliseconds
     */
    public void update(final int dt) {
        int timeLeft = dt;
        try {
            stateTable[activeState].update(dt);
            // perform the default transition until all the time left is used up.
            while (stateTable[activeState].hasExpiredSince() > 0 &&
                    // only perform state transition if the next state would be different from the current state
                    stateTable[activeState].getType() != stateTable[stateTable[activeState].getDefaultTransitionIndex()].getType()) {
                timeLeft -= stateTable[activeState].hasExpiredSince();
                activeState = stateTable[activeState].getDefaultTransitionIndex();
                stateTable[activeState].start(timeLeft);
                // Log.e(TAG, "Default Transistion: "+stateTable[activeState].getStateType());
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, "WRONG STATE TABLE!");
        }
        lastState = activeState;
    }

    /**
     * this method allow to propose a state. the state is accepted if it has a
     * higher priority (meaning higher index in the state table) than the
     * current state
     *
     * @param proposal the proposed state
     * @return true if the state was accepted, false if not.
     */
    public boolean proposeState(final State.Type proposal) {
        // Log.e(TAG, "current State: "+stateTable[activeState].getStateType());
        // Log.e(TAG,
        // "current State expire: "+stateTable[activeState].hasExpiredSince());
        // Log.e(TAG, "State was proposed: "+proposal);
        int proposalIndex = 0;
        // proposal to idle can be skipped (i=1)
        for (int i = 1; proposalIndex < 1 || i < stateTable.length; i++) {
            if (stateTable[i].getType() == proposal) {
                proposalIndex = i;
            }
        }
        if (proposalIndex > activeState) {
            lastState = activeState;
            activeState = proposalIndex;
            stateTable[activeState].start(0);
            // Log.e(TAG,
            // "Accepted State: "+stateTable[activeState].getStateType());
            return true;
        }
        // Log.e(TAG, "proposal denied");
        return false;
    }

    /**
     * returns the active state
     *
     * @return the active state
     */
    public State getActiveState() {
        return stateTable[activeState];
    }

    /**
     * returns the last active state
     *
     * @return the last active state
     */
    public State getLastState() {
        return stateTable[lastState];
    }

    /**
     * resets the state manager in terms of setting the current state to the
     * first state, which is probably the IDLE state
     */
    public void reset() {
        // IDLE is always state number zero
        lastState = activeState;
        activeState = 0;
        stateTable[activeState].start(0);
    }

    /**
     * Sanity check if the state has changed.
     *
     * @return <code>true</code> if it has changed
     */
    public boolean hasChanged() {
        return lastState == activeState;
    }
}
