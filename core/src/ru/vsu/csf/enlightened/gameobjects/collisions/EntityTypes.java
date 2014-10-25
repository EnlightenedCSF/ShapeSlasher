package ru.vsu.csf.enlightened.gameobjects.collisions;

public class EntityTypes {

    public static final short GROUND_CATEGORY = 1;    // 0 0 0 1
    public static final short HERO_CATEGORY = 2;      // 0 0 1 0
    public static final short SWORD_CATEGORY = 4;    // 0 1 0 0
    public static final short ENEMY_CATEGORY = 8;    // 1 0 0 0

    public static final short GROUND_MASK = 10;   // 1 0 1 0
    public static final short HERO_MASK = 9;    // 1 0 0 1
    public static final short SWORD_MASK = 8;    // 1 0 0 0
    public static final short ENEMY_MASK = 7;    // 0 1 1 1
}
