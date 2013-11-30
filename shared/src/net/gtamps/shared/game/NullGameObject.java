package net.gtamps.shared.game;

/**
 * An empty dummy object to be used when <code>null</code> is not allowed.
 *
 * @author Tom Wallroth, Jan Rabe, Til Boerner
 */
public final class NullGameObject extends GameObject {

    public static final NullGameObject DUMMY = new NullGameObject("DUMMY");

    private NullGameObject(final String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }

}
