package net.gtamps.server;

/**
 * the types of requests a client can send and the exact wording of
 * the responses the server can give.
 *
 * @deprecated	not used anymore; kept on for reference and legacy actionscript code.
 * 				replaced by net.gtamps.shared.communication.SendableType
 * @see net.gtamps.shared.communication.SendableType
 * @author jan, tom, til
 *
 */
@Deprecated
public enum RequestTypes {
	IDENTIFY ("login", "ok", "confirm password", "bad login"),
	REGISTER ("register", "ok", "confirm password", "bad register"),
	JOIN ("join", "ok", "please identify", "bad join"),
	LEAVE("leave", "ok", "please identify", "bad leave"),
	GETMAPDATA("getmapdata", "ok", "please identify", "no mapdata"),
	GETPLAYER("getplayer", "ok", "please identify", "bad player"),
	GETUPDATE("update", "ok", "please identify", "bad revision id");
	
	public static RequestTypes findByTypeString(final String typeString) {
		for (final RequestTypes r : RequestTypes.values()) {
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
	private RequestTypes(final String typeString, final String okResponse, final String needResponse, final String badResponse) {
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
		return typeString;
	}
	
	/**
	 * the positive answer: request granted.
	 * 
	 * @return
	 */
	public String okResponse() {
		return okResponse;
	}

	/**
	 * the undecided answer: the server needs some more info.
	 * 
	 * @return
	 */
	public String needResponse() {
		return needResponse;
	}
	
	/**
	 * definitely the negative answer: request denied.
	 * 
	 * @return
	 */
	public String badResponse() {
		return badResponse;
	}

}
