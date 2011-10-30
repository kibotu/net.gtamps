package net.gtamps.shared.communication;

public enum SendableType {

	BAD_MESSAGE,
	BAD_SENDABLE,
	
    // requests / responses
    GETUPDATE,
    GETPLAYER,
    JOIN,
    LEAVE,
    LOGIN,
    REGISTER,
    SESSION,
    GETMAPDATA,

    GETUPDATE_OK,
    GETPLAYER_OK,
    JOIN_OK,
    LEAVE_OK,
    LOGIN_OK,
    REGISTER_OK,
    SESSION_OK,
    GETMAPDATA_OK,

    GETUPDATE_NEED,
    GETPLAYER_NEED,
    JOIN_NEED,
    LEAVE_NEED,
    LOGIN_NEED,
    REGISTER_NEED,
    SESSION_NEED,
    GETMAPDATA_NEED,

    GETUPDATE_BAD,
    GETPLAYER_BAD,
    JOIN_BAD,
    LEAVE_BAD,
    LOGIN_BAD,
    REGISTER_BAD,
    SESSION_BAD,
    GETMAPDATA_BAD,

    GETUPDATE_ERROR,
    GETPLAYER_ERROR,
    JOIN_ERROR,
    LEAVE_ERROR,
    LOGIN_ERROR,
    REGISTER_ERROR,
    SESSION_ERROR,
    GETMAPDATA_ERROR,

    // commands
    ACCELERATE,
    DECELERATE,
    ENTEREXIT,
    SHOOT,
    HANDBRAKE,
    LEFT,
    RIGHT,
    SUICIDE;
    
    public SendableType getOKResponse() {
    	SendableType type = null;
    	switch(this) {
	    	case GETUPDATE: type=GETUPDATE_OK; break;
	    	case GETPLAYER: type=GETPLAYER_OK; break;
	    	case JOIN: type=JOIN_OK; break;
	    	case LEAVE: type=LEAVE_OK; break;
	    	case LOGIN: type=LOGIN_OK; break;
	    	case REGISTER: type=REGISTER_OK; break;
	    	case SESSION: type=SESSION_OK; break;
	    	case GETMAPDATA: type=GETMAPDATA_OK; break;
	    	default: break;
    	}
    	return type;
    }

    public SendableType getNeedResponse() {
    	SendableType type = null;
    	switch(this) {
	    	case GETUPDATE: type=GETUPDATE_NEED; break;
	    	case GETPLAYER: type=GETPLAYER_NEED; break;
	    	case JOIN: type=JOIN_NEED; break;
	    	case LEAVE: type=LEAVE_NEED; break;
	    	case LOGIN: type=LOGIN_NEED; break;
	    	case REGISTER: type=REGISTER_NEED; break;
	    	case SESSION: type=SESSION_NEED; break;
	    	case GETMAPDATA: type=GETMAPDATA_NEED; break;
	    	default: break;
    	}
    	return type;
    }

    public SendableType getBadResponse() {
    	SendableType type = null;
    	switch(this) {
	    	case GETUPDATE: type=GETUPDATE_BAD; break;
	    	case GETPLAYER: type=GETPLAYER_BAD; break;
	    	case JOIN: type=JOIN_BAD; break;
	    	case LEAVE: type=LEAVE_BAD; break;
	    	case LOGIN: type=LOGIN_BAD; break;
	    	case REGISTER: type=REGISTER_BAD; break;
	    	case SESSION: type=SESSION_BAD; break;
	    	case GETMAPDATA: type=GETMAPDATA_BAD; break;
	    	default: break;
    	}
    	return type;
    }

    public SendableType getErrorResponse() {
    	SendableType type = null;
    	switch(this) {
	    	case GETUPDATE: type=GETUPDATE_ERROR; break;
	    	case GETPLAYER: type=GETPLAYER_ERROR; break;
	    	case JOIN: type=JOIN_ERROR; break;
	    	case LEAVE: type=LEAVE_ERROR; break;
	    	case LOGIN: type=LOGIN_ERROR; break;
	    	case REGISTER: type=REGISTER_ERROR; break;
	    	case SESSION: type=SESSION_ERROR; break;
	    	case GETMAPDATA: type=GETMAPDATA_ERROR; break;
	    	default: break;
    	}
    	return type;
    }

    
}
