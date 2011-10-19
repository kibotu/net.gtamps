package net.gtamps.server;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.communication.ISerializer;
import net.gtamps.shared.communication.Message;
import net.gtamps.shared.communication.Sendable;
import net.gtamps.shared.communication.SendableType;
import net.gtamps.shared.communication.data.AuthentificationData;
import net.gtamps.shared.communication.data.ISendableData;
import net.gtamps.shared.communication.data.RevisionData;
import net.gtamps.shared.communication.data.StringData;
import net.gtamps.shared.communication.data.UpdateData;
import net.gtamps.shared.game.IntProperty;
import net.gtamps.shared.game.Propertay;
import net.gtamps.shared.game.entity.Entity;

import org.jetbrains.annotations.NotNull;


public class ManualTypeSerializer implements ISerializer {
	private static final LogType TAG = LogType.SERVER;
	
	public static String DELIMITER = " ";
	
	public static String MESSAGE = "M";
	public static String SENDABLE = "SEND";
	public static String STATUS_OK = "OK";
	public static String STATUS_NEED = "NEED";
	public static String STATUS_BAD = "BAD";
	public static String STATUS_ERROR = "ERROR";
	public static String LOGIN = "LOGIN";
	public static String REGISTER = "REGISTER";
	public static String SESSION = "SESSION";
	public static String JOIN = "JOIN";
	public static String LEAVE = "LEAVE";
	public static String UPDATE = "GETUPDATE";
	public static String GETMAPDATA = "GETMAPDATA";
	public static String GETPLAYER  = "GETPLAYER";
	public static String ENTITY = "ENTITY";
	public static String PROPERTY = "PROP";
	public static String INTEGER = "INT";
	public static String BAD_SENDABLE = "BAD SENDABLE";
	public static String BAD_MESSAGE = "BAD MESSAGE";

    @Override
    public byte[] serializeMessage(@NotNull Message message) {
    	Logger.getInstance().log(TAG, "serializing message: " + message);
    	StringBuilder bld = new StringBuilder();
    	addToken(bld, MESSAGE);
    	String sessId = message.getSessionId(); 
    	addToken(bld, (sessId==null || sessId.length()==0) ? "" : sessId);
    	for (Sendable s : message.sendables) {
    		serializeSendable(bld, s);
    	}
    	return bld.toString().getBytes();
    }
    
    private void addToken(StringBuilder bld, String token) {
    	bld.append(token + DELIMITER);
    }

