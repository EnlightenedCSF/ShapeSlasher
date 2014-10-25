package ru.vsu.csf.enlightened.controlling.attacking;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class AttackTemplate {

    public BodyDef bodyDef;
    public FixtureDef fixtureDef;
    public int damage;
    public float knockbackPower;
    public Vector2 knockbackDirection;

    public AttackTemplate(BodyDef bodyDef, FixtureDef fixtureDef, int damage, float knockbackPower, Vector2 knockbackDirection) {
        this.bodyDef = bodyDef;
        this.fixtureDef = fixtureDef;
        this.damage = damage;
        this.knockbackPower = knockbackPower;
        this.knockbackDirection = knockbackDirection;
    }
}
