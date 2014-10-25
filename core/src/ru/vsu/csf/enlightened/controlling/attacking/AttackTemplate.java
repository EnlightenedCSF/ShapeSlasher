package ru.vsu.csf.enlightened.controlling.attacking;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class AttackTemplate {

    public BodyDef bodyDef;
    public FixtureDef fixtureDef;

    public AttackTemplate(BodyDef bodyDef, FixtureDef fixtureDef) {
        this.bodyDef = bodyDef;
        this.fixtureDef = fixtureDef;
    }
}
