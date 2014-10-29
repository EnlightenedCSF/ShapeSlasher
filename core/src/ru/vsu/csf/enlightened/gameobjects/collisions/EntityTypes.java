package ru.vsu.csf.enlightened.gameobjects.collisions;

public class EntityTypes {
    public static final short GROUND            = 0x1;    // 0 0 0 0 | 0 0 0 1
    public static final short HERO              = 0x2;    // 0 0 0 0 | 0 0 1 0
    public static final short SWORD             = 0x4;    // 0 0 0 0 | 0 1 0 0
    public static final short ENEMY             = 0x8;    // 0 0 0 0 | 1 0 0 0
    public static final short JUMP_SENSOR       = 0x10;   // 0 0 0 1 | 0 0 0 0
    public static final short PROJECTILE        = 0x20;   // 0 0 1 0 | 0 0 0 0
    public static final short ENEMY_VISION_S    = 0x40;   // 0 1 0 0 | 0 0 0 0
    public static final short ENEMY_OBSTACLE_S  = 0x80;   // 1 0 0 0 | 0 0 0 0

    public static final short GROUND_MASK       = 0xba;   // 1 0 1 1 | 1 0 1 0
    public static final short HERO_MASK         = 0x49;   // 0 1 0 0 | 1 0 0 1
    public static final short SWORD_MASK        = 0x8;    // 0 0 0 0 | 1 0 0 0
    public static final short ENEMY_MASK        = 0x37;   // 0 0 1 1 | 0 1 1 1
    public static final short JUMP_SENSOR_MASK  = 0x9;    // 0 0 0 0 | 1 0 0 1
    public static final short PROJECTILE_MASK   = 0x9;    // 0 0 0 0 | 1 0 0 1
    public static final short ENEMY_VISION_M    = 0x2;    // 0 0 0 0 | 0 0 1 0
    public static final short ENEMY_OBSTACLE_M  = 0x1;    // 0 0 0 0 | 0 0 0 1
}