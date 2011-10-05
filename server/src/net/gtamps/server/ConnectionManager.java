package net.gtamps.server;

import net.gtamps.Command;
import net.gtamps.XmlElements;
import net.gtamps.game.GameThread;
import net.gtamps.game.IGameThread;
import net.gtamps.game.player.Player;
import net.gtamps.server.db.DBHandler;

import java.util.concurrent.ConcurrentHashMap;

import org.jdom.Attribute;
import org.jdom.Element;

/**
 * Run gameThread, map connections to players, 
 * link connections to game thread.
 * 
 * @author til
 *
 */
public class ConnectionManager {
	
	
	// ConnectionID --> PlayerUID
	private ConcurrentHashMap<String, Integer> connectionIdentities = new ConcurrentHashMap<String, Integer>();
	
	
	private IGameThread gameThread = null;
	private RequestHandler requestHandler = null;
	private CommandHandler commandHandler = null;
	private DBHandler dbHandler = new  DBHandler("db/net.net.gtamps");
	
	public ConnectionManager(){
		restartGame();
	}
	
	public void restartGame(){
		gameThread = new GameThread();
	}
	
	public boolean connectionIsKnown(String connectionId) {
//		synchronized (connectionIdentities) {
			return this.connectionIdentities.containsKey(connectionId);
//		}
	}
	
	/**
	 * if this is the first connection, start the gameThread.
	 * 
	 * @param id
	 * @param con
	 */
	@Deprecated
	public void addConnection(String id) {
		if (id == null) {
			throw new IllegalArgumentException("'id' must not be null");
		}
//		synchronized (connectionIdentities) {
//			if (connectionIdentities.size() == 0) {
				//gameThread = new GameThread();
//			}
			
//			if (!connectionIdentities.containsKey(id)) {
//			connectionIdentities.putIfAbsent(id, null);
//				connectionIdentities.put(id, null);
//			}
//			int puid = this.gameThread.spawnPlayer("testspieler" + id.hashCode());
//			connectionIdentities.put(id, puid);
//			this.gameThread.joinPlayer(puid);
//		}
		//TODO what? notify a waiting thread?
	}
	
	/**
	 * if this is the last connection, stop the gameThread.
	 * @param id
	 */
	public void removeConnection(String id) {
		if (id == null) {
			throw new IllegalArgumentException("'id' must not be null");
		}
//		synchronized (connectionIdentities) {
			if (this.isIdentifiedConnection(id)) {
				this.leaveGame(id);
			}
			synchronized (this) {
				connectionIdentities.remove(id);
			}
			if (connectionIdentities.size() == 0) {
/*				try {
					this.gameThread.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
*/			}
//		}
	}

