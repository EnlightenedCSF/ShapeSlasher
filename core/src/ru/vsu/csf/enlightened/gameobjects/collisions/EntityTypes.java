package ru.vsu.csf.enlightened.gameobjects.collisions;

public class EntityTypes {

    public static final short GROUND            = 0x1;    // 0 0 0 0 1
    public static final short HERO              = 0x2;    // 0 0 0 1 0
    public static final short SWORD             = 0x4;    // 0 0 1 0 0
    public static final short ENEMY             = 0x8;    // 0 1 0 0 0
    public static final short SENSOR            = 0x10;   // 1 0 0 0 0

    public static final short GROUND_MASK   = 0x1a;   // 1 1 0 1 0
    public static final short HERO_MASK     = 0x9;    // 0 1 0 0 1
    public static final short SWORD_MASK    = 0x8;    // 0 1 0 0 0
    public static final short ENEMY_MASK    = 0x17;   // 1 0 1 1 1
    public static final short SENSOR_MASK   = 31;     // 1 1 1 1 1
}
