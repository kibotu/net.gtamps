package net.gtamps.server;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.communication.ISerializer;
import net.gtamps.shared.communication.Message;
import net.gtamps.shared.communication.MessageDeserializationException;
import net.gtamps.shared.communication.Sendable;
import net.gtamps.shared.communication.SendableDeserializationException;
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


/**
 * $message 	= M $sessionId ($sendable)*
 * $sessionId 	= _String_
 * $sendable 	= SEND $sendId $sendType ($sendData)?
 * $sendId 		= _Integer_
 * $sendType 	= _SendableType.name()_
 * $sendData 	= $AuthData | $StringData | $RevData | $UpdateData
 * $AuthData 	= _String_ _String_
 * $StringData 	= _String_
 * $RevData		= $revisionId
 * $UpdateData 	= $revisionId ($entity)*
 * $revisionId 	= _Long_
 * $entity 		= ENTITY $uid $name ($property)*
 * $uid			= _Integer_
 * $name		= _String_
 * $property	= $intProp
 * $intProp		= PROP INT $name _Integer_
 * 
 *
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 *
 */
public class ManualTypeSerializer implements ISerializer {
	private static final LogType TAG = LogType.SERVER;
	
	public static String DELIMITER = " ";
	
	public static String MESSAGE = "M";
	public static String SENDABLE = "SEND";
	public static String ENTITY = "ENTITY";
	public static String PROPERTY = "PROP";
	public static String INTEGER = "INT";

    @Override
    public byte[] serializeMessage(@NotNull final Message message) {
    	Logger.getInstance().log(TAG, "serializing message: " + message);
    	final StringBuilder bld = new StringBuilder();
    	addToken(bld, MESSAGE);
    	final String sessId = message.getSessionId();
    	addToken(bld, (sessId==null || sessId.length()==0) ? "" : sessId);
    	for (final Sendable s : message.sendables) {
    		serializeSendable(bld, s);
    	}
    	return bld.toString().getBytes();
    }
    
    private void addToken(final StringBuilder bld, final String token) {
    	bld.append(token + DELIMITER);
    }

    private void serializeSendable(final StringBuilder bld, final Sendable s) {
    	addToken(bld, SENDABLE);
    	addToken(bld, Integer.toString(s.id));
    	addToken(bld, s.type.name());
    	switch(s.type) {
	    	case GETUPDATE:
	    		addToken(bld, Long.toString( ((RevisionData)s.data).revisionId));
	    		break;

	    	case GETUPDATE_OK:
	    		final UpdateData udata = (UpdateData) s.data;
	    		addToken(bld, Long.toString(udata.revId));
	    		serializeUpdateData(bld, udata);
	    		break;
	    		
    		case LOGIN:
    			final AuthentificationData data = (AuthentificationData) s.data;
    			addToken(bld, data.username);
    			addToken(bld, data.password);
    			break;
    			
    		case REGISTER:
    			final AuthentificationData adata = (AuthentificationData) s.data;
    			addToken(bld, adata.username);
    			addToken(bld, adata.password);
    			break;
    			
    		case SESSION_OK:
    		case BAD_SENDABLE:
    			final StringData sdata = (StringData) s.data;
    			if (sdata != null) {
    				addToken(bld, sdata.value);
    			}
    			break;
    		default:
    		break;
    	}
    }
    
    private void serializeUpdateData(final StringBuilder bld, final UpdateData udata) {
    	for (final Entity e : udata.entites) {
    		addToken(bld, ENTITY);
    		addToken(bld, Integer.toString(e.getUid()));
    		addToken(bld, e.getName());
    		for (final Propertay<?> p : e.getAllProperties()) {
    			serializeProperty(bld, p);
    		}
    	}
    }
    
    private void serializeProperty(final StringBuilder bld, final Propertay<?> p) {
    	if (p instanceof IntProperty) {
    		final IntProperty ip = (IntProperty)p;
    		addToken(bld, PROPERTY);
    		addToken(bld, INTEGER);
    		addToken(bld, ip.getName());
    		addToken(bld, Integer.toString(ip.value()));
    	} else {
    		// error
    	}
    }
    