	/**
	 * associate a connection with a player name and player id.
	 * have gamethread create a player object.
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	public Element identifyConnection(String id, String name) {
		return this.identifyConnection(id, name, null);
	}
	
	
	/**
	 * associate a connection with a player name and player id.
	 * have gamethread create a player object.
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	public Element identifyConnection(String id, String name, String passw) {
		if (id == null) {
			throw new IllegalArgumentException("'id' must not be null");
		}
		if (name == null) {
			throw new IllegalArgumentException("'name' must not be null");
		}
		if (passw == null) {
			passw = "";
		}
		{
			// TODO return badResponse
			if (name.isEmpty()) {
				return assembleBadResponse(id, RequestTypes.IDENTIFY);
				//throw new IllegalArgumentException("'name' must not be the empty string");
			}
			if (!name.matches("[a-zA-Z0-9]+")) {
				return assembleBadResponse(id, RequestTypes.IDENTIFY);
				//throw new IllegalArgumentException("'name' must be alphanumerical: " + name);
			}
			if (!passw.matches("[a-zA-Z0-9]*")) {
				return assembleBadResponse(id, RequestTypes.IDENTIFY);
				//throw new IllegalArgumentException("'name' must be alphanumerical: " + name);
			}
		}
		if (!dbHandler.hasPlayer(name)) {
			//return assembleNeedResponse(id, RequestTypes.IDENTIFY);
			//FIXME
			registerPlayer(id, name, passw, passw);
		} else if (dbHandler.authPlayer(name, passw) == -1) {
			return assembleBadResponse(id, RequestTypes.IDENTIFY);
		}
		synchronized (this) {
			int uid = this.gameThread.createPlayer(name);
			this.connectionIdentities.put(id, uid);
		}
		return assembleOKResponse(id, RequestTypes.IDENTIFY);
	}
	
	public Element registerPlayer(String id, String name, String passw, String confirmPassw) {
		if (!name.matches("[a-zA-Z0-9]+")) {
			return assembleBadResponse(id, RequestTypes.REGISTER);
			//throw new IllegalArgumentException("'name' must be alphanumerical: " + name);
		}
		if (!passw.matches("[a-zA-Z0-9]*")) {
			return assembleBadResponse(id, RequestTypes.REGISTER);
			//throw new IllegalArgumentException("'name' must be alphanumerical: " + name);
		}
		if (dbHandler.hasPlayer(name)) {
			return assembleBadResponse(id, RequestTypes.REGISTER);
		}
		if (!passw.equals(confirmPassw)) {
			return assembleNeedResponse(id, RequestTypes.REGISTER);
		}
		dbHandler.createPlayer(name, passw);
		return identifyConnection(id, name, passw);
	}
	
	/**
	 * write player element to connection.  connection must be identified
	 * 
	 * @param id
	 * @return 
	 */
	public Element getPlayer(String id) {
		if (id == null) {
			throw new IllegalArgumentException("'id' must not be null");
		}
		if (!isIdentifiedConnection(id)) {
			return assembleNeedResponse(id, RequestTypes.GETPLAYER);
		}
		Integer puid = null; 
//		synchronized (connectionIdentities) {
			puid = connectionIdentities.get(id);
//		}
		assert puid != null : "this should be handled by isIdentifiedConnection()";
		Player p = gameThread.getPlayer(puid);
		assert p != null : "this player should exists for an identifued connection";
		Element response = assembleOKResponse(id, RequestTypes.GETPLAYER);
		response.addContent(p.toXMLElement(0, null));
		return response;
	}
	
	/**
	 * write mapdata document to connection.  connection must be identified
	 * 
	 * @param id
	 * @return 
	 */
	@Deprecated
	public Element getMapData(String id) {
		if (id == null) {
			throw new IllegalArgumentException("'id' must not be null");
		}
		if (!isIdentifiedConnection(id)) {
			return assembleNeedResponse(id, RequestTypes.GETMAPDATA);
		}
		Element mapData = null;
		synchronized (connectionIdentities) {
			mapData = this.gameThread.getMapData();
		}
		if (mapData == null) {
			return assembleBadResponse(id, RequestTypes.GETMAPDATA);
		}
		return assembleOKResponse(id, RequestTypes.GETMAPDATA);
	}
	

	/**
	 * write update document to connection. connection must be identified
	 * 
	 * @param connectionId
	 * @param revisionId
	 * @return 
	 */
	public Element updateToRevision(String connectionId, int revisionId) {
		if (connectionId == null) {
			throw new IllegalArgumentException("'connectionId' must not be null");
		}
		if (!isIdentifiedConnection(connectionId)) {
			return assembleNeedResponse(connectionId, RequestTypes.GETUPDATE);
		}
		if (revisionId < 0) {
			return assembleBadResponse(connectionId, RequestTypes.GETUPDATE);
		}
		Element update = null;
//		synchronized (connectionIdentities) {
			update = this.gameThread.getUpdatesAsXML(revisionId);
//		}
		return update;
	}
	
	/**
	 * rename this! 
	 * connection must be identified.
	 * @return 
	 */
	public Element getCompleteGameStatus(String connectionID) {
		if (connectionID == null) {
			throw new IllegalArgumentException(
					"'connectionID' must not be null");
		}
		if (!isIdentifiedConnection(connectionID)) {
			return assembleNeedResponse(connectionID, RequestTypes.GETUPDATE);
		}
		return updateToRevision(connectionID, 0);
	}
	
	/**
	 * notify gameThread of a player command. connection must be identified
	 * 
	 * @param id
	 * @param command
	 */
	public void command(String id, Command command) {
		if (id == null) {
			throw new IllegalArgumentException("'id' must not be null");
		}
		if (command == null) {
			throw new IllegalArgumentException("'command' must not be null");
		}
		if (!isIdentifiedConnection(id)) {
			return;
		}
		Integer puid = null; 
//		synchronized (connectionIdentities) {
			puid = this.connectionIdentities.get(id);
			assert puid != null : "isIdentifiedConnection() is supposed to take care of this";
			this.gameThread.command(puid, command);
//		}
	}