    private void serializeSendable(StringBuilder bld, Sendable s) {
    	addToken(bld, SENDABLE);
    	addToken(bld, Integer.toString(s.id));
    	switch(s.type) {
	    	case ACCELERATE:
	    	case DECELERATE:
	    	case ENTEREXIT:
	    	case HANDBRAKE:
	    	case LEFT:
	    	case RIGHT:
	    	case SHOOT:
	    		addToken(bld,  s.type.name());
	    		break;
	    		
	    	case GETMAPDATA_BAD: addToken(bld, GETMAPDATA); addToken(bld, STATUS_BAD); break;
	    	case GETMAPDATA_ERROR: addToken(bld, GETMAPDATA); addToken(bld, STATUS_ERROR); break;
	    	case GETMAPDATA_NEED: addToken(bld, GETMAPDATA); addToken(bld, STATUS_NEED); break;
	    	case GETMAPDATA_OK: addToken(bld, GETMAPDATA); addToken(bld, STATUS_OK); break;
	    	case GETMAPDATA: addToken(bld, GETMAPDATA); break;
	    		
	    	case GETPLAYER: addToken(bld, GETPLAYER); break;
	    	case GETPLAYER_BAD: addToken(bld, GETPLAYER); addToken(bld, STATUS_BAD); break;
	    	case GETPLAYER_ERROR: addToken(bld, GETPLAYER); addToken(bld, STATUS_ERROR); break;
	    	case GETPLAYER_NEED: addToken(bld, GETPLAYER); addToken(bld, STATUS_NEED); break;
	    	case GETPLAYER_OK:  addToken(bld, GETPLAYER); addToken(bld, STATUS_OK); break;
	    		
	    	case GETUPDATE: 
	    		addToken(bld, UPDATE); 
	    		addToken(bld, Long.toString( ((RevisionData)s.data).revisionId));
	    		break;
	    	case GETUPDATE_BAD:addToken(bld, UPDATE); addToken(bld, STATUS_BAD); break;
	    	case GETUPDATE_ERROR: addToken(bld, UPDATE); addToken(bld, STATUS_ERROR); break;
	    	case GETUPDATE_NEED: addToken(bld, UPDATE); addToken(bld, STATUS_NEED); break;
	    	case GETUPDATE_OK: addToken(bld, UPDATE); addToken(bld, STATUS_OK);
	    		UpdateData udata = (UpdateData) s.data;
	    		addToken(bld, Long.toString(udata.revId));
	    		serializeUpdateData(bld, udata);
	    		break;
	    		
	    	case JOIN: addToken(bld, JOIN); break;
	    	case JOIN_BAD:addToken(bld, JOIN); addToken(bld, STATUS_BAD); break;
	    	case JOIN_ERROR: addToken(bld, JOIN); addToken(bld, STATUS_ERROR); break;
	    	case JOIN_NEED: addToken(bld, JOIN); addToken(bld, STATUS_NEED); break;
	    	case JOIN_OK: addToken(bld, JOIN); addToken(bld, STATUS_OK); break;
	    		
	    	case LEAVE: addToken(bld, LEAVE); break;
	    	case LEAVE_BAD:addToken(bld, LEAVE); addToken(bld, STATUS_BAD); break;
	    	case LEAVE_ERROR: addToken(bld, LEAVE); addToken(bld, STATUS_ERROR); break;
	    	case LEAVE_NEED: addToken(bld, LEAVE); addToken(bld, STATUS_NEED); break;
    		case LEAVE_OK: addToken(bld, LEAVE); addToken(bld, STATUS_OK); break;
    			
    		case LOGIN:  
    			addToken(bld, LOGIN);
    			AuthentificationData data = (AuthentificationData) s.data;
    			addToken(bld, data.username);
    			addToken(bld, data.password);
    			break;
    		case LOGIN_BAD:addToken(bld, LOGIN); addToken(bld, STATUS_BAD); break;
    		case LOGIN_ERROR: addToken(bld, LOGIN); addToken(bld, STATUS_ERROR); break;
    		case LOGIN_NEED: addToken(bld, LOGIN); addToken(bld, STATUS_NEED); break;
    		case LOGIN_OK: addToken(bld, LOGIN); addToken(bld, STATUS_OK); break;
    			
    		case REGISTER: 
    			addToken(bld, REGISTER);
    			AuthentificationData adata = (AuthentificationData) s.data;
    			addToken(bld, adata.username);
    			addToken(bld, adata.password);
    			break;
    		case REGISTER_BAD:addToken(bld, REGISTER); addToken(bld, STATUS_BAD); break;
    		case REGISTER_ERROR: addToken(bld, REGISTER); addToken(bld, STATUS_ERROR); break;
    		case REGISTER_NEED: addToken(bld, REGISTER); addToken(bld, STATUS_NEED); break;
    		case REGISTER_OK: addToken(bld, REGISTER); addToken(bld, STATUS_OK); break;
    			
    		case SESSION: addToken(bld, SESSION); break;
    		case SESSION_BAD:addToken(bld, SESSION); addToken(bld, STATUS_BAD); break;
    		case SESSION_ERROR: addToken(bld, SESSION); addToken(bld, STATUS_ERROR); break;
    		case SESSION_NEED: addToken(bld, SESSION); addToken(bld, STATUS_NEED); break;
    		case SESSION_OK: 
    			addToken(bld, SESSION); 
    			addToken(bld, STATUS_OK);
    			StringData sdata = (StringData) s.data;
    			addToken(bld, sdata.value);
    			break;
    			
    		default:
    			addToken(bld, BAD_SENDABLE);
    			break;
    	}
    }
    
