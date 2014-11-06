package ru.vsu.csf.enlightened.controlling.attacking;

import com.badlogic.gdx.physics.box2d.Body;
import ru.vsu.csf.enlightened.gameobjects.hero.Facing;

public class CurrentAttack {
    public int index;
    public Facing facing;
    public Body body;
    public float duration;

    public CurrentAttack(int index, Facing facing, Body body, float duration) {
        this.index = index;
        this.facing = facing;
        this.body = body;
        this.duration = duration;
    }
}