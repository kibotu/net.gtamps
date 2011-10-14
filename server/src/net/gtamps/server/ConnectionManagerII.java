package net.gtamps.server;

import java.util.ArrayList;

import net.gtamps.game.GameThread;
import net.gtamps.game.IGameThread;
import net.gtamps.shared.communication.Command;
import net.gtamps.shared.communication.Request;
import net.gtamps.shared.communication.Response;
import net.gtamps.shared.communication.RevisionData;
import net.gtamps.shared.communication.UpdateData;
import net.gtamps.shared.game.entity.Entity;

/**
 * Run gameThread, map connections to players, 
 * link connections to game thread.
 * 
 * @author til
 *
 */
public class ConnectionManagerII implements ICommandHandler, IRequestHandler {
	
	
	// ConnectionID --> PlayerUID
//	private ConcurrentHashMap<String, Integer> connectionIdentities = new ConcurrentHashMap<String, Integer>();
	
	
	private IGameThread gameThread = null;
	private IResponseHandler responseHandler;
	
	private int playerUid = 0;
	
	public ConnectionManagerII(IResponseHandler responseHandler){
		if (responseHandler == null) {
			throw new IllegalArgumentException(
					"'responseHandler' must not be null");
		}
		this.responseHandler = responseHandler;
		restartGame();
	}
	
	public void restartGame(){
		if (gameThread != null) {
			gameThread.hardstop();
		}
		gameThread = new GameThread();
		playerUid = gameThread.createPlayer("test");
		gameThread.joinPlayer(playerUid);
	}

	@Override
	public void handleRequest(Session s, Request r) {
		if (r.type.equals(r.type.GETUPDATE)) {
			Response resp = new Response(Response.Status.OK, r);
			long revid = 0;
			ArrayList<Entity> updates = new ArrayList<Entity>();
			for (Entity e : gameThread.getUpdates(((RevisionData)(r.getData())).revisionId)) {
				updates.add(e);
				revid = Math.max(revid, e.getRevision());
			}
			UpdateData data = new UpdateData(revid);
			data.entites = updates;
			resp.setData(data);
			responseHandler.handleResponse(s, resp);
		}
		
	}

	@Override
	public void handleCommand(Session s, Command c) {
		gameThread.command(playerUid, c);
	}
	

}
