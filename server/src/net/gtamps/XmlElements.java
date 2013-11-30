package net.gtamps;


/**
 * XML Elements that are being used and their tags.
 *
 * @author jan, tom, til
 */
@Deprecated
public enum XmlElements {

    MESSAGE("message", "m"),
    COMMAND("command", "cmd"),
    ACTION("action", "act"),
    REQUESTS("requests", "rqs"),
    SINGLE_REQUEST("request", "rq"),
    RESPONSES("responses", "rs"),
    SINGLE_RESPONSE("response", "r"),
    UPDATE("gamecontent", "gc"),
    ENTITY("go", "go"),
    PROPERTY("property", "p"),
    PLAYER("player", "pl"),
    MAPDATA("map", "md"),
    SINGLE_EVENT("event", "e"),
    EVENTS("events", "ee"),
    ATTRIB_UID("uid", "id"),
    ATTRIB_NAME("name", "n"),
    ATTRIB_TYPE("type", "t"),
    ATTRIB_VALUE("value", "v"),
    ATTRIB_USER("username", "usn"),
    ATTRIB_PASSW("password", "pwd"),
    ATTRIB_REVISION("rev", "rev"),
    ATTRIB_SOURCE("source", "src"),
    ATTRIB_TARGET("target", "tgt");
    //TODO more!

    private final String debugName;
    private final String productionName;

    private XmlElements(final String debugName, final String productionName) {
        assert debugName != null : "'debugName' must not be null";
        assert productionName != null : "'productionName' must not be null";
//		assert isUniqueName(debugName, true) : "debugname is not unique: " + debugName;
//		assert isUniqueName(productionName, false) : "production name is not unique: " + productionName;
        this.debugName = debugName;
        this.productionName = productionName;
    }

    public String tagName() {
        return GTAMultiplayerServer.DEBUG ? debugName : productionName;
    }

    public String debugName() {
        return debugName;
    }

    public String productionName() {
        return productionName;
    }

    private boolean isUniqueName(final String name, final boolean debug) {
        for (final XmlElements e : XmlElements.values()) {
            final String check = debug ? e.debugName : e.productionName;
            if (name.equalsIgnoreCase(check)) {
                return false;
            }
        }
        return true;
    }

}
