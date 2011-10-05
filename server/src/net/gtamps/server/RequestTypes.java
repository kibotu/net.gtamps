package net.gtamps.server;

/**
 * the types of requests a client can send and the exact wording of
 * the responses the server can give.
 *
 * @author jan, tom, til
 *
 */
public enum RequestTypes {
	IDENTIFY ("login", "ok", "confirm password", "bad login"),
	REGISTER ("register", "ok", "confirm password", "bad register"),
	JOIN ("join", "ok", "please identify", "bad join"),
	LEAVE("leave", "ok", "please identify", "bad leave"),
	GETMAPDATA("getmapdata", "ok", "please identify", "no mapdata"),
	GETPLAYER("getplayer", "ok", "please identify", "bad player"),
	GETUPDATE("update", "ok", "please identify", "bad revision id");
	
	public static RequestTypes findByTypeString(String typeString) {
		for (RequestTypes r : RequestTypes.values()) {
			if (r.typeString.equalsIgnoreCase(typeString)) {
				return r;
			}
		}
		throw new IllegalArgumentException();
	}
	
	private final String typeString;
	private final String okResponse;
	private final String needResponse;
	private final String badResponse;
	private RequestTypes(String typeString, String okResponse, String needResponse, String badResponse) {
		this.typeString = typeString;
		this.okResponse = okResponse;
		this.needResponse = needResponse;
		this.badResponse = badResponse;
	}
	
	/**
	 * what the value of the <code>type</code> attribute of the &lt;request;gt;
	 * will look like 
	 * 
	 * @return
	 */
	public String typeString() {
		return this.typeString;
	}
	
	/**
	 * the positive answer: request granted.
	 * 
	 * @return
	 */
	public String okResponse() {
		return this.okResponse;
	}

	/**
	 * the undecided answer: the server needs some more info.
	 * 
	 * @return
	 */
	public String needResponse() {
		return this.needResponse;
	}
	
	/**
	 * definitely the negative answer: request denied.
	 * 
	 * @return
	 */
	public String badResponse() {
		return this.badResponse;
	}

}
