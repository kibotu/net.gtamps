package net.gtamps.shared.game;

public class GameData {

	public final String name;
	public final String hash;
	public final int numPlayers;
	public final int pingMillis;
	public final long runtimeMillis;
	public final String imgUrl;

	public GameData(final String name, final String hash, final int numPlayers, final int pingMillis, final long runtimeMillis, final String imgUrl) {
		super();
		this.name = name;
		this.hash = hash;
		this.numPlayers = numPlayers;
		this.pingMillis = pingMillis;
		this.runtimeMillis = runtimeMillis;
		this.imgUrl = imgUrl;
	}

}
