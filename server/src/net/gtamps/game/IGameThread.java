package net.gtamps.game;

import net.gtamps.Command;
import net.gtamps.shared.game.player.Player;

import org.jdom.Element;

public interface IGameThread extends Runnable{
	

	public void start();
	
	/**
	 * This method takes a revision id from the client and returns all
	 * objects that have changed since this revision as a jdom.Document
	 * 
	 * Please see doku/message_xml_layout for xml specs.
	 * @param revisionId 
	 * 	the revisionId representing the current known game-state of the client 
	 * @return
	 * 	a jdom xml containing all changed objects and their attributes
	 */
	Element getUpdatesAsXML(long revisionId);
	
	/**
	 * This method lets the game-engine computer all the necessary steps
	 * for the given command.
	 * @param playeruid
	 * 	the player uid who send the command
	 * @param cmd
	 * 	the command executed on the client
	 */
	void command(int playeruid, Command cmd); 
	
	/**
	 * Returns the map and all the corresponding data for the client as jdom document
	 * @return
	 */
	Element getMapData();
	
	Player getPlayer(int uid);
	boolean joinPlayer(int uid);
	void leavePlayer(int uid);
	int createPlayer(String name);
}