    @Override
    public Message deserializeMessage(@NotNull final byte[] bytes)
    throws MessageDeserializationException {
    	final String msgString = new String(bytes);
    	Logger.getInstance().log(TAG, "deserializing message: " + msgString);
    	final Message m = new Message();
    	final Scanner scanner = new Scanner(msgString);
    	try {
	    	scanner.next(MESSAGE);
	    	final String sessId = scanner.next();
	    	m.setSessionId(sessId);
	    	while(scanner.hasNext(SENDABLE)) {
	    		scanner.next();
	    		final Sendable s = getSendable(scanner);
	    		s.sessionId = sessId;
	    		m.addSendable(s);
	    	}
	    	Logger.getInstance().log(TAG, "finished deserializing message: " + m);
    	} catch (final InputMismatchException e) {
    		throw new MessageDeserializationException(e);
    	} catch (final NoSuchElementException e) {
    		throw new MessageDeserializationException(e);
    	} catch (final SendableDeserializationException e) {
    		Logger.getInstance().log(TAG, "MessageDeserializationException");
    		throw new MessageDeserializationException(e);
    	}
    	return m;
    }
    
    private Sendable getSendable(final Scanner scanner) {
    	Sendable s = null;
    	Integer id = null;
    	SendableType type = null;
    	ISendableData data = null;
    	try {
	    	try {
	   			id = scanner.nextInt();
		    	final String typeString = scanner.next();
		    	type = SendableType.valueOf(typeString);
		    	switch(type) {
			    	case ACCELERATE:
			    	case DECELERATE:
			    	case ENTEREXIT:
			    	case HANDBRAKE:
			    	case LEFT:
			    	case RIGHT:
			    	case SHOOT:
			    		break;
			    	case REGISTER:
			    	case LOGIN:
			    		data = getAuthData(scanner);
			    		break;
			    	case SESSION_OK:
			    		data = getStringData(scanner);
			    		break;
			    	case GETUPDATE:
			    		data = getRevisionData(scanner);
			    		break;
			    	case GETUPDATE_OK:
			    		data = getUpdateData(scanner);
			    		break;
		    	}
		    	s = new Sendable(type, id, data);
	    	} catch (final InputMismatchException e) {
	    		throw new SendableDeserializationException(e.getMessage(), e);
	    	} catch (final NoSuchElementException e) {
	    		throw new SendableDeserializationException(e.getMessage(), e);
	    	} catch (final IllegalArgumentException e) {
	    		throw new SendableDeserializationException(e.getMessage(), e);
	    	}
    	} catch (final SendableDeserializationException e) {
    		id = (id == null) ? -1 : id;
    		s = new Sendable(SendableType.BAD_SENDABLE, id, null);
    		s.data = new StringData(e.getMessage());
    		Logger.getInstance().log(TAG, e.getCause().toString());
    		e.getCause().printStackTrace();
    		proceedToNext(scanner, SENDABLE);
    	}
    	return s;
    }
    
    private void proceedToNext(final Scanner scanner, final String pattern) {
    	while (scanner.hasNext() && ! scanner.hasNext(pattern)) {
    		scanner.next();
    	}
    }
    
	private ISendableData getAuthData(final Scanner scanner) {
		final String name = scanner.next();
		final String pw = scanner.next();
		final AuthentificationData data = new AuthentificationData(name, pw);
		return data;
	}
	
	private ISendableData getStringData(final Scanner scanner) {
		final StringData data = new StringData(scanner.next());
		return data;
	}
	
	private ISendableData getRevisionData(final Scanner scanner) {
		final long revId = scanner.nextLong();
		final RevisionData data = new RevisionData(revId);
		return data;
	}
	
	private ISendableData getUpdateData(final Scanner scanner) {
		final long revId = scanner.nextLong();
		final UpdateData data = new UpdateData(revId);
		Entity entity = getEntity(scanner);
		final ArrayList<Entity> entities = new ArrayList<Entity>();
		while (entity != null) {
			entities.add(entity);
			entity = getEntity(scanner);
		}
		data.entites = entities;
		return data;
	}

	private Entity getEntity(final Scanner scanner) {
		//TODO
		return null;
	}
	
	private Propertay getProperty(final Scanner scanner) {
		//TODO
		return null;
	}
	

}
