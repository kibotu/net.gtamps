package net.gtamps.android.core.net;

import java.util.NoSuchElementException;

import net.gtamps.android.core.sound.SoundEngine;
import net.gtamps.android.game.content.EntityView;
import net.gtamps.android.renderer.Registry;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.GameobjectStore;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.player.Player;
import net.gtamps.shared.game.score.Score;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.communication.NewMessageFactory;
import net.gtamps.shared.serializer.communication.NewSendable;
import net.gtamps.shared.serializer.communication.SendableCacheFactory;
import net.gtamps.shared.serializer.communication.SendableFactory;
import net.gtamps.shared.serializer.communication.StringConstants;
import net.gtamps.shared.serializer.communication.data.DataMap;
import net.gtamps.shared.serializer.communication.data.ListNode;
import net.gtamps.shared.serializer.communication.data.SendableDataConverter;
import net.gtamps.shared.serializer.communication.data.Value;

import org.jetbrains.annotations.NotNull;

public class MessageHandler {

	private SendableFactory sendableFactory = new SendableFactory(new SendableCacheFactory());
	private GameobjectStore store = new GameobjectStore();
    private ConnectionManager connection;
    private IWorld world;

    public MessageHandler(ConnectionManager connection, IWorld world2) {
        this.connection = connection;
        this.world = world2;
    }

