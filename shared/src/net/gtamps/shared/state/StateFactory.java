package net.gtamps.shared.state;


/**
 * Creates states manager for entity types.
 */
public class StateFactory {

    // static
    private StateFactory() {
    }

    /**
     * Builds a carry state manager.
     *
     * @return state manager
     */
    public static StateManager createCarrier() {

        State[] stateTable = new State[]{
                new State(State.Type.IDLE, 0, 1000),
                new State(State.Type.SPAWN, 0, 300)
        };

        return new StateManager(0, stateTable) {
        };
    }

    /**
     * Builds a unit state manager.
     *
     * @return state manager
     */
    public static StateManager createUnit() {

        State[] stateTable = new State[]{
                new State(State.Type.IDLE, 0, 400),
                new State(State.Type.MOVE, 0, 300),
                new State(State.Type.ATTACK, 0, 300),
                new State(State.Type.STRUCK, 0, 300),
                new State(State.Type.DIE, 0, 300),
                new State(State.Type.DESTROYED, 0, 300),
        };

        return new StateManager(0, stateTable) {
        };
    }
//
//    public static StateManager create(Entity.Type type) {
//
//        StateManager stateManager = null;
//
//        switch (type) {
//            case CARRIER:
//                stateManager = createCarrier();
//                break;
//            case UNIT:
//                stateManager = createUnit();
//                break;
//            case DECORATIVE:
//                stateManager = createDecorative();
//                break;
//        }
//        return stateManager;
//    }

    private static StateManager createDecorative() {

        State[] stateTable = new State[]{
                new State(State.Type.IDLE, 0, 1000),
        };

        return new StateManager(0, stateTable) {
        };
    }
}
