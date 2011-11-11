package net.gtamps.game;

/**
 * <p>
 * A class to keep track of revision numbers. It allows access to
 * the revision number it considers current and to the number that will
 * follow; it can update these numbers based on interest in a
 * new revision.
 * </p><p>
 * All revision sequences start with the value of {@link #START_REVISION}.
 * </p>
 *
 * @author jan, tom, til
 * @deprecated revisions are now linked to gameTime, which is centrally
 * kept by a {@link TimeKeeper timeKeeper}
 * @see TimeKeeper
 */
@Deprecated
public class RevisionKeeper {
    /**
     * The current revision right after initialization of a new
     * revisionKeeper object. Currently set to {@value #START_REVISION}.
     */
    public static final long START_REVISION = 1l;
    private long currentRevision = START_REVISION;
    private long nextRevision = currentRevision + 1;
    private boolean updateRequired = false;
    private final Object master;

    /**
     * creates a new RevisionKeeper object.
     *
     * @param master the object to serve as the revisionKeeper's master,
     *               which is basically a key or password to some
     *               of the revisionKeeper's methods.
     */
    public RevisionKeeper(final Object master) {
        if (master == null) {
            throw new IllegalArgumentException("'master' must not be null");
        }
        this.master = master;
    }

    /**
     * @return the number of the current revision
     */
    public long getCurrentRevision() {
        return currentRevision;
    }

    /**
     * Return the number of the next revision. This sets the
     * {@link #updateIsRequired()} flag, and after the next call to
     * {@link #updateRevision(Object)}, the value returned by this
     * method will become the current revision.
     *
     * @return the number that will be returned as
     *         {@link RevisionKeeper#getCurrentRevision()} after the next
     *         call to {@link updateRevision(Object)}
     */
    public long getNextRevision() {
        updateRequired = true;
        return nextRevision;
    }

    /**
     * <code>true</code> if the next call to {@link #updateRevision(Object)}
     * will change the {@link #getCurrentRevision() current revision} to a
     * {@link #getNextRevision() new value}.
     *
     * @return    <code>true</code> if the next call to {@link #updateRevision(Object)}
     * will change the current revision to a new value.
     */
    public boolean updateIsRequired() {
        return updateRequired;
    }

    /**
     * Update the {@link #getCurrentRevision() current revision} to a
     * {@link #getNextRevision() new value}, if {@link #updateIsRequired() necessary}.
     *
     * @param master the object that was used to
     *               {@link #RevisionKeeper(Object) initialize} this
     *               RevisionKeeper
     * @throws IllegalArgumentException if <code>master</code> is not the correct object
     * @return the revision number that is now {@link #getCurrentRevision() current}
     */
    public long updateRevision(final Object master) throws IllegalArgumentException {
        if (!master.equals(this.master)) {
            throw new IllegalArgumentException("this is not my master: " + master);
        }
        if (updateRequired) {
            currentRevision = nextRevision;
            nextRevision = currentRevision + 1;
        }
        updateRequired = false;
        return currentRevision;
    }

    @Override
    public String toString() {
        return "[" + currentRevision
                + (updateRequired ? "*" : "") + "]";
    }


}