    private void serializeUpdateData(StringBuilder bld, UpdateData udata) {
    	for (Entity e : udata.entites) {
    		addToken(bld, ENTITY);
    		addToken(bld, Integer.toString(e.getUid()));
    		addToken(bld, e.getName());
    		for (Propertay<?> p : e.getAllProperties()) {
    			serializeProperty(bld, p);
    		}
    	}
    }
    
    private void serializeProperty(StringBuilder bld, Propertay<?> p) {
    	if (p instanceof IntProperty) {
    		IntProperty ip = (IntProperty)p;
    		addToken(bld, PROPERTY);
    		addToken(bld, INTEGER);
    		addToken(bld, ip.getName());
    		addToken(bld, Integer.toString(ip.value()));
    	} else {
    		// error
    	}
    }
    
    @Override
    public Message deserializeMessage(@NotNull byte[] bytes) {
    	String msgString = new String(bytes);
    	Logger.getInstance().log(TAG, "deserializing message: " + msgString);
    	Message m = new Message();
    	Scanner scanner = new Scanner(msgString);
    	scanner.next(MESSAGE);
    	String sessId = scanner.next();
    	m.setSessionId(sessId);
    	while(scanner.hasNext(SENDABLE)) {
    		scanner.next();
    		Sendable s = getSendable(scanner);
    		s.sessionId = sessId;
    		m.addSendable(s);
    	}
    	Logger.getInstance().log(TAG, "finished deserializing message: " + m);
    	return m;
    }
    
    private Sendable getSendable(Scanner scanner) {
    	Sendable s = null;
    	int id = scanner.nextInt();
    	try {
	    	String type = scanner.next();
	    	if (type.equals(LOGIN)) {
	    		s = getLogin(scanner, id);
	    	} else if (type.equals(REGISTER)) {
	    		s = getRegister(scanner, id);
	    	} else if (type.equals(SESSION)) {
	    		s = getSession(scanner, id);
	    	} else if (type.equals(JOIN)) {
	    		s = getJoin(scanner, id);
	    	} else if (type.equals(LEAVE)) {
	    		s = getLeave(scanner, id);
	    	} else if (type.equals(UPDATE)) {
	    		s = getUpdate(scanner, id);
	    	}
    	} catch (NoSuchElementException nse) {
    		s = new Sendable(SendableType.BAD_SENDABLE, id, null);
    	}
    	return s;
    }

	private Sendable getLogin(Scanner scanner, int id) {
		ISendableData data = null;
		SendableType type = null;
		if (scanner.hasNext(STATUS_OK)) {
			scanner.next();
			type = SendableType.LOGIN_OK;
		} else if (scanner.hasNext(STATUS_NEED)) {
			type = SendableType.LOGIN_NEED;
		} else if (scanner.hasNext(STATUS_BAD)) {
			type = SendableType.LOGIN_BAD;
		} else if (scanner.hasNext(STATUS_ERROR)) {
			type = SendableType.LOGIN_ERROR;
		} else {
			type = SendableType.LOGIN;
			data = getAuthData(scanner);
		}
		return new Sendable(type, id, data);
	}
    
	private Sendable getRegister(Scanner scanner, int id) {
		ISendableData data = null;
		SendableType type = null;
		if (scanner.hasNext(STATUS_OK)) {
			scanner.next();
			type = SendableType.REGISTER_OK;
		} else if (scanner.hasNext(STATUS_NEED)) {
			type = SendableType.REGISTER_NEED;
		} else if (scanner.hasNext(STATUS_BAD)) {
			type = SendableType.REGISTER_BAD;
		} else if (scanner.hasNext(STATUS_ERROR)) {
			type = SendableType.REGISTER_ERROR;
		} else {
			type = SendableType.REGISTER;
			data = getAuthData(scanner);
		}
		return new Sendable(type, id, data);
	}
    
