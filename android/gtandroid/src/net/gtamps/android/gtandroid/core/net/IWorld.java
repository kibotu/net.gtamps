package net.gtamps.android.gtandroid.core.net;

import net.gtamps.android.gtandroid.core.input.layout.AbstractInputLayout;
import net.gtamps.android.gtandroid.game.PlayerManager;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.level.Tile;
import net.gtamps.shared.serializer.communication.NewMessage;

import java.util.LinkedList;
import java.util.List;

public interface IWorld {

    public PlayerManager playerManager = new PlayerManager();
    AbstractInputLayout layout = null;
    NewMessage message = null;

    AbstractEntityView getViewById(int uid);

    //	void add(IEntityView entityView);
    void setActiveView(AbstractEntityView entityView);

    void add(AbstractEntityView entityView);

    List<AbstractEntityView> getAllEntities();

    AbstractEntityView getActiveView();

    AbstractEntityView createEntityView(Entity e);

    public boolean supports2DTileMap();

    void setTileMap(LinkedList<Tile> linkedList);

    void remove(int targetUid);

    void setPlayerFragScore(int count);

    int getPlayerFragScore();

    void deactivate(int targetUid);

    void invokeExplosion(AbstractEntityView viewById);

}
