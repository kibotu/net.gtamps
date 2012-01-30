package net.gtamps.android.core.net;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.gtamps.android.core.input.layout.AbstractInputLayout;
import net.gtamps.android.game.PlayerManager;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.level.Tile;
import net.gtamps.shared.serializer.communication.NewMessage;

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

}
