package ru.vsu.csf.enlightened.gameobjects.hero;

import ru.vsu.csf.enlightened.ShapeSlasher;

public enum Facing {
    LEFT,
    RIGHT;

    public static float getAngle(Facing facing) {
        switch (facing) {
            case LEFT:
                return 180 * ShapeSlasher.DEGREES_TO_RADIANS;
            case RIGHT:
                return 0;
            default:
                break;
        }
        return 0;
    }
}
