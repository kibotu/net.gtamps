package net.gtamps.shared.communication;

public enum SendableType {

    // requests
    GETUPDATE,
    GETPLAYER,
    JOIN,
    LEAVE,
    LOGIN,
    REGISTER,
    SESSION,
    GETMAPDATA,

    // responses
    OK,
    NEED,
    BAD,
    ERROR,

    // commands
    ACCELERATE,
    DECELERATE,
    ENTEREXIT,
    SHOOT,
    HANDBRAKE,
    LEFT,
    RIGHT
}
