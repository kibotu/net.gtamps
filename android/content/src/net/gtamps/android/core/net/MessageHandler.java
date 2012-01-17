package net.gtamps.android.core.net;

import java.util.List;

import net.gtamps.android.core.sound.SoundEngine;
import net.gtamps.android.game.content.EntityView;
import net.gtamps.android.game.content.scenes.World;
import net.gtamps.android.renderer.Registry;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.GameobjectStore;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.player.Player;
import net.gtamps.shared.serializer.ConnectionManager;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.communication.NewMessageFactory;
import net.gtamps.shared.serializer.communication.NewSendable;
import net.gtamps.shared.serializer.communication.SendableCacheFactory;
import net.gtamps.shared.serializer.communication.SendableFactory;
import net.gtamps.shared.serializer.communication.StringConstants;
import net.gtamps.shared.serializer.communication.data.DataMap;
import net.gtamps.shared.serializer.communication.data.ListNode;
import net.gtamps.shared.serializer.communication.data.PlayerData;
import net.gtamps.shared.serializer.communication.data.SendableDataConverter;
import net.gtamps.shared.serializer.communication.data.Value;

import org.jetbrains.annotations.NotNull;

public class MessageHandler {

	private SendableFactory sendableFactory = new SendableFactory(new SendableCacheFactory());
	private GameobjectStore store = new GameobjectStore();
    private ConnectionManager connection;
    private World world;

    public MessageHandler(ConnectionManager connection, World world) {
        this.connection = connection;
        this.world = world;
    }

    public void handleMessage(NewSendable sendable, NewMessage message) {

        Logger.i(this, "Handles message.");
//        Logger.i(this, sendable);

        switch (sendable.type) {
            case GETUPDATE_OK:
                // empty
                if (sendable.data == null)
                    break;

                // update revision id
                DataMap updateData = (DataMap) sendable.data;
                connection.currentRevId = ((Value<Long>)updateData.get(StringConstants.UPDATE_REVSION)).get();

                // parse all transmitted entities
                ListNode<DataMap> entities = ((ListNode<DataMap>)updateData.get(StringConstants.UPDATE_ENTITIES));
                for (DataMap emap: entities) {
                	int uid = SendableDataConverter.getGameObjectUid(emap);
                	Entity e = store.getEntity(uid);
                	SendableDataConverter.updateGameobject(e, emap);
                	updateOrCreateEntity(e);
                }

                ListNode<DataMap> events = ((ListNode<DataMap>)updateData.get(StringConstants.UPDATE_GAMEEVENTS));
                for (DataMap emap: events) {
                	int uid = SendableDataConverter.getGameObjectUid(emap);
                	GameEvent e = store.getGameEvent(uid);
                	SendableDataConverter.updateGameobject(e, emap);
                	handleEvent(e);
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
                DataMap pmap = ((DataMap) sendable.data).get(StringConstants.PLAYER_DATA);
                
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
                connection.add(NewMessageFactory.createLoginRequest(Config.DEFAULT_USERNAME, Config.DEFAULT_PASSWORD));
                break;
            case SESSION_NEED:
                break;
            case SESSION_BAD:
                break;
            case SESSION_ERROR:
                Logger.toast(this, "SESSION_ERROR");
                break;

            case JOIN_OK:
                connection.add(NewMessageFactory.createGetPlayerRequest());
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
            default:
                break;
        }
    }

    private void updateOrCreateEntity(@NotNull Entity serverEntity) {

        // new or update
        EntityView entityView = world.getViewById(serverEntity.getUid());
        if (entityView == null) {

            // new entity
            entityView = new EntityView(serverEntity);
            world.add(entityView);

            // add to setup
            Registry.getRenderer().addToSetupQueue(entityView.getObject3d());

//            Logger.i(this, "Add new entity " + serverEntity.getUid());
        } else {
            // update
            entityView.update(serverEntity);
            Logger.i(this, "Update existing entity " + serverEntity.getUid());
        }
    }

    // preallocate
    Entity sourceEntity;
    Entity targetEntity;

    public void handleEvent(GameEvent event) {
//		Logger.i(this, "Handle event " + event);

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
                break;
            case ENTITY_CREATE:
                break;
            case ENTITY_DAMAGE:
                break;
            case ENTITY_DEACTIVATE:
                break;
            case ENTITY_DESTROYED:
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

                // target no player
                if (!(event.getTarget() instanceof Player))
                    break;

                // player not active player
                Player player = (Player) event.getTarget();

                if (!world.playerManager.getActivePlayer().equals(player))
                    break;

                // source no entity
                if (!(event.getSource() instanceof Entity))
                    break;
                Entity serverEntity = (Entity) event.getSource();

                // new active object
                EntityView entityView = world.getViewById(serverEntity.getUid());
                world.setActiveView(entityView);

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
        }
    }

}