    public void handleMessage(NewSendable sendable, NewMessage message) {

//        Logger.i(this, "Handles message.");
//        Logger.i(this, sendable);

        switch (sendable.type) {
            case GETUPDATE_OK:
                // empty
                if (sendable.data == null)
                    break;

                // update revision id
                DataMap updateData = (DataMap) sendable.data;
                connection.currentRevId = ((Value<Long>)updateData.get(StringConstants.UPDATE_REVISION)).get();

                // parse all transmitted entities
                ListNode<DataMap> entities = ((ListNode<DataMap>)updateData.get(StringConstants.UPDATE_ENTITIES));
                for (DataMap emap: entities) {
                	int uid = SendableDataConverter.getGameObjectUid(emap);
                	Entity e = store.getEntity(uid);
                	SendableDataConverter.updateGameobject(e, emap);
                	updateOrCreateEntity(e);
                }

                // events
                ListNode<DataMap> events = ((ListNode<DataMap>)updateData.get(StringConstants.UPDATE_GAMEEVENTS));
                for (DataMap emap: events) {
                	int uid = SendableDataConverter.getGameObjectUid(emap);
                	GameEvent e = store.getGameEvent(uid);
                	SendableDataConverter.updateGameobject(e, emap);
                	handleEvent(e);
                }

                // scores
                ListNode<DataMap> scores = ((ListNode<DataMap>)updateData.get(StringConstants.UPDATE_SCORES));
                for (DataMap emap: scores) {
                	int uid = SendableDataConverter.getGameObjectUid(emap);
                	Score score = store.getScore(uid);
                	SendableDataConverter.updateGameobject(score, emap);
                	if (belongsToActivePlayer(score)) {
                		world.setPlayerFragScore(score.getCount());
                	}
                }
                break;

            case GETUPDATE_NEED:
                break;
            case GETUPDATE_BAD:
                break;
            case GETUPDATE_ERROR:
                Logger.toast(this, "GETUPDATE_ERROR");
                break;

            case GETPLAYER_OK:

                // empty
                if (sendable.data == null)
                    break;

                // not player data
                DataMap pmap = (DataMap) ((DataMap) sendable.data).get(StringConstants.PLAYER_DATA);
                
                Player player = store.getPlayer(SendableDataConverter.getGameObjectUid(pmap));
                SendableDataConverter.updateGameobject(player, pmap);
                
                world.playerManager.setActivePlayer(player);

                updateOrCreateEntity(store.getEntity(player.getEntityUid()));

                Logger.D(this, "GETPLAYER_OK " + player);

                // get update
                connection.add(NewMessageFactory.createGetUpdateRequest(connection.currentRevId));
                break;

            case GETPLAYER_NEED:
                break;
            case GETPLAYER_BAD:
                break;
            case GETPLAYER_ERROR:
                Logger.toast(this, "GETPLAYER_ERROR");
                break;

            case SESSION_OK:
                connection.currentSessionId = message.getSessionId();
                //FIXME workaround for presentation
                if(android.os.Build.VERSION.SDK_INT>8){
                	connection.add(NewMessageFactory.createLoginRequest(Config.DEFAULT_USERNAME_TIL, Config.DEFAULT_PASSWORD_TIL));
                } else {
                	connection.add(NewMessageFactory.createLoginRequest(Config.DEFAULT_USERNAME, Config.DEFAULT_PASSWORD));
                }
                break;
            case SESSION_NEED:
                break;
            case SESSION_BAD:
                break;
            case SESSION_ERROR:
                Logger.toast(this, "SESSION_ERROR");
                break;

            case JOIN_OK:
            	if(world.supports2DTileMap()){
                	Logger.D(this, "Requesting Map Data...");
                	connection.add(NewMessageFactory.creategetTileMapRequest());
                } else {
                	connection.add(NewMessageFactory.createGetPlayerRequest());
                }
                break;
            case JOIN_NEED:
                break;
            case JOIN_BAD:
                break;
            case JOIN_ERROR:
                Logger.toast(this, "JOIN_ERROR");
                break;

            case GETMAPDATA_OK:
                break;
            case GETMAPDATA_NEED:
                break;
            case GETMAPDATA_BAD:
                break;
            case GETMAPDATA_ERROR:
                Logger.toast(this, "GETMAPDATA_ERROR");
                break;

            case LEAVE_OK:
                break;
            case LEAVE_NEED:
                break;
            case LEAVE_BAD:
                break;
            case LEAVE_ERROR:
                Logger.toast(this, "LEAVE_ERROR");
                break;

            case LOGIN_OK:
                connection.add(NewMessageFactory.createJoinRequest());
                break;
            case LOGIN_NEED:
                break;
            case LOGIN_BAD:
                connection.add(NewMessageFactory.createRegisterRequest(Config.DEFAULT_USERNAME, Config.DEFAULT_PASSWORD));
                break;
            case LOGIN_ERROR:
                Logger.toast(this, "LOGIN_ERROR: most likely password or username wrong.");
                break;

            case REGISTER_OK:
                connection.add(NewMessageFactory.createJoinRequest());
                break;
            case REGISTER_NEED:
                break;
            case REGISTER_BAD:
                break;
            case REGISTER_ERROR:
                Logger.toast(this, "REGISTER_ERROR");
                break;
                
            case GETTILEMAP_ERROR:
            	Logger.e(world, "Server error! Tile Map was not received!");
            	connection.add(NewMessageFactory.createGetPlayerRequest());
            	break;
            case GETTILEMAP_OK:
            	if(world.supports2DTileMap()){
            		Logger.e(this,"Receiving Map Data, parsing...");
            		world.setTileMap(SendableDataConverter.toTileMap((ListNode<DataMap>) sendable.data));
            	} else {
            		Logger.e(world, "doesn't support Tile Maps!");
            	}
            	connection.add(NewMessageFactory.createGetPlayerRequest());
            	break;
            
            default:
            	sendable.recycle();
                break;
        }
        message.recycle();
        message = null;
    }

	private boolean belongsToActivePlayer(Score score) throws NoSuchElementException {
		return world.playerManager.getActivePlayer().useProperty(StringConstants.PROPERTY_FRAGSCORE_UID, GameObject.INVALID_UID).value() == score.getUid();
	}