	private Sendable getSession(Scanner scanner, int id) {
		Sendable s = null;
		SendableType type = null;
		if (scanner.hasNext(STATUS_OK)) {
			scanner.next();
			type = SendableType.SESSION_OK;
		} else if (scanner.hasNext(STATUS_NEED)) {
			type = SendableType.SESSION_NEED;
		} else if (scanner.hasNext(STATUS_BAD)) {
			type = SendableType.SESSION_BAD;
		} else if (scanner.hasNext(STATUS_ERROR)) {
			type = SendableType.SESSION_ERROR;
		} else {
			type = SendableType.SESSION;
		}
		return new Sendable(type, id, null);
	}
	
	private Sendable getJoin(Scanner scanner, int id) {
		SendableType type = null;
		ISendableData data = null;
		if (scanner.hasNext(STATUS_OK)) {
			scanner.next();
			type = SendableType.JOIN_OK;
		} else if (scanner.hasNext(STATUS_NEED)) {
			type = SendableType.JOIN_NEED;
		} else if (scanner.hasNext(STATUS_BAD)) {
			type = SendableType.JOIN_BAD;
		} else if (scanner.hasNext(STATUS_ERROR)) {
			type = SendableType.JOIN_ERROR;
		} else {
			type = SendableType.JOIN;
		}
		return new Sendable(type, id, data);
	}
	
	private Sendable getLeave(Scanner scanner, int id) {
		Sendable s = null;
		SendableType type = null;
		if (scanner.hasNext(STATUS_OK)) {
			scanner.next();
			type = SendableType.LEAVE_OK;
		} else if (scanner.hasNext(STATUS_NEED)) {
			type = SendableType.LEAVE_NEED;
		} else if (scanner.hasNext(STATUS_BAD)) {
			type = SendableType.LEAVE_BAD;
		} else if (scanner.hasNext(STATUS_ERROR)) {
			type = SendableType.LEAVE_ERROR;
		} else {
			type = SendableType.LEAVE;
		}
		return new Sendable(type, id, null);
	}
	
	private Sendable getUpdate(Scanner scanner, int id) {
		ISendableData data = null;
		SendableType type = null;
		if (scanner.hasNext(STATUS_OK)) {
			scanner.next();
			type = SendableType.GETUPDATE_OK;
			data = getUpdateData(scanner);
		} else if (scanner.hasNext(STATUS_NEED)) {
			type = SendableType.GETUPDATE_NEED;
		} else if (scanner.hasNext(STATUS_BAD)) {
			type = SendableType.GETUPDATE_BAD;
		} else if (scanner.hasNext(STATUS_ERROR)) {
			type = SendableType.GETUPDATE_ERROR;
		} else {
			type = SendableType.GETUPDATE;
			data = getRevisionData(scanner);
		}
		return new Sendable(type, id, data);
	}
	
	private ISendableData getAuthData(Scanner scanner) {
		String name = scanner.next();
		String pw = scanner.next();
		AuthentificationData data = new AuthentificationData(name, pw);
		return data;
	}
	
	private ISendableData getRevisionData(Scanner scanner) {
		long revId = scanner.nextLong();
		RevisionData data = new RevisionData(revId);
		return data;
	}
	
	private ISendableData getUpdateData(Scanner scanner) {
		long revId = scanner.nextLong();
		UpdateData data = new UpdateData(revId);
		Entity entity = getEntity(scanner);
		ArrayList<Entity> entities = new ArrayList<Entity>();
		while (entity != null) {
			entities.add(entity);
			entity = getEntity(scanner);
		}
		data.entites = entities;
		return data;
	}

	private Entity getEntity(Scanner scanner) {
		return null;
	}
	
	private Propertay getProperty(Scanner scanner) {
		return null;
	}
	

}
