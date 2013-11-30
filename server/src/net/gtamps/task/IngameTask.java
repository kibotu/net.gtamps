package net.gtamps.task;

import net.gtamps.server.Session;

public abstract class IngameTask<INDATA, OUTDATA> extends Task<INDATA, OUTDATA> {

	public IngameTask(final int id, final Session session, final INDATA indata) {
		super(id, session, indata);
	}
	
}