    private void updateOrCreateEntity(@NotNull Entity serverEntity) {

        // new or update
        AbstractEntityView entityView = world.getViewById(serverEntity.getUid());
        if (entityView == null) {

            // new entityT
            entityView = world.createEntityView(serverEntity);
            world.add(entityView);

            // add to setup
            if(entityView instanceof EntityView){
            	Registry.getRenderer().addToSetupQueue(((EntityView) entityView).getObject3d());
            }

//            Logger.i(this, "Add new entity " + serverEntity.getUid());
        } else {
            // update
            entityView.update(serverEntity);
//            Logger.i(this, "Update existing entity " + serverEntity.getUid());
        }
    }

    // preallocate
    Entity sourceEntity;
    Entity targetEntity;

    public void handleEvent(GameEvent event) {
//		Logger.i(this, "Handle event "+event.getName()+" type: " + event.getType());

        switch (event.getType()) {
            case ACTION_ACCELERATE:
                SoundEngine.getInstance().playSound(sourceEntity.type, event.getType());
                break;
            case ACTION_DECELERATE:
                break;
            case ACTION_ENTEREXIT:
                break;
            case ACTION_EVENT:
                break;
            case ACTION_HANDBRAKE:
                break;
            case ACTION_NOISE:
                break;
            case ACTION_SHOOT:
            	store.reclaim(event);
            	event = null;
                break;
            case ACTION_SUICIDE:
                break;
            case ACTION_SWITCH_GUN_NEXT:
                break;
            case ACTION_SWITCH_GUN_PREV:
                break;
            case ACTION_TURNLEFT:
                break;
            case ENTITY_ACTIVATE:
                break;
            case ENTITY_BULLET_HIT:
                break;
            case ENTITY_COLLIDE:
            	store.reclaim(event);
            	event = null;
                break;
            case ENTITY_CREATE:
                break;
            case ENTITY_DAMAGE:
                break;
            case ENTITY_DEACTIVATE:
            	world.deactivate(event.getTargetUid());
            	store.reclaim(event);
            	event = null;
                break;
            case ENTITY_DESTROYED:
            	world.remove(event.getTargetUid());
            	store.reclaim(event.getTargetUid());
            	store.reclaim(event);
            	event = null;
                break;
            case ENTITY_EVENT:
                break;
            case ENTITY_REMOVE:
                break;
            case ENTITY_SENSE:
                break;
            case ENTITY_SENSE_DOOR:
                break;
            case ENTITY_SENSE_EXPLOSION:
                break;
            case ENTITY_UPDATE:
                break;

            case PLAYER_ENTERSCAR:
                break;
            case PLAYER_JOINS:
                break;
            case PLAYER_KILLED:
                break;
            case PLAYER_LOGIN:
                break;
            case PLAYER_LEAVES:
                break;
            case ENTITY_NEW_PLAYER:
            	
            	int playerUid = event.getTargetUid();
            	int entityUid = event.getSourceUid();

            	// player not active player
            	if (world.playerManager.getActivePlayer().getUid() != playerUid)
            		break;

                Player player = store.getPlayer(playerUid);
                if (player == null) {
                	throw new IllegalStateException("event target: player not found " + playerUid);
                }

                Entity serverEntity = store.getEntity(entityUid);
                if (serverEntity == null) {
                	throw new IllegalStateException("event source: entity not found " + playerUid);
                }

                // new active object
                AbstractEntityView entityView = world.getViewById(serverEntity.getUid());
                world.setActiveView(entityView);
                store.reclaim(event);
                Logger.i(this, "PLAYER_NEWENTITY " + entityView.entity.getUid());

                break;

            case PLAYER_POWERUP:
                break;
            case PLAYER_SCORES:
                break;
            case PLAYER_SPAWNS:
                break;
            case PLAYER_WINS:
                break;

            case SESSION_ENDS:
                break;
            case SESSION_STARTS:
                break;
            case SESSION_UPDATE:
                break;
            default:
            	store.reclaim(event);
        }
    }

}
