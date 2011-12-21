package net.gtamps.task;

import net.gtamps.server.Session;
import net.gtamps.shared.serializer.communication.Sendable;

/**
 * Something that needs to be done. Intended to be created according to a {@link Sendable}.
 * 
 *
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 *
 */
public abstract class Task<INDATA, OUTDATA> {
	
	protected static enum Outcome {
		
		PENDING, OK, NEED, DECLINED, ERROR;
		
	}
	
	private final int id;
	private final Session session;
	private String message;
	private Outcome outcome = Outcome.PENDING;
	
	protected final INDATA indata;
	protected OUTDATA outdata = null;
	
	public Task(final int id, final Session session, final INDATA indata) {
		if (session == null) {
			throw new IllegalArgumentException("'session' must not be 'null'");
		}
		this.id = id;
		this.session = session;
		this.indata = indata;
	}

	public final int getId() {
		return id;
	}

	public final Session getSession() {
		return session;
	}
	
	public final Outcome getOutcome() {
		return outcome;
	}
	
	public final String getOutcomeMessage() {
		return message;
	}
	
	public final OUTDATA getOutdata() {
		return outdata;
	}
	
	public boolean isPending() {
		return outcome == Outcome.PENDING;
	}
	
	public final void execute() throws IllegalStateException {
		if (!isPending()) {
			throw new IllegalStateException("task was executed already with outcome: " + outcome.toString());
		}
		outcome = executeHook();
		if (isPending()) {
			throw new IllegalStateException("task was executed with invalid outcome: " + outcome.toString());
		}
	}
	
	protected Outcome solvedSuccessfully() {
		ensurePending();
		return Outcome.OK;
	}
	
	protected Outcome requestMoreData() {
		ensurePending();
		return Outcome.NEED;
	}
	
	protected Outcome decline() {
		ensurePending();
		return Outcome.DECLINED;
	}
	
	protected Outcome giveUp() {
		ensurePending();
		return Outcome.ERROR;
	}
	
	protected Outcome solvedSuccessfully(final String message) {
		ensurePending();
		this.message = message;
		return Outcome.OK;
	}
	
	protected Outcome requestMoreData(final String message) {
		ensurePending();
		this.message = message;
		return Outcome.NEED;
	}
	
	protected Outcome decline(final String message) {
		ensurePending();
		this.message = message;
		return Outcome.DECLINED;
	}
	
	protected Outcome giveUp(final String message) {
		ensurePending();
		this.message = message;
		return Outcome.ERROR;
	}

	protected abstract Outcome executeHook();
	
	private void ensurePending() {
		if (!isPending()) {
			throw new IllegalStateException("outcome already set to: " + outcome);
		}
	}

}
