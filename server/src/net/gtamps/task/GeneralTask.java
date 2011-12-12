package net.gtamps.task;

import net.gtamps.server.Session;

public abstract class GeneralTask<INDATA, OUTDATA> extends Task<INDATA, OUTDATA> {

	public GeneralTask(final int id, final Session session, final INDATA indata) {
		super(id, session, indata);
	}

}
