package net.gtamps.game.conf;

public enum PhysicalProperties {

    Sportscar(1000f, // MASS
            1f, // DENSITY
            0.1f, // FRICTION
            0.5f, // RESTITUTION
            1500f, // VELOCITY FORCE
            200f, // STEERING FORCE
            3f, // STEERING RADIUS
            0.5f, // SLIDYNESS
            100f, // MAX SPEED
            1f, // LINEAR DAMPING
            2f,    // ANGULAR DAMPING
            Type.CAR
    ),

    Taxi(1000f, // MASS
            1.4f, // DENSITY
            0.15f, // FRICTION
            0.5f, // RESTITUTION
            1000f, // VELOCITY FORCE
            100f, // STEERING FORCE
            3f, // STEERING RADIUS
            0.2f, // SLIDYNESS
            30f, // MAX SPEED
            1f, // LINEAR DAMPING
            2f,    // ANGULAR DAMPING
            Type.CAR
    ),

    Human(0f, // MASS
            1f, // DENSITY
            0.1f, // FRICTION
            0.5f, // RESTITUTION
            10f, //
            //5f, // VELOCITY FORCE
            2f, // STEERING FORCE
            0f, // STEERING RADIUS
            0f, // SLIDYNESS
            //5f, // MAX SPEED
            10f, // MAX SPEED
            0.01f, // LINEAR DAMPING
            1.05f,    // ANGULAR DAMPING
            Type.HUMAN
    ),
    Bullet(0f, // MASS
            100f, // DENSITY
            0.1f, // FRICTION
            0.5f, // RESTITUTION
            5f, // VELOCITY FORCE
            2f, // STEERING FORCE
            0f, // STEERING RADIUS
            0f, // SLIDYNESS
            5f, // MAX SPEED
            0.01f, // LINEAR DAMPING
            1.05f,    // ANGULAR DAMPING
            Type.BULLET
    ),

    //TODO rather a workaround for non dynamic bodies.
    //but this might be a nice implementation for staying modular.
    Empty(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, Type.NONE);


    public float MASS;
    public float DENSITY;
    public float FRICTION;
    public float RESTITUTION;
    public float VELOCITY_FORCE;
    public float STEERING_FORCE;
    public float STEERING_RADIUS;
    public float SLIDYNESS;
    public float LINEAR_DAMPING;
    public float ANGULAR_DAMPING;
    public float MAX_SPEED;
    public Type TYPE;

    public enum Type {
        CAR, HUMAN, NONE, BULLET
    }

    PhysicalProperties(float mass, float density, float friction, float restitution, float velocityForce,
                       float steeringForce, float steeringRadius, float slidyness, float max_speed, float linear_damping,
                       float angular_damping, Type type) {
        MASS = mass; // OBSOLETE: Mass is calculated according to the density
        DENSITY = density;
        FRICTION = friction;
        RESTITUTION = restitution;
        VELOCITY_FORCE = velocityForce;
        STEERING_FORCE = steeringForce;
        STEERING_RADIUS = steeringRadius;
        SLIDYNESS = slidyness;    // a float between 0f and 1f where 0f represents
        // a car running on tracks and 1f represents a
        // floating car
        MAX_SPEED = max_speed;
        this.TYPE = type;
    }

    PhysicalProperties(PhysicalProperties pp) {
        MASS = pp.MASS; // OBSOLETE: Mass is calculated according to the density
        DENSITY = pp.DENSITY;
        FRICTION = pp.FRICTION;
        RESTITUTION = pp.RESTITUTION;
        VELOCITY_FORCE = pp.VELOCITY_FORCE;
        STEERING_FORCE = pp.STEERING_FORCE;
        STEERING_RADIUS = pp.STEERING_RADIUS;
        SLIDYNESS = pp.SLIDYNESS;    // a float between 0f and 1f where 0f represents
        // a car running on tracks and 1f represents a
        // floating car
        MAX_SPEED = pp.MAX_SPEED;
        TYPE = pp.TYPE;
    }

}