	/**
	 * put a player into the game. connection must be identified
	 * 
	 * @param id	connection id
	 * @return 
	 */
	public Element joinGame(String id) {
		if (id == null) {
			throw new IllegalArgumentException("'id' must not be null");
		}
		if (!isIdentifiedConnection(id)) {
			return assembleNeedResponse(id, RequestTypes.JOIN);
		}
//		synchronized (connectionIdentities) {
			Integer puid = this.connectionIdentities.get(id);
			assert puid != null : "assertIdentifiedConnection() should prevent this";
			boolean joined = this.gameThread.joinPlayer(puid);
			if (!joined) {
				return assembleBadResponse(id, RequestTypes.JOIN);
			}
//		}
		return assembleOKResponse(id, RequestTypes.JOIN);
	}
	

	/**
	 * remove a player from the game. connection must be identified
	 * 
	 * @param connection id
	 * @return 
	 */
	public Element leaveGame(String id) {
		if (id == null) {
			throw new IllegalArgumentException("'id' must not be null");
		}
		if (!isIdentifiedConnection(id)) {
			return assembleNeedResponse(id, RequestTypes.LEAVE);
		}
//		synchronized (connectionIdentities) {
			Integer puid = this.connectionIdentities.get(id);
			assert puid != null : "assertIdentifiedConnection() should prevent this";
			this.gameThread.leavePlayer(puid);
//		}
		return assembleOKResponse(id, RequestTypes.LEAVE);
	}

	/**
	 * @param requestHandler
	 */
	public void setRequestHandler(RequestHandler requestHandler) {
		if (requestHandler == null) {
			throw new IllegalArgumentException(
					"'requestHandler' must not be null");
		}
		this.requestHandler = requestHandler;
	}

	/**
	 * @param commandHandler
	 */
	public void setCommandHandler(CommandHandler commandHandler) {
		if (commandHandler == null) {
			throw new IllegalArgumentException(
					"'commandHandler' must not be null");
		}
		this.commandHandler = commandHandler;
	}
	
	/**
	 * @param id
	 * @return
	 */
	private boolean isIdentifiedConnection(String id) {
		assert id != null;
		synchronized (this) {
			return connectionIdentities.containsKey(id) && connectionIdentities.get(id) != null;
		}
	}

//	private boolean assertIdentifiedConnection(String connectionId, RequestTypes reqType) {
//		if (!isIdentifiedConnection(connectionId)) {
//			sendResponse(connectionId, reqType.typeString(), reqType.needResponse());
//			return false;
//		} else {
//			return true;
//		}
//	}
	
	private boolean validate() {
		//TODO check if connections are available, identities are exclusive...
		return false;
	}
	
/*	private void sendResponse(String connectionId, String typeStr, String valueStr) {
		Element e = new Element(XmlElements.SINGLE_RESPONSE.tagName());
		e.setAttribute(new Attribute("type", typeStr));
		e.setAttribute(new Attribute("value", valueStr));
		this.sendResponse(connectionId, e);
	}
*/	
	private Element assembleOKResponse(String connectionId, RequestTypes reqtype) {
		return this.assembleResponse(connectionId, reqtype.typeString(), reqtype.okResponse());
	}

	private Element assembleNeedResponse(String connectionId, RequestTypes reqtype) {
		//TODO test hack:
//		int puid = this.gameThread.spawnPlayer("testspieler" + connectionId.hashCode());
//		connectionIdentities.put(connectionId, puid);
//		this.gameThread.joinPlayer(puid);
//		this.identifyConnection(connectionId, "testspieler" + GTAMultiplayerServer.getNextUID());
//		this.joinGame(connectionId);
		return this.assembleResponse(connectionId, reqtype.typeString(), reqtype.needResponse());
	}
	
	private Element assembleBadResponse(String connectionId, RequestTypes reqtype) {
		return this.assembleResponse(connectionId, reqtype.typeString(), reqtype.badResponse());
	}

	/**
	 * @param connectionId
	 * @param typeStr
	 * @param valueStr
	 * @return
	 */
	private Element assembleResponse(String connectionId, String typeStr, String valueStr) {
		Element e = new Element(XmlElements.SINGLE_RESPONSE.tagName());
		e.setAttribute(new Attribute("type", typeStr));
		e.setAttribute(new Attribute("value", valueStr));
		return e;
	}

}
