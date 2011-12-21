package net.gtamps.task;

import net.gtamps.server.Session;

public abstract class AuthenticationTask<INDATA, OUTDATA> extends Task<INDATA, OUTDATA> {

	public AuthenticationTask(final int id, final Session session, final INDATA indata) {
		super(id, session, indata);
	}

}
