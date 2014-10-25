package ru.vsu.csf.enlightened.controlling.attacking;

import com.badlogic.gdx.physics.box2d.Body;

public class CurrentAttack {

    public Body body;
    public float duration;

    public CurrentAttack(Body body, float duration) {
        this.body = body;
        this.duration = duration;
    }
}
