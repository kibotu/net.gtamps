package gtamapedit.view.map;

import gtamapedit.conf.Configuration;
import gtamapedit.tileManager.TileImageHolder;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;

import net.gtamps.shared.game.entity.Entity.Type;
import net.gtamps.shared.game.level.EntityPosition;

public class MapModel {
	
	private MapElement[][] map;
	public void setMap(MapElement[][] map, LinkedList<EntityPosition> eps) {
		this.map = map;
		this.entityList = eps;
	}
	private LinkedList<MapElement> selectedElements = new LinkedList<MapElement>();
	private HashMap<String,MapElement> commonProperties = new HashMap<String, MapElement>();
	private LinkedList<EntityPosition> entityList = new LinkedList<EntityPosition>();
	EntityPosition entitySelection = null;
	
	private int commonFloors = 0;
	private int commonRotation= 0;
	private TileImageHolder commonTopTexture = null;
	private TileImageHolder commonWestTexture = null;
	private TileImageHolder commonEastTexture = null;
	private TileImageHolder commonNorthTexture = null;
	private TileImageHolder commonSouthTexture = null;
	private Type currentEntityType;
	
	public EntityPosition getEntitySelection() {
		return entitySelection;
	}
	
	public HashMap<String, MapElement> getCommonProperties() {
		return commonProperties;
	}

	public MapModel(int sizeX, int sizeY){
		map = new MapElement[sizeX][sizeY];
		for(int x=0; x<sizeX; x++){
			for(int y=0; y<sizeY; y++){
				map[x][y] = new MapElement();
			}
		}
	}
	public MapElement[][] getMap() {
		return map;
	}
	public void changeSelection(Point selectionStart,Point selectionEnd) {
		selectedElements.clear();
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				if (selectionStart != null && selectionEnd != null) {
					if (x > Math.min(selectionStart.x / TileImageHolder.tileSize, selectionEnd.x / TileImageHolder.tileSize )-1
							&& x <= Math.max(selectionStart.x / TileImageHolder.tileSize, selectionEnd.x	/ TileImageHolder.tileSize ) 
							&& y > Math.min(selectionStart.y / TileImageHolder.tileSize, selectionEnd.y
									/ TileImageHolder.tileSize )-1
							&& y <= Math.max(selectionStart.y / TileImageHolder.tileSize, selectionEnd.y
									/ TileImageHolder.tileSize ) )  {
						this.map[x][y].setSelected(true);
						selectedElements.push(map[x][y]);
					} else {
						this.map[x][y].setSelected(false);
					}

				} else {
					this.map[x][y].setSelected(false);
				}
			}
		}
		findCommonProperties();
		
	}
	private void findCommonProperties() {
		boolean first = true;
		for(MapElement me: selectedElements){
			if(first){
				commonFloors = me.getFloors();
				commonRotation = me.getRotation();
				commonTopTexture = me.getTextureTop();
				commonEastTexture = me.getTextureEast();
				commonWestTexture = me.getTextureWest();
				commonNorthTexture = me.getTextureNorth();
				commonSouthTexture = me.getTextureSouth();
				first = false;
			} else {
				if(commonFloors != me.getFloors()){
					commonFloors = -1;
				}
				if(commonRotation != me.getRotation()){
					commonRotation = -1;
				}
				if(commonTopTexture == null || !commonTopTexture.equals(me.getTextureTop())){
					commonTopTexture = null;
				}
				if(commonEastTexture == null || !commonEastTexture.equals(me.getTextureEast())){
					commonEastTexture = null;
				}
				if(commonWestTexture == null || !commonWestTexture.equals(me.getTextureWest())){
					commonWestTexture = null;
				}
				if(commonNorthTexture == null || !commonNorthTexture.equals(me.getTextureNorth())){
					commonNorthTexture = null;
				}
				if(commonSouthTexture == null || !commonSouthTexture.equals(me.getTextureSouth())){
					commonSouthTexture = null;
				}
			}
			
		}
	}
	public LinkedList<MapElement> getSelectedElements() {
		return selectedElements;
	}
	
	public int getCommonFloors() {
		return commonFloors;
	}
	public int getCommonRotation() {
		return commonRotation;
	}
	public TileImageHolder getCommonTopTexture() {
		return commonTopTexture;
	}
	public TileImageHolder getCommonWestTexture() {
		return commonWestTexture;
	}
	public TileImageHolder getCommonEastTexture() {
		return commonEastTexture;
	}
	public TileImageHolder getCommonNorthTexture() {
		return commonNorthTexture;
	}
	public TileImageHolder getCommonSouthTexture() {
		return commonSouthTexture;
	}

	public EntityPosition getEntityAtPosition(Point p) {
		for(EntityPosition ep : entityList){
			if(Math.abs(ep.getPosition().x-p.x)<Configuration.ENTITY_SIZE && Math.abs(ep.getPosition().y-p.y)<Configuration.ENTITY_SIZE){
				return ep;
			}
		}
		entitySelection = null;
		//TODO create something else than a spawnpoint
		return null;
	}

	public EntityPosition createEntityAtPosition(Point pos, Type type) {
		EntityPosition ep = new EntityPosition(pos.x, pos.y, 0, 0, type);
		entityList.add(ep);
		return ep;
	}

	public void changeSelection(EntityPosition ep) {
		entitySelection = ep;
	}
	
	public LinkedList<EntityPosition> getEntityList() {
		return entityList;
	}

	public void setCurrentEntityType(Type t) {
		this.currentEntityType = t;
		
	}
	public Type getCurrentEntityType() {
		return currentEntityType;
	}

	public void deleteSelectedEntity() {
		if(entitySelection!=null){
			entityList.remove(entitySelection);
		}
	}
}
